package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.LibrarySettingsModel;
import ilusr.textadventurecreator.settings.SettingNames;

public class LibrarySettingsModelUnitTest {

	private ISettingsManager manager;
	private ILanguageService languageService;
	
	private LibrarySettingsModel model;
	
	@Before
	public void setup() {
		manager = mock(ISettingsManager.class);
		when(manager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true)).thenReturn(true);
		when(manager.getBooleanSetting(SettingNames.GAME_LIBRARY, false)).thenReturn(false);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_SETTINGS)).thenReturn("Library Settings");
		when(languageService.getValue(DisplayStrings.USE_GLOBAL_LIBRARY)).thenReturn("Global Library");
		when(languageService.getValue(DisplayStrings.USE_INDIVIDUAL_LIBRARY)).thenReturn("Individual Library");
		
		model = new LibrarySettingsModel(manager, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
		assertTrue(model.globalLibrary().get());
		assertFalse(model.gameLibrary().get());
	}

	@Test
	public void testGlobalLibrary() {
		model.globalLibrary().set(false);
		assertFalse(model.globalLibrary().get());
		verify(manager, times(1)).setBooleanValue(SettingNames.GLOBAL_LIBRARY, false);
	}
	
	@Test
	public void testGameLibrary() {
		model.gameLibrary().set(true);
		assertTrue(model.gameLibrary().get());
		verify(manager, times(1)).setBooleanValue(SettingNames.GAME_LIBRARY, true);
	}
	
	@Test
	public void testSettingText() {
		assertEquals(model.settingText().get(), "Library Settings");
	}
	
	@Test
	public void testGlobalText() {
		assertEquals(model.globalText().get(), "Global Library");
	}
	
	@Test
	public void testIndividualText() {
		assertEquals(model.individualText().get(), "Individual Library");
	}
}
