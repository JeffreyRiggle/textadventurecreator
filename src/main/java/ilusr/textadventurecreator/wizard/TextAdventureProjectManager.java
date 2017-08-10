package ilusr.textadventurecreator.wizard;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.core.io.StreamUtilities;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.codegen.ProjectBuilder;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.shell.LayoutApplicationService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.Wizard;
import ilusr.textadventurecreator.views.WizardPage;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

/**
 *
 * @author Jeff Riggle
 *
 */
public class TextAdventureProjectManager {

	private final TextAdventureProvider provider;
	private final IDialogService dialogService;
	private final MediaFinder mediaFinder;
	private final ProjectPersistenceManager persistenceManager;
	private final ProjectStatusService statusService;
	private final ILanguageService languageService;
	private final ILayoutService layoutService;
	private final ISettingsManager settingsManager;
	private final LayoutApplicationService layoutApplicationService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;

	/**
	 *
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param mediaFinder A @see MediaFinder to find and convert media files.
	 * @param persistenceManager A @see ProjectPersistenceManager to manage the projects saves.
	 * @param statusService A @see ProjectStatusService to display the projects status.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param layoutService A @see LayoutService to manage layouts.
	 * @param settingsManager A @see SettingsManager to save and retrieve settings.
	 * @param layoutApplicationService A @see LayoutApplicationService to notify watchers of applied layouts.
	 */
	public TextAdventureProjectManager(TextAdventureProvider provider,
									   IDialogService dialogService,
									   MediaFinder mediaFinder,
									   ProjectPersistenceManager persistenceManager,
									   ProjectStatusService statusService,
									   ILanguageService languageService,
									   ILayoutService layoutService,
									   ISettingsManager settingsManager,
									   LayoutApplicationService layoutApplicationService,
									   IStyleContainerService styleService,
									   InternalURLProvider urlProvider) {
		this.provider = provider;
		this.dialogService = dialogService;
		this.mediaFinder = mediaFinder;
		this.persistenceManager = persistenceManager;
		this.statusService = statusService;
		this.languageService = languageService;
		this.layoutService = layoutService;
		this.settingsManager = settingsManager;
		this.layoutApplicationService = layoutApplicationService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}

	/**
	 * Creates a new project and displays a wizard.
	 *
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createNewProject() throws TransformerConfigurationException, ParserConfigurationException {
		LogRunner.logger().log(Level.INFO, "Creating new project.");

		TextAdventureProjectPersistence project = new TextAdventureProjectPersistence(layoutService, settingsManager);

		GameSettingsModel model = new GameSettingsModel(project, mediaFinder, languageService);
		List<WizardPage<GameSettingsModel>> pages = new ArrayList<WizardPage<GameSettingsModel>>();
		pages.add(new GameSettingsWizardPage(new GameInfoWizardView(), false, styleService, urlProvider));
		pages.add(new GameSettingsWizardPage(new GameTypeWizardView(), false, styleService, urlProvider));
		pages.add(new GameTypeSettingsWizardPage(new HostedGameWizardView(), new StandAloneGameWizardView(), model, true, styleService, urlProvider));
		pages.add(new GameSettingsWizardPage(new GameConfigurationWizardView(), true, styleService, urlProvider));
		Wizard<GameSettingsModel> wizard = new Wizard<GameSettingsModel>(pages, model, languageService, styleService, urlProvider);

		wizard.setOnCancel(() -> {
			LogRunner.logger().log(Level.INFO, "New project has been cancelled.");
		});

		wizard.setOnFinish((m) -> {
			LogRunner.logger().log(Level.INFO, "New project has been created.");
			tryAddInitialGameState(m.persistenceObject().getTextAdventure());
			provider.setTextAdventure(m.persistenceObject());
			tryApplyDefaultLayout();
		});

		LogRunner.logger().log(Level.INFO, "Attempting to create new project.");
		dialogService.displayModal(wizard);
	}

	private void tryAddInitialGameState(TextAdventurePersistenceObject tav) {
		try {
			tav.addGameState(new GameStatePersistenceObject(languageService.getValue(DisplayStrings.START_STATE)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void tryApplyDefaultLayout() {
		Platform.runLater(() -> {
			try {
				LogRunner.logger().log(Level.INFO, "Applying default layout for new project.");
				layoutService.loadLayout(StreamUtilities.getStreamContents(getClass().getResourceAsStream("defaultlayout.xml")));
				layoutApplicationService.applyLayout();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Attempts to open an existing project.
	 *
	 * @param window The window to show over.
	 */
	public void openProject(Window window) {
		FileChooser chooser = new FileChooser();

		ExtensionFilter ext = new ExtensionFilter("Text Adventure", "*.txaml");
		chooser.getExtensionFilters().add(ext);

		File loadFile = chooser.showOpenDialog(window);

		if (loadFile == null) {
			return;
		}

		LogRunner.logger().log(Level.INFO, String.format("Attempting to open project from %s", loadFile.getAbsolutePath()));
		tryOpenFile(loadFile);
	}

	private void tryOpenFile(File file) {
		persistenceManager.loadAsync(file.getAbsolutePath(), (c) -> {
			LogRunner.logger().log(Level.INFO, "Successfully opened project.");
			provider.setTextAdventure(c);

			if (c.layout() == null || c.layout().isEmpty()) {
				return;
			}

			Platform.runLater(() -> {
				LogRunner.logger().log(Level.INFO, "Applying layout for project.");
				layoutService.loadLayout(c.layout());
				layoutApplicationService.applyLayout();
			});
		});
	}

	/**
	 *
	 * @param project The project to publish.
	 *
	 * @param window The window to use.
	 */
	public void publish(TextAdventureProjectPersistence project, Window window) {
		if (!project.getIsStandAlone()) {
			publishHosted(project, window);
			return;
		}

		publishStandAlone();
	}

	private void publishHosted(TextAdventureProjectPersistence project, Window window) {
		LogRunner.logger().log(Level.INFO, "Preparing to publish hosted project.");

		File customGameDir = new File(System.getProperty("user.home") + "/ilusr/CustomGames");
		if (!customGameDir.exists()) {
			customGameDir.mkdirs();
		}

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setInitialDirectory(customGameDir);
		final File dir = chooser.showDialog(window);

		if (dir == null) {
			return;
		}

		final StatusItem item = new StatusItem(languageService.getValue(DisplayStrings.PUBLISHING), null, 10000);
		item.setOnStart(() -> {
			new Thread(() -> {
				publishHostedAction(project, dir, item);
			}).start();
		});

		statusService.addStatusItemToQueue(item);
	}

	private void publishHostedAction(TextAdventureProjectPersistence project, File dir, StatusItem item) {
		try {
			LogRunner.logger().log(Level.INFO, "Publishing hosted project.");

			item.displayText().set(languageService.getValue(DisplayStrings.SETUP_GAME_INFO));
			item.progressAmount().set(.2);
			File gameDir = new File(String.format("%s/%s/", dir.getAbsolutePath(), project.getGameInfo().gameName()));
			gameDir.mkdirs();

			item.progressAmount().set(.3);
			File originalIcon = new File(project.getIconLocation());
			String extension = project.getIconLocation().substring(project.getIconLocation().lastIndexOf('.'));
			File gameIcon = new File(String.format("%s/gameicon%s", gameDir.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), gameIcon.toPath());

			File gameInfo = new File(gameDir.getAbsolutePath() + "/info.xml");
			XmlConfigurationManager manager = new XmlConfigurationManager(gameInfo.getAbsolutePath());
			project.getGameInfo().prepareXml();
			manager.addConfigurationObject(project.getGameInfo());
			manager.save();

			item.displayText().set(languageService.getValue(DisplayStrings.MOVE_ASSETS));
			item.progressAmount().set(.5);
			File gameAssets = new File(gameDir.getAbsolutePath() + "/assets/");
			gameAssets.mkdirs();

			String newTrans = tryCopyToAssets(project.getTextAdventure().transition().mediaLocation(), gameAssets);
			if (!newTrans.isEmpty()) {
				project.getTextAdventure().transition().mediaLocation(newTrans);
			}

			moveGameMedia(project.getTextAdventure(), gameAssets);

			item.displayText().set(languageService.getValue(DisplayStrings.BUILDING_GAME));
			item.progressAmount().set(.8);
			File saves = new File(gameDir.getAbsolutePath() + "/saves/");
			saves.mkdirs();
			XmlConfigurationManager man = new XmlConfigurationManager(saves + "/game.xml");
			project.prepareXml();
			man.addConfigurationObject(project.getTextAdventure());
			item.progressAmount().set(.9);
			man.save();
			item.progressAmount().set(1.0);
			item.indicator().set(StatusIndicator.Good);
		} catch (Exception e) {
			e.printStackTrace();
			item.indicator().set(StatusIndicator.Error);
		}

		item.finished().set(true);
	}

	private void moveGameMedia(TextAdventurePersistenceObject textAdventure, File assets) {
		for (GameStatePersistenceObject gameState : textAdventure.gameStates()) {
			if (gameState.layout().getLayoutContent() == null || gameState.layout().getLayoutContent().isEmpty()) {
				continue;
			}

			String newFile = tryCopyToAssets(gameState.layout().getLayoutContent(), assets);
			if (!newFile.isEmpty()) {
				gameState.layout().setLayoutContent(newFile);
			}
		}
	}

	private String tryCopyToAssets(String original, File assets) {
		String retVal = "";
		try {
			File originalFile = new File(original);
			int start = original.lastIndexOf('\\');
			start = start == -1 ? original.lastIndexOf('/') : start;
			String newName = original.substring(start);
			File newMedia = new File(assets.getAbsolutePath() + newName);
			Files.copy(originalFile.toPath(), newMedia.toPath());
			retVal = newMedia.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	private void publishStandAlone() {
		LogRunner.logger().log(Level.INFO, "Preparing to publish stand alone project.");

		final StatusItem item = new StatusItem(languageService.getValue(DisplayStrings.BUILDING), null, 10000);
		item.setOnStart(() -> {
			new Thread(() -> {
				ProjectBuilder builder = new ProjectBuilder(provider.getTextAdventureProject(), languageService);
				builder.build(item);
			}).start();
		});

		statusService.addStatusItemToQueue(item);
	}
}
