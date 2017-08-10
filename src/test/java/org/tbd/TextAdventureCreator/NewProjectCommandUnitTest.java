package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.toolbars.NewProjectCommand;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.event.ActionEvent;

public class NewProjectCommandUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProjectManager projectManager;
	private ILanguageService languageService;
	
	private NewProjectCommand command;
	
	@Before
	public void setup() {
		projectManager = mock(TextAdventureProjectManager.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.NEW_PROJECT)).thenReturn("New Project");
		
		command = new NewProjectCommand(projectManager, languageService);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("New Project", command.getTooltip().getText());
	}

	@Test
	public void testAction() {
		command.getOnAction().handle(mock(ActionEvent.class));
		try {
			verify(projectManager, times(1)).createNewProject();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
