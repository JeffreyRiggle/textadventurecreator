package org.tbd.TextAdventureCreator;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.DockPosition;
import ilusr.iroshell.core.LocationParameters;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IToolBarService;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.shell.ToolBarInitializer;
import ilusr.textadventurecreator.toolbars.TextAdventureToolBar;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;

public class ToolBarInitializerUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IToolBarService toolbarService;
	private ProjectPersistenceManager persistenceManager;
	private TextAdventureProvider provider;
	private IDebugService debugService;
	private TextAdventureProjectManager projectManager;
	private IDialogService dialogService;
	private ILanguageService languageService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private ToolBarInitializer toolbar;
	
	@Before
	public void setup() {
		toolbarService = mock(IToolBarService.class);
		persistenceManager = mock(ProjectPersistenceManager.class);
		provider = mock(TextAdventureProvider.class);
		debugService = mock(IDebugService.class);
		projectManager = mock(TextAdventureProjectManager.class);
		dialogService = mock(IDialogService.class);
		languageService = mock(ILanguageService.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		toolbar = new ToolBarInitializer(toolbarService, persistenceManager, provider,
				debugService, projectManager, dialogService, languageService, styleService, urlProvider);
	}
	
	@Test
	public void testInitialize() {
		toolbar.initialize();
		
		verify(toolbarService, times(1)).addToolBar(any(TextAdventureToolBar.class), any(LocationParameters.class), eq(DockPosition.Top), eq(false));
	}
}
