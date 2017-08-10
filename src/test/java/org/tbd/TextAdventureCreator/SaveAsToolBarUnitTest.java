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
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.SaveAsToolBarCommand;

public class SaveAsToolBarUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ProjectPersistenceManager persistenceManager; 
	private TextAdventureProvider provider;
	private ILanguageService languageService;
	
	private SaveAsToolBarCommand command;
	
	@Before
	public void setup() {
		persistenceManager = mock(ProjectPersistenceManager.class);
		provider = mock(TextAdventureProvider.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SAVE_AS)).thenReturn("Save As");
		
		command = new SaveAsToolBarCommand(persistenceManager, provider, languageService);
	}
	
	@Test
	public void testTooltip() {
		assertEquals("Save As", command.getTooltip().getText());
	}

}
