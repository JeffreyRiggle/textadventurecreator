package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.SaveMenuItem;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.event.ActionEvent;

public class SaveMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ProjectPersistenceManager persistenceManager; 
	private TextAdventureProvider provider;
	private ILanguageService languageService;
	
	private TextAdventureProjectPersistence project;
	
	private SaveMenuItem item;
	
	@Before
	public void setup() {
		persistenceManager = mock(ProjectPersistenceManager.class);
		provider = mock(TextAdventureProvider.class);
		project = mock(TextAdventureProjectPersistence.class);
		when(provider.getTextAdventureProject()).thenReturn(project);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SAVE)).thenReturn("Save");
		
		item = new SaveMenuItem(persistenceManager, provider, languageService);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(persistenceManager, times(1)).saveAsync(project);
	}

	@Test
	public void testDisplay() {
		assertEquals("Save", item.textProperty().get());
	}
}
