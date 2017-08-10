package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.debug.DebugCharacteristic;
import playerlib.characteristics.Characteristic;
import playerlib.characteristics.ICharacteristic;

public class DebugCharacteristicUnitTest {

	private ICharacteristic characteristic;
	
	@Before
	public void setup() {
		characteristic = new Characteristic("Age", "13");
	}
	
	@Test
	public void testCreate() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertNotNull(chr);
	}

	@Test
	public void testDescription() {
		characteristic.description("test");
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("test", chr.description());
		
		characteristic.description("something");
		assertEquals("something", chr.description());
	}
	
	@Test
	public void testName() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("Age", chr.name());
		
		characteristic.name("Class");
		assertEquals("Class", chr.name());
	}
	
	@Test
	public void testChangeListener() {
		UpdateListener updated = new UpdateListener();
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		chr.updated(updated);
		characteristic.value("test");
		
		assertEquals(true, updated.ran());
	}
	
	@Test
	public void testStringValue() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("13", chr.value());
		
		characteristic.value("testing");
		assertEquals("testing", chr.value());
	}
	
	@Test
	public void testIntValue() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("13", chr.value());
		
		characteristic.value(15);
		assertEquals("15", chr.value());
	}
	
	@Test
	public void testDoubleValue() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("13", chr.value());
		
		characteristic.value(15.5);
		assertEquals("15.5", chr.value());
	}
	
	@Test
	public void testFloatValue() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("13", chr.value());
		
		characteristic.value(15.6f);
		assertEquals("15.6", chr.value());
	}
	
	@Test
	public void testBoolValue() {
		DebugCharacteristic chr = new DebugCharacteristic(characteristic);
		assertEquals("13", chr.value());
		
		characteristic.value(false);
		assertEquals("false", chr.value());
	}
	
	private class UpdateListener implements Runnable {

		private boolean ran;
		
		public UpdateListener() {
			ran = false;
		}
		
		@Override
		public void run() {
			ran = true;
		}
		
		public boolean ran() {
			return ran;
		}
	}
}
