package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.RunGameMenuItem;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.event.ActionEvent;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class RunGameMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private IDebugService service;
	private ILanguageService languageService;
	private TextAdventurePersistenceObject game;
	
	private RunGameMenuItem item;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		game = mock(TextAdventurePersistenceObject.class);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		when(proj.getTextAdventure()).thenReturn(game);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		
		service = mock(IDebugService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.RUN)).thenReturn("Run");
		
		item = new RunGameMenuItem(provider, service, languageService);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(service, times(1)).runGame(game);
	}

	@Test
	public void testDisplay() {
		assertEquals("Run", item.textProperty().get());
	}
}
