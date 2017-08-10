package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.DebugSingleToolBarCommand;
import ilusr.textadventurecreator.views.ItemSelector;
import javafx.event.ActionEvent;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class DebugSingleToolBarUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private IDebugService debugService;
	private IDialogService dialogService;
	private ILanguageService languageService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private DebugSingleToolBarCommand debug;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		TextAdventurePersistenceObject tav = mock(TextAdventurePersistenceObject.class);
		when(tav.gameStates()).thenReturn(Arrays.asList(mock(GameStatePersistenceObject.class)));
		when(proj.getTextAdventure()).thenReturn(tav);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		
		debugService = mock(IDebugService.class);
		dialogService = mock(IDialogService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.DEBUG_SINGLE)).thenReturn("Debug Single");
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		debug = new DebugSingleToolBarCommand(provider, debugService, dialogService, languageService, styleService, urlProvider);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Debug Single", debug.getTooltip().getText());
	}
	
	@Test
	public void testAction() {
		debug.getOnAction().handle(mock(ActionEvent.class));
		verify(dialogService, times(1)).displayModal(any(ItemSelector.class));
	}
}
