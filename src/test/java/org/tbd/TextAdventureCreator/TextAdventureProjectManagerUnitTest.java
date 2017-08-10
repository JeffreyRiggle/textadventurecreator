package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.StyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.shell.LayoutApplicationService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.Wizard;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;

public class TextAdventureProjectManagerUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private IDialogService dialogService;
	private MediaFinder mediaFinder;
	private ProjectPersistenceManager persistenceManager;
	private ProjectStatusService statusService;
	private ILanguageService languageService;
	private ILayoutService layoutService;
	private ISettingsManager settingsManager;
	private LayoutApplicationService layoutApplicationService;
	private StyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private TextAdventureProjectManager manager;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		dialogService = mock(IDialogService.class);
		mediaFinder = mock(MediaFinder.class);
		persistenceManager = mock(ProjectPersistenceManager.class);
		statusService = mock(ProjectStatusService.class);
		layoutService = mock(ILayoutService.class);
		languageService = mock(ILanguageService.class);
		settingsManager = mock(ISettingsManager.class);
		layoutApplicationService = mock(LayoutApplicationService.class);
		styleService = mock(StyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		manager = new TextAdventureProjectManager(provider, dialogService, mediaFinder, persistenceManager, statusService, languageService, layoutService, settingsManager, layoutApplicationService, styleService, urlProvider);
	}
	
	@Test
	public void testCreateNewProject() {
		try {
			manager.createNewProject();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		verify(dialogService, times(1)).displayModal(any(Wizard.class));
	}
}
