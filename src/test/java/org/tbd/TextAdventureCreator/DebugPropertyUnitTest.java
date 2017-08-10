package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.debug.DebugProperty;
import playerlib.items.IProperty;
import playerlib.items.Property;

public class DebugPropertyUnitTest {

	private IProperty property;
	
	@Before
	public void setup() {
		property = new Property("Age", "13");
	}
	
	@Test
	public void testCreate() {
		DebugProperty prop = new DebugProperty(property);
		assertNotNull(prop);
	}

	@Test
	public void testDescription() {
		property.description("test");
		DebugProperty prop = new DebugProperty(property);
		assertEquals("test", prop.description());
		
		property.description("something");
		assertEquals("something", prop.description());
	}
	
	@Test
	public void testName() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("Age", prop.name());
		
		property.name("Class");
		assertEquals("Class", prop.name());
	}
	
	@Test
	public void testChangeListener() {
		UpdateListener updated = new UpdateListener();
		DebugProperty prop = new DebugProperty(property);
		prop.updated(updated);
		property.value("test");
		
		assertEquals(true, updated.ran());
	}
	
	@Test
	public void testStringValue() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("13", prop.value());
		
		property.value("testing");
		assertEquals("testing", prop.value());
	}
	
	@Test
	public void testIntValue() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("13", prop.value());
		
		property.value(15);
		assertEquals("15", prop.value());
	}
	
	@Test
	public void testDoubleValue() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("13", prop.value());
		
		property.value(15.5);
		assertEquals("15.5", prop.value());
	}
	
	@Test
	public void testFloatValue() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("13", prop.value());
		
		property.value(15.6f);
		assertEquals("15.6", prop.value());
	}
	
	@Test
	public void testBoolValue() {
		DebugProperty prop = new DebugProperty(property);
		assertEquals("13", prop.value());
		
		property.value(false);
		assertEquals("false", prop.value());
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
