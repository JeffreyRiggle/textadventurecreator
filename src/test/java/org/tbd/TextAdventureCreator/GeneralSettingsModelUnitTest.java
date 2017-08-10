package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.GeneralSettingsModel;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;

public class GeneralSettingsModelUnitTest {

	private ISettingsManager manager;
	private ILanguageService languageService;
	
	private GeneralSettingsModel model;
	
	@Before
	public void setup() {
		manager = mock(ISettingsManager.class);
		when(manager.getBooleanSetting(SettingNames.LANDING, true)).thenReturn(false);
		when(manager.getBooleanSetting(SettingNames.PERSIST_LAYOUT, true)).thenReturn(true);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SHOW_GREETINGS)).thenReturn("Show Greetings");
		when(languageService.getValue(DisplayStrings.GENERAL_SETTINGS)).thenReturn("General Settings");
		when(languageService.getValue(DisplayStrings.PERSIST_LAYOUT)).thenReturn("Persist Layout");
		
		model = new GeneralSettingsModel(manager, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
		assertFalse(model.hideLanding().get());
		assertTrue(model.includeLayout().get());
	}

	@Test
	public void testHideLanding() {
		model.hideLanding().set(true);
		verify(manager, times(1)).setBooleanValue(SettingNames.LANDING, true);
	}
	
	@Test
	public void testIncludeLayout() {
		model.includeLayout().set(false);
		verify(manager, times(1)).setBooleanValue(SettingNames.PERSIST_LAYOUT, false);
	}
	
	@Test
	public void testShowGrettingText() {
		assertEquals("Show Greetings", model.showGreetingText().get());
	}
	
	@Test
	public void testLayoutText() {
		assertEquals("Persist Layout", model.layoutText().get());
	}
	
	@Test
	public void testSettingText() {
		assertEquals("General Settings", model.settingText().get());
	}
}
