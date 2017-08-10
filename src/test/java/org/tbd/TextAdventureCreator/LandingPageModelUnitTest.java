package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.views.LandingPageModel;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.stage.Window;

public class LandingPageModelUnitTest {

	private TextAdventureProjectManager projectManager; 
	private ISettingsManager settingsManager;
	private ILanguageService languageService;
	
	private LandingPageModel model;
	
	@Before
	public void setup() {
		projectManager = mock(TextAdventureProjectManager.class);
		settingsManager = mock(ISettingsManager.class);
		when(settingsManager.getBooleanSetting(SettingNames.LANDING, true)).thenReturn(true);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.CREATE_NEW_PROJECT)).thenReturn("Create new project");
		when(languageService.getValue(DisplayStrings.LOAD_EXISTING_PROJECT)).thenReturn("Load existing project");
		when(languageService.getValue(DisplayStrings.TUTORIAL)).thenReturn("Tutorial");
		when(languageService.getValue(DisplayStrings.FIND_OUT_MORE)).thenReturn("Find out more");
		when(languageService.getValue(DisplayStrings.DONT_SHOW_AGAIN)).thenReturn("Dont show again");
		when(languageService.getValue(DisplayStrings.TEXTADVENTURECREATOR)).thenReturn("Text Adventure Creator");
		when(languageService.getValue(DisplayStrings.CREATOR_TAG_LINE)).thenReturn("Tag line");
		
		model = new LandingPageModel(projectManager, settingsManager, languageService);
	}
	
	@Test
	public void testCreateProject() {
		model.createNewProject();
		try {
			verify(projectManager, times(1)).createNewProject();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testOpenProject() {
		Window window = mock(Window.class);
		model.openProject(window);
		verify(projectManager, times(1)).openProject(window);
	}
	
	@Test
	public void testHideLanding() {
		assertFalse(model.hideLanding().get());
		model.hideLanding().set(true);
		assertTrue(model.hideLanding().get());
		verify(settingsManager, times(1)).setBooleanValue(SettingNames.LANDING, false);
	}
	
	@Test
	public void testNewProjectText() {
		assertEquals("Create new project", model.newProjectText().get());
	}
	
	@Test
	public void testLoadProjectText() {
		assertEquals("Load existing project", model.loadProjectText().get());
	}
	
	@Test
	public void testTutorialText() {
		assertEquals("Tutorial", model.tutorialText().get());
	}
	
	@Test
	public void testFindMoreText() {
		assertEquals("Find out more", model.findMoreText().get());
	}
	
	@Test
	public void testDontShowText() {
		assertEquals("Dont show again", model.dontShowText().get());
	}
	
	@Test
	public void testTitleText() {
		assertEquals("Text Adventure Creator", model.titleText().get());
	}
	
	@Test
	public void testTagLineText() {
		assertEquals("Tag line", model.tagLineText().get());
	}
}
