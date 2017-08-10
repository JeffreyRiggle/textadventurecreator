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
import ilusr.textadventurecreator.menus.DebugMenuItem;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.event.ActionEvent;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class DebugMenuUnitTest {
	
	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider;
    private TextAdventurePersistenceObject textAdventure;
	private IDebugService service;
    private ILanguageService languageService;
	
    private DebugMenuItem item;
    
	@Before
	public void setup() {
		textAdventure = mock(TextAdventurePersistenceObject.class);
		TextAdventureProjectPersistence project = mock(TextAdventureProjectPersistence.class);
		when(project.getTextAdventure()).thenReturn(textAdventure);
		
		provider = mock(TextAdventureProvider.class);
		when(provider.getTextAdventureProject()).thenReturn(project);
		
		service = mock(IDebugService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.DEBUG)).thenReturn("Debug");
		
		item = new DebugMenuItem(provider, service, languageService);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(service, times(1)).debugGame(textAdventure);
	}

	@Test
	public void testText() {
		assertEquals("Debug", item.textProperty().get());
	}
}
