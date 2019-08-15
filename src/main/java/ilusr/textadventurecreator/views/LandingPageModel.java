package ilusr.textadventurecreator.views;

import java.awt.Desktop;
import java.net.URI;

import ilusr.core.environment.EnvironmentUtilities;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Window;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LandingPageModel {

	private final TextAdventureProjectManager projectManager;
	private final ISettingsManager settingsManager;
	private SimpleBooleanProperty hideLanding;
	private LanguageAwareString newProjectText;
	private LanguageAwareString loadProjectText;
	private LanguageAwareString tutorialText;
	private LanguageAwareString findMoreText;
	private LanguageAwareString dontShowText;
	private LanguageAwareString titleText;
	private LanguageAwareString tagLineText;
	
	/**
	 * 
	 * @param projectManager A @see TextAdventureProjectManager to manage the current project.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LandingPageModel(TextAdventureProjectManager projectManager, 
							ISettingsManager settingsManager,
							ILanguageService languageService) {
		this.projectManager = projectManager;
		this.settingsManager = settingsManager;
		
		hideLanding = new SimpleBooleanProperty(!settingsManager.getBooleanSetting(SettingNames.LANDING, true));
		newProjectText = new LanguageAwareString(languageService, DisplayStrings.CREATE_NEW_PROJECT);
		loadProjectText = new LanguageAwareString(languageService, DisplayStrings.LOAD_EXISTING_PROJECT);
		tutorialText = new LanguageAwareString(languageService, DisplayStrings.TUTORIAL);
		findMoreText = new LanguageAwareString(languageService, DisplayStrings.FIND_OUT_MORE);
		dontShowText = new LanguageAwareString(languageService, DisplayStrings.DONT_SHOW_AGAIN);
		titleText = new LanguageAwareString(languageService, DisplayStrings.TEXTADVENTURECREATOR);
		tagLineText = new LanguageAwareString(languageService, DisplayStrings.CREATOR_TAG_LINE);
		
		initialize();
	}
	
	private void initialize() {
		hideLanding.addListener((v, o, n) -> {
			settingsManager.setBooleanValue(SettingNames.LANDING, !n);
		});
	}
	
	/**
	 * Creates a new project.
	 */
	public void createNewProject() {
		try {
			projectManager.createNewProject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempts to open a project from the file system.
	 * 
	 * @param window The window of the current application.
	 */
	public void openProject(Window window) {
		projectManager.openProject(window);
	}
	
	/**
	 * 
	 * @return If the landing page should be hidden.
	 */
	public SimpleBooleanProperty hideLanding() {
		return hideLanding;
	}
	
	/**
	 * 
	 * @return Display string for new project.
	 */
	public SimpleStringProperty newProjectText() {
		return newProjectText;
	}
	
	/**
	 * 
	 * @return Display string for load project.
	 */
	public SimpleStringProperty loadProjectText() {
		return loadProjectText;
	}
	
	/**
	 * 
	 * @return Display string for tutorial.
	 */
	public SimpleStringProperty tutorialText() {
		return tutorialText;
	}
	
	/**
	 * 
	 * @return Display string for find out more.
	 */
	public SimpleStringProperty findMoreText() {
		return findMoreText;
	}
	
	/**
	 * 
	 * @return Display string for do not show.
	 */
	public SimpleStringProperty dontShowText() {
		return dontShowText;
	}
	
	/**
	 * 
	 * @return Title display string.
	 */
	public SimpleStringProperty titleText() {
		return titleText;
	}
	
	/**
	 * 
	 * @return Tag line display text.
	 */
	public SimpleStringProperty tagLineText() {
		return tagLineText;
	}
	
	/**
	 * Opens a web page for a tutorial.
	 */
	public void showTutorial() {
		openWebPage("http://ilusr.com/textadventurecreator/FirstProject");
	}
	
	/**
	 * Opens a web page for more information about this application.
	 */
	public void showMore() {
		openWebPage("http://ilusr.com/textadventurecreator");
	}
	
	private void openWebPage(String url) {
		try {
			if (EnvironmentUtilities.isUnix()) {
				if (Runtime.getRuntime().exec(new String[] { "which", "xdg-open" }).getInputStream().read() != -1) {
                	Runtime.getRuntime().exec(new String[] { "xdg-open", url });
            	} else {
					LogRunner.logger().info("Unable to show page for linux machine.");
				}
			}
			else if (Desktop.isDesktopSupported()) {
				LogRunner.logger().info(String.format("Showing web page %s", url));
				Desktop.getDesktop().browse(new URI(url));
			} else {
				LogRunner.logger().info("Unable to show page");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
