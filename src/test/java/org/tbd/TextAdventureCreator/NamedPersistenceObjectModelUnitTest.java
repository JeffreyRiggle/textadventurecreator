package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.views.player.NamedPersistenceObjectModel;
import textadventurelib.persistence.player.NamedPersistenceObject;

public class NamedPersistenceObjectModelUnitTest {

	private NamedPersistenceObject persistence;
	
	private NamedPersistenceObjectModel model;
	
	@Before
	public void setup() {
		persistence = mock(NamedPersistenceObject.class);
		when(persistence.valueType()).thenReturn("string");
		
		model = new NamedPersistenceObjectModel(persistence);
	}
	
	@Test
	public void testTypes() {
		assertEquals(4, model.types().list().size());
	}

	@Test
	public void testBooleanValue() {
		model.types().selected().set("True/False");
		model.value().set("True");
		assertEquals("True", model.value().get());
		verify(persistence, times(1)).objectValue(true);
	}
	
	@Test
	public void testIntegerValue() {
		model.types().selected().set("Number");
		model.value().set("15");
		assertEquals("15", model.value().get());
		verify(persistence, times(1)).objectValue(15);
	}
	
	@Test
	public void testDoubleValue() {
		model.types().selected().set("Decimal");
		model.value().set("12.87");
		assertEquals("12.87", model.value().get());
		verify(persistence, times(1)).objectValue(12.87);
	}
	
	@Test
	public void testStringValue() {
		model.types().selected().set("Word");
		model.value().set("This is some word value");
		assertEquals("This is some word value", model.value().get());
		verify(persistence, times(1)).objectValue(eq("This is some word value"));
	}
	
	@Test
	public void testName() {
		model.name().set("TestObject");
		assertEquals("TestObject", model.name().get());
		verify(persistence, times(1)).objectName("TestObject");
	}
	
	@Test
	public void testDescription() {
		model.description().set("TestDesc");
		assertEquals("TestDesc", model.description().get());
		verify(persistence, times(1)).description("TestDesc");
	}
}
