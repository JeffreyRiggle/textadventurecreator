package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.ModSetting;

public class ModSettingUnitTest {

	private ISettingsManager settingsManager;
	private String name = "Mod1";
	private String id = "ID1";
	
	private ModSetting setting;
	
	@Before
	public void setup() {
		settingsManager = mock(ISettingsManager.class);
		
		setting = new ModSetting(settingsManager, name, id, true);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(setting);
		assertEquals(name, setting.name());
		assertEquals(id, setting.id());
		assertTrue(setting.active().get());
	}

	@Test
	public void testActive() {
		setting.active().set(false);
		verify(settingsManager, times(1)).setBooleanValue(id + "Active", false);
	}
}
