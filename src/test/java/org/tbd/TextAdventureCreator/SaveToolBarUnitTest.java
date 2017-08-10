package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.SaveToolBarCommand;
import javafx.event.ActionEvent;

public class SaveToolBarUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ProjectPersistenceManager persistenceManager; 
	private TextAdventureProvider provider;
	private ILanguageService languageService;
	
	private TextAdventureProjectPersistence proj;
	
	private SaveToolBarCommand command;
	
	@Before
	public void setup() {
		persistenceManager = mock(ProjectPersistenceManager.class);
		provider = mock(TextAdventureProvider.class);
		proj = mock(TextAdventureProjectPersistence.class);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SAVE)).thenReturn("Save");
		
		command = new SaveToolBarCommand(persistenceManager, provider, languageService);
	}
	
	@Test
	public void testTooltip() {
		assertEquals("Save", command.getTooltip().getText());
	}

	@Test
	public void testAction() {
		command.getOnAction().handle(mock(ActionEvent.class));
		verify(persistenceManager, times(1)).saveAsync(proj);
	}
}
