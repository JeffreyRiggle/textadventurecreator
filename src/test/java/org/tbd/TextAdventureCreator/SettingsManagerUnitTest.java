package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.iroshell.services.IApplicationClosingManager;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.settings.SettingsManager;

public class SettingsManagerUnitTest {

	private XmlConfigurationManager configurationManager;
	private IApplicationClosingManager closingManager;
	
	private XmlConfigurationObject root;
	
	private SettingsManager manager;
	
	@Before
	public void setup() {
		configurationManager = mock(XmlConfigurationManager.class);
		root = mock(XmlConfigurationObject.class);
		when(root.children()).thenReturn(new ArrayList<PersistXml>());
		when(configurationManager.configurationObjects()).thenReturn(Arrays.asList(root));
		
		closingManager = mock(IApplicationClosingManager.class);
		
		manager = new SettingsManager(configurationManager, closingManager);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(manager);
		verify(closingManager, times(1)).addApplicationClosingListener(manager);
		
		try {
			verify(configurationManager, times(1)).load();
		} catch (Exception e) { 
			fail(e.getMessage());
		}
	}

	@Test
	public void testStringSetting() {
		manager.setStringSetting("Setting1", "Value1");
		assertEquals("Value1", manager.getStringSetting("Setting1", new String()));
	}
	
	@Test
	public void testGetStringSettingDefault() {
		assertEquals("Value", manager.getStringSetting("Something", "Value"));
	}
	
	@Test
	public void testBooleanSetting() {
		manager.setBooleanValue("Setting2", true);
		assertTrue(manager.getBooleanSetting("Setting2", false));
	}
	
	@Test
	public void testGetBooleanSettingDefault() {
		assertFalse(manager.getBooleanSetting("Somthing2", false));
	}
	
	@Test
	public void testIntSetting() {
		manager.setIntValue("Setting3", 12);
		assertEquals(12, manager.getIntSetting("Setting3", 5));
	}
	
	@Test
	public void testGetIntSettingDefault() {
		assertEquals(6, manager.getIntSetting("Something3", 6));
	}
	
	@Test
	public void testSave() {
		ArgumentCaptor<XmlConfigurationObject> root = ArgumentCaptor.forClass(XmlConfigurationObject.class);
		manager.setStringSetting("Setting1", "Value1");
		manager.setBooleanValue("Setting2", true);
		manager.setIntValue("Setting3", 12);
		manager.save();
		
		verify(configurationManager, times(1)).clearConfigurationObjects();
		verify(configurationManager, times(1)).addConfigurationObject(root.capture());
		assertEquals(3, root.getValue().children().size());
		try {
			verify(configurationManager, times(1)).prepare();
			verify(configurationManager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSaveAsync() {
		ArgumentCaptor<XmlConfigurationObject> root = ArgumentCaptor.forClass(XmlConfigurationObject.class);
		manager.setStringSetting("Setting1", "Value1");
		manager.setBooleanValue("Setting2", true);
		manager.setIntValue("Setting3", 12);
		manager.saveAsync();
		
		try {
			//TODO: This is a hack
			Thread.sleep(1000);
		} catch (Exception e) { }
		
		verify(configurationManager, times(1)).clearConfigurationObjects();
		verify(configurationManager, times(1)).addConfigurationObject(root.capture());
		assertEquals(3, root.getValue().children().size());
		try {
			verify(configurationManager, times(1)).prepare();
			verify(configurationManager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testClosing() {
		ArgumentCaptor<XmlConfigurationObject> root = ArgumentCaptor.forClass(XmlConfigurationObject.class);
		manager.setStringSetting("Setting1", "Value1");
		manager.setBooleanValue("Setting2", true);
		manager.setIntValue("Setting3", 12);
		manager.onClose();
		
		verify(configurationManager, times(1)).clearConfigurationObjects();
		verify(configurationManager, times(1)).addConfigurationObject(root.capture());
		assertEquals(3, root.getValue().children().size());
		try {
			verify(configurationManager, times(1)).prepare();
			verify(configurationManager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
