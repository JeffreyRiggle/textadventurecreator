package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.debug.DebugAttribute;
import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;

public class DebugAttributeUnitTest {

	private IAttribute attribute;
	
	@Before
	public void setup() {
		attribute = new Attribute("Age", "13");
	}
	
	@Test
	public void testCreate() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertNotNull(att);
	}

	@Test
	public void testDescription() {
		attribute.description("test");
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("test", att.description());
		
		attribute.description("something");
		assertEquals("something", att.description());
	}
	
	@Test
	public void testName() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("Age", att.name());
		
		attribute.name("Class");
		assertEquals("Class", att.name());
	}
	
	@Test
	public void testChangeListener() {
		UpdateListener updated = new UpdateListener();
		DebugAttribute att = new DebugAttribute(attribute);
		att.updated(updated);
		attribute.value("test");
		
		assertEquals(true, updated.ran());
	}
	
	@Test
	public void testStringValue() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("13", att.value());
		
		attribute.value("testing");
		assertEquals("testing", att.value());
	}
	
	@Test
	public void testIntValue() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("13", att.value());
		
		attribute.value(15);
		assertEquals("15", att.value());
	}
	
	@Test
	public void testDoubleValue() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("13", att.value());
		
		attribute.value(15.5);
		assertEquals("15.5", att.value());
	}
	
	@Test
	public void testFloatValue() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("13", att.value());
		
		attribute.value(15.6f);
		assertEquals("15.6", att.value());
	}
	
	@Test
	public void testBoolValue() {
		DebugAttribute att = new DebugAttribute(attribute);
		assertEquals("13", att.value());
		
		attribute.value(false);
		assertEquals("false", att.value());
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
