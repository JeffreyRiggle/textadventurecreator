package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.RegistrationType;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.style.IThemeListener;
import ilusr.textadventurecreator.style.Theme;
import ilusr.textadventurecreator.style.ThemeService;

public class ThemeServiceUnitTest {

	private IStyleContainerService styleService;
	private ISettingsManager settingManager;
	
	private ThemeService service;
	
	@Before
	public void setup() {
		styleService = mock(IStyleContainerService.class);
		settingManager = mock(ISettingsManager.class);
		
		service = new ThemeService(styleService, settingManager);
	}
	
	@Test
	public void testInitializedWithNoTheme() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		try {
			verify(styleService, times(0)).register(any(), any(File.class), any());
			verify(styleService, times(0)).register(any(), any(String.class), any());
		} catch (IllegalArgumentException | IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInitializeWithTheme() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn("Dark");
		service.initialize();
		try {
			verify(styleService, atLeast(1)).register(any(), any(File.class), any());
			verify(styleService, times(0)).register(any(), any(String.class), any());
		} catch (IllegalArgumentException | IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInitializeWithDeferredTheme() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn("Test");
		service.initialize();
		try {
			verify(styleService, times(0)).register(any(), any(File.class), any());
			verify(styleService, times(0)).register(any(), any(String.class), any());
		} catch (IllegalArgumentException | IOException e) {
			fail(e.getMessage());
		}
		
		Theme t = mock(Theme.class);
		when(t.getName()).thenReturn("Test");
		Map<String, File> styleMap = new HashMap<>();
		File f = mock(File.class);
		styleMap.put("TestStyle", f);
		when(t.styles()).thenReturn(styleMap);
		service.addTheme(t);
		
		try {
			verify(styleService, times(1)).register("TestStyle", f, RegistrationType.Override);
			verify(styleService, times(0)).register(any(), any(String.class), any());
		} catch (IllegalArgumentException | IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddTheme() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		Theme t = mock(Theme.class);
		when(t.getName()).thenReturn("Test");
		when(t.styles()).thenReturn(new HashMap<>());
		
		service.addTheme(t);
		assertTrue(service.getRegisteredThemes().contains("Test"));
	}
	
	@Test
	public void testRemoveTheme() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		Theme t = mock(Theme.class);
		when(t.getName()).thenReturn("Test");
		when(t.styles()).thenReturn(new HashMap<>());
		
		service.addTheme(t);
		service.removeTheme(t);
		assertFalse(service.getRegisteredThemes().contains("Test"));
	}
	
	@Test
	public void testAddListener() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		Theme t = mock(Theme.class);
		when(t.getName()).thenReturn("Test");
		when(t.styles()).thenReturn(new HashMap<>());
		
		TListener listener = new TListener();
		service.addTheme(t);
		service.addListener(listener);
		service.changeTheme("Test");
		assertTrue(listener.ran());
	}
	
	@Test
	public void testRemoveListener() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		Theme t = mock(Theme.class);
		when(t.getName()).thenReturn("Test");
		when(t.styles()).thenReturn(new HashMap<>());
		
		TListener listener = new TListener();
		service.addTheme(t);
		service.addListener(listener);
		service.removeListener(listener);
		service.changeTheme("Test");
		assertFalse(listener.ran());
	}
	
	@Test
	public void testRegisteredThemes() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		assertEquals(1, service.getRegisteredThemes().size());
	}
	
	@Test
	public void testUpdated() {
		when(settingManager.getStringSetting(SettingNames.SELECTED_THEME, null)).thenReturn(null);
		service.initialize();
		
		File f = mock(File.class);
		service.updated("TestStyle", f);
		try {
			verify(styleService, times(1)).register("TestStyle", f, RegistrationType.Override);
			verify(styleService, times(0)).register(any(), any(String.class), any());
		} catch (IllegalArgumentException | IOException e) {
			fail(e.getMessage());
		}
	}
	
	private class TListener implements IThemeListener {

		private boolean ran = false;
		
		@Override
		public void themeChanged(String theme) {
			ran = true;
		}
		
		public boolean ran() {
			return ran;
		}
	}
}
