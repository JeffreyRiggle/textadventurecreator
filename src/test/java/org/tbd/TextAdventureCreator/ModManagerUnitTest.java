package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.iroshell.core.ApplicationClosingListener;
import ilusr.iroshell.services.IApplicationClosingManager;
import ilusr.textadventurecreator.mod.IMod;
import ilusr.textadventurecreator.mod.ModLoader;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;

public class ModManagerUnitTest {

	private IMod mod1;
	private IMod mod2;
	private IMod mod3;
	
	private ModLoader loader;
	private ISettingsManager settingsManager;
	private IApplicationClosingManager closingManager;
	
	private ModManager manager;
	
	@Before
	public void setup() {
		mod1 = mock(IMod.class);
		when(mod1.id()).thenReturn("Mod1");
		mod2 = mock(IMod.class);
		when(mod2.id()).thenReturn("Mod2");
		mod3 = mock(IMod.class);
		when(mod3.id()).thenReturn("Mod3");
		
		loader = mock(ModLoader.class);
		when(loader.getMods()).thenReturn(Arrays.asList(mod1, mod2, mod3));
		
		settingsManager = mock(ISettingsManager.class);
		closingManager = mock(IApplicationClosingManager.class);
		
		manager = new ModManager(loader, settingsManager, closingManager);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(manager);
	}

	@Test
	public void testInitializeWithoutAllowMods() {
		when(settingsManager.getBooleanSetting(mod1.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod2.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod3.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(false);
		manager.initialize();
		
		verify(loader, times(0)).initialize();
		verify(mod1, times(0)).load(any());
		assertFalse(manager.getActivation(mod1.id()));
		verify(mod2, times(0)).load(any());
		assertFalse(manager.getActivation(mod2.id()));
		verify(mod3, times(0)).load(any());
		assertFalse(manager.getActivation(mod3.id()));
	}
	
	@Test
	public void testInitialize() {
		when(settingsManager.getBooleanSetting(mod1.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod2.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod3.id() + "Active", true)).thenReturn(false);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(true);
		manager.initialize();
		
		verify(loader, times(1)).initialize();
		verify(mod1, times(1)).load(any());
		assertTrue(manager.getActivation(mod1.id()));
		verify(mod2, times(1)).load(any());
		assertTrue(manager.getActivation(mod2.id()));
		verify(mod3, times(0)).load(any());
		assertFalse(manager.getActivation(mod3.id()));
	}
	
	@Test
	public void testActivateMod() {
		when(settingsManager.getBooleanSetting(mod1.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod2.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod3.id() + "Active", true)).thenReturn(false);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(true);
		manager.initialize();
		
		manager.activateMod(mod3.id());
		verify(mod3, times(1)).load(any());
		assertTrue(manager.getActivation(mod3.id()));
	}
	
	@Test
	public void testDeactivateMod() {
		when(settingsManager.getBooleanSetting(mod1.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod2.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod3.id() + "Active", true)).thenReturn(false);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(true);
		manager.initialize();
		
		manager.deactivateMod(mod2.id());
		verify(mod2, times(1)).unload();
		assertFalse(manager.getActivation(mod2.id()));
	}
	
	@Test
	public void testApplicationClosing() {
		ArgumentCaptor<ApplicationClosingListener> captor = ArgumentCaptor.forClass(ApplicationClosingListener.class);
		
		when(settingsManager.getBooleanSetting(mod1.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod2.id() + "Active", true)).thenReturn(true);
		when(settingsManager.getBooleanSetting(mod3.id() + "Active", true)).thenReturn(false);
		when(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)).thenReturn(true);
		manager.initialize();
		
		verify(closingManager, times(1)).addApplicationClosingListener(captor.capture());
		captor.getValue().onClose();
		
		verify(mod1, times(1)).unload();
		verify(mod2, times(1)).unload();
		verify(mod3, times(0)).unload();
	}
}
