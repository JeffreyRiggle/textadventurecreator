package ilusr.textadventurecreator.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.DockPosition;
import ilusr.iroshell.core.LocationProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IToolBarService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.toolbars.DebugSingleToolBarCommand;
import ilusr.textadventurecreator.toolbars.DebugToolBarCommand;
import ilusr.textadventurecreator.toolbars.NewProjectCommand;
import ilusr.textadventurecreator.toolbars.OpenCommand;
import ilusr.textadventurecreator.toolbars.RunToolBarCommand;
import ilusr.textadventurecreator.toolbars.SaveAsToolBarCommand;
import ilusr.textadventurecreator.toolbars.SaveToolBarCommand;
import ilusr.textadventurecreator.toolbars.TextAdventureToolBar;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ToolBarInitializer implements IInitialize {

	private final IToolBarService toolbarService;
	private final ProjectPersistenceManager persistenceManager;
	private final TextAdventureProvider provider;
	private final IDebugService debugService;
	private final TextAdventureProjectManager projectManager;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param toolbarService A @see IToolBarService to display toolbars.
	 * @param persistenceManager A @see ProjectPersistenceManager to mange the projects persistence.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param debugService A @see DebugService to debug the text adventure.
	 * @param projectManager A @see TextAdventureProjectManager to manage the text adventure.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public ToolBarInitializer(IToolBarService toolbarService,
							  ProjectPersistenceManager persistenceManager,
							  TextAdventureProvider provider,
							  IDebugService debugService,
							  TextAdventureProjectManager projectManager,
							  IDialogService dialogService,
							  ILanguageService languageService,
							  IStyleContainerService styleService,
							  InternalURLProvider urlProvider) {
		this.toolbarService = toolbarService;
		this.provider = provider;
		this.persistenceManager = persistenceManager;
		this.debugService = debugService;
		this.projectManager = projectManager;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public void initialize() {
		LogRunner.logger().log(Level.INFO, "Initializing the toolbar.");
		List<Node> commands = new ArrayList<Node>();
		commands.add(new NewProjectCommand(projectManager, languageService));
		commands.add(new OpenCommand(projectManager, languageService));
		commands.add(new Separator(Orientation.VERTICAL));
		commands.add(new SaveToolBarCommand(persistenceManager, provider, languageService));
		commands.add(new SaveAsToolBarCommand(persistenceManager, provider, languageService));
		commands.add(new Separator(Orientation.VERTICAL));
		commands.add(new RunToolBarCommand(provider, debugService, languageService));
		commands.add(new DebugSingleToolBarCommand(provider, debugService, dialogService, languageService, styleService, urlProvider));
		commands.add(new DebugToolBarCommand(provider, debugService, languageService));
		
		toolbarService.addToolBar(new TextAdventureToolBar(commands), LocationProvider.first(), DockPosition.Top, false);
	}

}
