package org.tbd.TextAdventureCreator;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.LocationParameters;
import ilusr.iroshell.menus.IMenuService;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.settings.ISettingsViewRepository;
import ilusr.textadventurecreator.shell.LayoutApplicationService;
import ilusr.textadventurecreator.shell.MenuInitializer;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuInitializerUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IMenuService menuService; 
	private ILayoutService layoutService; 
	private TextAdventureProjectManager projectManager; 
	private TextAdventureProvider provider;
	private ProjectPersistenceManager persistenceManager;
	private IDebugService debugService;
	private IDialogService dialogService;
	private LibraryService libraryService;
	private ILanguageService languageService;
	private LayoutApplicationService layoutApplicationService;
	private ISettingsViewRepository repository;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	private IDialogProvider dialogProvider;
	private IReportIssueService reportIssueService;
	
	private MenuInitializer menu;
	
	@Before
	public void setup() {
		menuService = mock(IMenuService.class);
		layoutService = mock(ILayoutService.class);
		projectManager = mock(TextAdventureProjectManager.class);
		provider = mock(TextAdventureProvider.class);
		persistenceManager = mock(ProjectPersistenceManager.class);
		debugService = mock(IDebugService.class);
		dialogService = mock(IDialogService.class);
		libraryService = mock(LibraryService.class);
		languageService = mock(ILanguageService.class);
		layoutApplicationService = mock(LayoutApplicationService.class);
		repository = mock(ISettingsViewRepository.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		dialogProvider = mock(IDialogProvider.class);
		reportIssueService = mock(IReportIssueService.class);
		
		menu = new MenuInitializer(menuService, layoutService, projectManager, provider,
				persistenceManager, debugService, dialogService, libraryService, languageService,
				layoutApplicationService, repository, styleService, urlProvider,
				dialogProvider, reportIssueService);
	}
	
	@Test
	public void testInitialize() {
		menu.initialize();
		verify(menuService, times(7)).addMenuItem(any(MenuItem.class), eq("File"), any(LocationParameters.class));
		verify(menuService, times(4)).addMenu(any(Menu.class), any(LocationParameters.class));
	}

}
