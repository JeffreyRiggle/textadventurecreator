package ilusr.textadventurecreator.shell;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.RootBeanDefinition;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.main.IComponent;
import ilusr.iroshell.main.LoadData;
import ilusr.iroshell.menus.IMenuService;
import ilusr.iroshell.services.IApplicationClosingManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IToolBarService;
import ilusr.iroshell.services.LayoutService;
import ilusr.iroshell.services.RegisteredServices;
import ilusr.iroshell.statusbar.IStatusBarService;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.language.LanguageRegistration;
import ilusr.textadventurecreator.library.LibraryBluePrint;
import ilusr.textadventurecreator.library.LibraryItemBluePrint;
import ilusr.textadventurecreator.library.LibraryRegistration;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.mod.ModRegistration;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.ISettingsViewRepository;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.settings.SettingRegistration;
import ilusr.textadventurecreator.settings.SettingsManager;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.style.LocalThemeFinder;
import ilusr.textadventurecreator.style.ThemeRegistration;
import ilusr.textadventurecreator.style.ThemeService;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.GameExplorerBluePrint;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LandingPageBluePrint;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.ViewRegistration;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.gamestate.GameStateBluePrint;
import ilusr.textadventurecreator.views.layout.LayoutCreatorBluePrint;
import ilusr.textadventurecreator.views.layout.LayoutRegistration;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.player.PlayerBluePrint;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;

/**
 *
 * @author Jeff Riggle
 *
 */
public class TextAdventureCreatorComponent implements IComponent{

	private String initialFile;

	/**
	 *
	 * @param initialFile The initial file to load.
	 */
	public void setFile(String initialFile) {
		this.initialFile = initialFile;
	}

	@Override
	public void load(LoadData data) {
		try {
			ConstructorArgumentValues cav = new ConstructorArgumentValues();
			String settingsLocation = System.getProperty("user.home") + "//ilusr//settings.xml";
	        cav.addGenericArgumentValue(new XmlConfigurationManager(settingsLocation));
	        cav.addGenericArgumentValue(data.serviceManager().<IApplicationClosingManager>get(RegisteredServices.ApplicationClosingManager));
	        RootBeanDefinition settings = new RootBeanDefinition(SettingsManager.class, cav, null);
			data.serviceManager().registerBean("SettingsManager", settings);
			SettingRegistration.register(data.serviceManager());
			ShellRegistration.register(data.serviceManager());
			ViewRegistration.register(data.serviceManager());
			LibraryRegistration.register(data.serviceManager());
			LanguageRegistration.register(data.serviceManager());
			ModRegistration.register(data.serviceManager());
			LayoutRegistration.register(data.serviceManager());
			ThemeRegistration.register(data.serviceManager());
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}

		IMenuService menuService = data.serviceManager().<IMenuService>get(RegisteredServices.MenuService);
		LayoutService layoutService = data.serviceManager().<LayoutService>get(RegisteredServices.LayoutService);
		TextAdventureProjectManager manager = data.serviceManager().<TextAdventureProjectManager>get("TextAdventureProjectManager");
		TextAdventureProvider provider = data.serviceManager().<TextAdventureProvider>get("TextAdventureProvider");
		ProjectPersistenceManager persistenceManager = data.serviceManager().<ProjectPersistenceManager>get("ProjectPersistenceManager");
		IDialogService dialogService = data.serviceManager().<IDialogService>get(RegisteredServices.DialogService);
		IDialogProvider dialogProvider = data.serviceManager().<IDialogProvider>get("IDialogProvider");
		MacroBuilderViewFactory macroFactory = data.serviceManager().<MacroBuilderViewFactory>get("MacroBuilderViewFactory");
		MediaFinder mediaFinder = data.serviceManager().<MediaFinder>get("MediaFinder");
		IStatusBarService statusBarService = data.serviceManager().<IStatusBarService>get(RegisteredServices.StatusBarService);
		ProjectStatusService statusService = data.serviceManager().<ProjectStatusService>get("ProjectStatusService");
		IToolBarService toolbarService = data.serviceManager().<IToolBarService>get(RegisteredServices.ToolBarService);
		IDebugService debugService = data.serviceManager().<IDebugService>get("DebugService");
		ISettingsManager settingsManager = data.serviceManager().<ISettingsManager>get("SettingsManager");
		TriggerViewFactory triggerFactory = data.serviceManager().<TriggerViewFactory>get("TriggerViewFactory");
		ActionViewFactory actionFactory = data.serviceManager().<ActionViewFactory>get("ActionViewFactory");
		PlayerModProviderFactory playerModFactory = data.serviceManager().<PlayerModProviderFactory>get("PlayerModProviderFactory");
		LibraryService libraryService = data.serviceManager().<LibraryService>get("LibraryService");
		ILanguageService languageService = data.serviceManager().<ILanguageService>get("LanguageService");
		LayoutApplicationService layoutApplicationService = data.serviceManager().<LayoutApplicationService>get("LayoutApplicationService");
		ModManager modManager = data.serviceManager().<ModManager>get("ModManager");
		InternalURLProvider urlProvider = data.serviceManager().<InternalURLProvider>get("InternalURLProvider");
		ISettingsViewRepository repository = data.serviceManager().<ISettingsViewRepository>get("ISettingsViewRepository");
		ThemeService themeService = data.serviceManager().<ThemeService>get("ThemeService");
		IStyleContainerService styleService = data.serviceManager().<IStyleContainerService>get("IStyleContainerService");
		LocalThemeFinder themeFinder = data.serviceManager().<LocalThemeFinder>get("LocalThemeFinder");
		IReportIssueService reportIssueService = data.serviceManager().<IReportIssueService>get("SonaIssueReporter");

		layoutService.registerBluePrint(BluePrintNames.Landing, new LandingPageBluePrint());
		layoutService.registerBluePrint(BluePrintNames.Explorer, new GameExplorerBluePrint());
		layoutService.registerBluePrint(BluePrintNames.Player, new PlayerBluePrint(dialogService, libraryService, provider, languageService, dialogProvider, styleService, urlProvider));
		layoutService.registerBluePrint(BluePrintNames.GameState, new GameStateBluePrint(dialogService, libraryService, macroFactory, mediaFinder, provider, triggerFactory, playerModFactory, languageService, actionFactory, dialogProvider, styleService, urlProvider));
		layoutService.registerBluePrint(BluePrintNames.Library, new LibraryBluePrint());
		layoutService.registerBluePrint(BluePrintNames.LibraryItem, new LibraryItemBluePrint(dialogService, triggerFactory, actionFactory, playerModFactory, languageService, libraryService, urlProvider, dialogProvider, styleService));
		layoutService.registerBluePrint(BluePrintNames.LayoutCreator, new LayoutCreatorBluePrint(provider, languageService, dialogService, urlProvider, styleService));

		MenuInitializer menuInit = new MenuInitializer(menuService, layoutService, manager, provider,
				persistenceManager, debugService, dialogService, libraryService, languageService,
				layoutApplicationService, repository, styleService, urlProvider, dialogProvider, reportIssueService);

		StatusBarInitializer statusInit = new StatusBarInitializer(statusBarService, statusService);

		ToolBarInitializer toolInit = new ToolBarInitializer(toolbarService, persistenceManager, provider, debugService,
				manager, dialogService, languageService, styleService, urlProvider);

		menuInit.initialize();
		statusInit.initialize();
		toolInit.initialize();
		libraryService.initialize();
		themeService.initialize();
		themeFinder.initialize();

		if (initialFile == null && settingsManager.getBooleanSetting(SettingNames.LANDING, true)) {
			LogRunner.logger().info("Adding landing tab since file is null or landing settings is true");
			layoutService.addTab(BluePrintNames.Landing);
		}

		if (initialFile == null) {
			LogRunner.logger().info("Initial file is null not loading any project");
			modManager.initialize();
			repository.initialize();
			return;
		}

		try {
			LogRunner.logger().info(String.format("Initial file is %s loading project", initialFile));
			TextAdventureProjectPersistence project = persistenceManager.load(initialFile);
			provider.setTextAdventure(project);
			modManager.initialize();
			repository.initialize();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
}
