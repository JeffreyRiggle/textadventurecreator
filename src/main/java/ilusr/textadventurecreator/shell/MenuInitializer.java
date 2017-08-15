package ilusr.textadventurecreator.shell;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.LocationProvider;
import ilusr.iroshell.menus.IMenuService;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.menus.AboutMenuItem;
import ilusr.textadventurecreator.menus.DebugMenuItem;
import ilusr.textadventurecreator.menus.DebugSingleMenuItem;
import ilusr.textadventurecreator.menus.ExplorerMenuItem;
import ilusr.textadventurecreator.menus.ExportMenuItem;
import ilusr.textadventurecreator.menus.ImportMenuItem;
import ilusr.textadventurecreator.menus.LibrariesMenuItem;
import ilusr.textadventurecreator.menus.NewProjectMenuItem;
import ilusr.textadventurecreator.menus.OpenMenuItem;
import ilusr.textadventurecreator.menus.ProduceMenuItem;
import ilusr.textadventurecreator.menus.ReportIssueMenuItem;
import ilusr.textadventurecreator.menus.RunGameMenuItem;
import ilusr.textadventurecreator.menus.SaveAsMenuItem;
import ilusr.textadventurecreator.menus.SaveMenuItem;
import ilusr.textadventurecreator.menus.SettingsMenuItem;
import ilusr.textadventurecreator.settings.ISettingsViewRepository;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MenuInitializer implements IInitialize {

	private final IMenuService menuService;
	private final ILayoutService layoutService;
	private final TextAdventureProjectManager projectManager;
	private final TextAdventureProvider provider;
	private final ProjectPersistenceManager persistenceManager;
	private final IDebugService debugService;
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final LayoutApplicationService layoutApplicationService;
	private final ISettingsViewRepository repository;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private final IDialogProvider dialogProvider;
	private final IReportIssueService reportIssueService;
	
	private Menu view;
	private Menu options;
	private Menu run;
	private Menu help;
	
	/**
	 * 
	 * @param menuService A @see IMenuService to display menu items.
	 * @param layoutService A @see LayoutService to display tab.s
	 * @param projectManager A @see TextAdventureProjectManager to manage projects.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param persistenceManager A @see ProjectPersistenceManager to save project data.
	 * @param debugService A @see DebugService to debug games.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param layoutApplicationService A @see LayoutApplicationService to notify about layout changes.
	 * @param modManager A @see ModManager to manage mods.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 * @param dialogProvider provides dialogs.
	 * @param reportIssueService reports issues.
	 */
	public MenuInitializer(IMenuService menuService, 
						   ILayoutService layoutService, 
						   TextAdventureProjectManager projectManager, 
						   TextAdventureProvider provider,
						   ProjectPersistenceManager persistenceManager,
						   IDebugService debugService,
						   IDialogService dialogService,
						   LibraryService libraryService,
						   ILanguageService languageService,
						   LayoutApplicationService layoutApplicationService,
						   ISettingsViewRepository repository, 
						   IStyleContainerService styleService,
						   InternalURLProvider urlProvider,
						   IDialogProvider dialogProvider,
						   IReportIssueService reportIssueService) {
		this.menuService = menuService;
		this.layoutService = layoutService;
		this.projectManager = projectManager;
		this.provider = provider;
		this.persistenceManager = persistenceManager;
		this.debugService = debugService;
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.layoutApplicationService = layoutApplicationService;
		this.repository = repository;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		this.dialogProvider = dialogProvider;
		this.reportIssueService = reportIssueService;
	}
	
	@Override
	public void initialize() {
		LogRunner.logger().info("Initializing menu items.");
		MenuItem newProj = new NewProjectMenuItem(projectManager, languageService);
		
		MenuItem open = new OpenMenuItem(projectManager, languageService);
		MenuItem save = new SaveMenuItem(persistenceManager, provider, languageService);
		MenuItem saveAs = new SaveAsMenuItem(persistenceManager, provider, languageService);
		MenuItem produce = new ProduceMenuItem(provider, projectManager, languageService);
		MenuItem export = new ExportMenuItem(libraryService, dialogService, languageService, styleService, urlProvider);
		MenuItem imp = new ImportMenuItem(libraryService, languageService);
		
		view = new Menu(languageService.getValue(DisplayStrings.VIEW));
		MenuItem libraries = new LibrariesMenuItem(layoutService, languageService);
		MenuItem explorer = new ExplorerMenuItem(provider, layoutService, languageService, layoutApplicationService);
		
		view.getItems().add(libraries);
		view.getItems().add(explorer);
		
		options = new Menu(languageService.getValue(DisplayStrings.OPTIONS));
		MenuItem settings = new SettingsMenuItem(languageService, repository, dialogService, styleService, urlProvider);
		options.getItems().add(settings);
		
		run = new Menu(languageService.getValue(DisplayStrings.RUN));
		MenuItem current = new DebugSingleMenuItem(provider, debugService, dialogService, languageService, styleService, urlProvider);
		MenuItem debug = new DebugMenuItem(provider, debugService, languageService);
		MenuItem normalRun = new RunGameMenuItem(provider, debugService, languageService);
		run.getItems().add(normalRun);
		run.getItems().add(current);
		run.getItems().add(debug);
		
		help = new Menu(languageService.getValue(DisplayStrings.HELP));
		MenuItem about = new AboutMenuItem(languageService, dialogService, styleService, urlProvider);
		MenuItem reportIssue = new ReportIssueMenuItem(languageService, dialogService, styleService, urlProvider, dialogProvider, reportIssueService);
		help.getItems().add(about);
		help.getItems().add(reportIssue);
		
		menuService.addMenuItem(imp, "File", LocationProvider.first());
		menuService.addMenuItem(export, "File", LocationProvider.first());
		menuService.addMenuItem(produce, "File", LocationProvider.first());
		menuService.addMenuItem(saveAs, "File", LocationProvider.first());
		menuService.addMenuItem(save, "File", LocationProvider.first());
		menuService.addMenuItem(open, "File", LocationProvider.first());
		menuService.addMenuItem(newProj, "File", LocationProvider.first());
		
		menuService.addMenu(view, LocationProvider.last());
		menuService.addMenu(options, LocationProvider.last());
		menuService.addMenu(run, LocationProvider.last());
		menuService.addMenu(help, LocationProvider.last());
		
		languageService.addListener(() -> {
			view.setText(languageService.getValue(DisplayStrings.VIEW));
			options.setText(languageService.getValue(DisplayStrings.OPTIONS));
			run.setText(languageService.getValue(DisplayStrings.RUN));
			help.setText(languageService.getValue(DisplayStrings.HELP));
		});
	}
}
