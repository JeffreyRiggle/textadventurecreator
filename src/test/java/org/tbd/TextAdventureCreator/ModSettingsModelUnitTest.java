package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.LanguageService;
import ilusr.textadventurecreator.mod.IMod;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.ModSettingsModel;
import ilusr.textadventurecreator.settings.SettingNames;

public class ModSettingsModelUnitTest {

	private ISettingsManager settingsManager;
	private ModManager modManager;
	private LanguageService languageService;
	
	private IMod mod1;
	private IMod mod2;
	private IMod mod3;
	
	private ModSettingsModel model;
	
	@Before
	public void setup() {
		settingsManager = mock(ISettingsManager.class);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(true);
		
		modManager = mock(ModManager.class);
		mod1 = mock(IMod.class);
		when(mod1.name()).thenReturn("Mod1");
		mod2 = mock(IMod.class);
		when(mod1.name()).thenReturn("Mod2");
		mod3 = mock(IMod.class);
		when(mod1.name()).thenReturn("Mod3");
		when(modManager.mods()).thenReturn(Arrays.asList(mod1, mod2, mod3));
		
		languageService = mock(LanguageService.class);
		when(languageService.getValue(DisplayStrings.MOD_SETTING)).thenReturn("Mod Settings");
		when(languageService.getValue(DisplayStrings.ENABLE_MODS)).thenReturn("Enable Mods");
		when(languageService.getValue(DisplayStrings.MODS)).thenReturn("Mods");
		
		model = new ModSettingsModel(settingsManager, modManager, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
		assertTrue(model.enableMods().get());
		assertTrue(model.mods().stream().filter(m -> m.name() == mod1.name()).findFirst().isPresent());
		assertTrue(model.mods().stream().filter(m -> m.name() == mod2.name()).findFirst().isPresent());
		assertTrue(model.mods().stream().filter(m -> m.name() == mod3.name()).findFirst().isPresent());
	}
	
	@Test
	public void testSettingText() {
		assertEquals("Mod Settings", model.settingText().get());
	}

	@Test
	public void testEnableModText() {
		assertEquals("Enable Mods", model.enableModText().get());
	}
	
	@Test
	public void testModsText() {
		assertEquals("Mods", model.modsText().get());
	}
	
	@Test
	public void testEnableMods() {
		model.enableMods().set(false);
		assertFalse(model.enableMods().get());
		verify(settingsManager, times(1)).setBooleanValue(SettingNames.ALLOW_MODS, false);
	}
}
