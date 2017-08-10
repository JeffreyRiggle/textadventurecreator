package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.toolbars.OpenCommand;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;

public class OpenCommandUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProjectManager projectManager;
	private ILanguageService languageService;
	
	private OpenCommand command;
	
	@Before
	public void setup() {
		projectManager = mock(TextAdventureProjectManager.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.OPEN)).thenReturn("Open");
		
		command = new OpenCommand(projectManager, languageService);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Open", command.getTooltip().getText());
	}
}
