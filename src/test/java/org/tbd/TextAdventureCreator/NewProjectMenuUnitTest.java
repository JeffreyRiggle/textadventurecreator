package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.NewProjectMenuItem;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.event.ActionEvent;

public class NewProjectMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProjectManager projectManager;
	private ILanguageService languageService;
	
	private NewProjectMenuItem item;
	
	@Before
	public void setup() {
		projectManager = mock(TextAdventureProjectManager.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.NEW)).thenReturn("New");
		
		item = new NewProjectMenuItem(projectManager, languageService);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		try {
			verify(projectManager, times(1)).createNewProject();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDisplay() {
		assertEquals("New", item.textProperty().get());
	}
}
