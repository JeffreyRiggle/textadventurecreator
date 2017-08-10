package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.style.ThemeService;
import ilusr.textadventurecreator.style.ThemeSettingsModel;

public class ThemeSettingsModelUnitTest {

	private ILanguageService languageService; 
	private ThemeService themeService;
	private ISettingsManager settingsManager;
	
	private ThemeSettingsModel model;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.THEME_SETTINGS)).thenReturn("Theme Settings");
		when(languageService.getValue(DisplayStrings.THEMES)).thenReturn("Themes");
		when(languageService.getValue(DisplayStrings.APPLY)).thenReturn("Apply");
		
		themeService = mock(ThemeService.class);
		when(themeService.getRegisteredThemes()).thenReturn(Arrays.asList("Classic", "Dark", "Test"));
		settingsManager = mock(ISettingsManager.class);
		when(settingsManager.getStringSetting(SettingNames.SELECTED_THEME, "Classic")).thenReturn("Dark");
		
		model = new ThemeSettingsModel(languageService, themeService, settingsManager);
	}
	
	@Test
	public void testApply() {
		model.apply();
		verify(themeService, times(1)).changeTheme("Dark");
		verify(settingsManager, times(1)).setStringSetting(SettingNames.SELECTED_THEME, "Dark");
	}

	@Test
	public void testSelectedTheme() {
		assertEquals(3, model.selectedTheme().list().size());
		assertEquals("Dark", model.selectedTheme().selected().get());
	}
	
	@Test
	public void testThemeSettingsText() {
		assertEquals("Theme Settings", model.themeSettingsText().get());
	}
	
	@Test
	public void testSelectedText() {
		assertEquals("Themes", model.selectedText().get());
	}
	
	@Test
	public void testApplyText() {
		assertEquals("Apply", model.applyText().get());
	}
}
