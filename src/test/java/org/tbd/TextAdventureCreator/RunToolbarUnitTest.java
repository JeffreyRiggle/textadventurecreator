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
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.RunToolBarCommand;
import javafx.event.ActionEvent;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class RunToolbarUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider;
	private IDebugService service;
	private ILanguageService languageService;
	
	private TextAdventurePersistenceObject tav;
	
	private RunToolBarCommand command;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		tav = mock(TextAdventurePersistenceObject.class);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		when(proj.getTextAdventure()).thenReturn(tav);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		
		service = mock(IDebugService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.RUN)).thenReturn("Run");
		
		command = new RunToolBarCommand(provider, service, languageService);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Run", command.getTooltip().getText());
	}

	@Test
	public void testAction() {
		command.getOnAction().handle(mock(ActionEvent.class));
		verify(service, times(1)).runGame(tav);
	}
}
