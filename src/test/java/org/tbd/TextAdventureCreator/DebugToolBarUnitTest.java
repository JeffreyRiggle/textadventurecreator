package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.DebugToolBarCommand;
import javafx.event.ActionEvent;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class DebugToolBarUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private IDebugService debugService;
	private ILanguageService languageService;
	private TextAdventurePersistenceObject tav;
	
	private DebugToolBarCommand debug;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		tav = mock(TextAdventurePersistenceObject.class);
		when(tav.gameStates()).thenReturn(Arrays.asList(mock(GameStatePersistenceObject.class)));
		when(proj.getTextAdventure()).thenReturn(tav);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		
		debugService = mock(IDebugService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.DEBUG)).thenReturn("Debug");
		
		debug = new DebugToolBarCommand(provider, debugService, languageService);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Debug", debug.getTooltip().getText());
	}

	@Test
	public void testAction() {
		debug.getOnAction().handle(mock(ActionEvent.class));
		verify(debugService, times(1)).debugGame(tav);
	}
}
