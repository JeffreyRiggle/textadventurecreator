package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.textadventurecreator.debug.DebugNamedObjectModel;
import ilusr.textadventurecreator.debug.INamedObject;

public class DebugNamedObjectUnitTest {

	private INamedObject namedObject;
	
	private DebugNamedObjectModel model;
	
	@Before
	public void setup() {
		namedObject = Mockito.mock(INamedObject.class);
		Mockito.when(namedObject.name()).thenReturn("TestItem");
		Mockito.when(namedObject.description()).thenReturn("Some description");
		Mockito.when(namedObject.value()).thenReturn("Value");
		model = new DebugNamedObjectModel(namedObject);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}
	
	@Test
	public void testName() {
		assertEquals("TestItem", model.name().get());
	}
	
	@Test
	public void testDescription() {
		assertEquals("Some description", model.description().get());
	}
	
	@Test
	public void testValue() {
		assertEquals("Value", model.value().get());
	}
	
	@Test
	public void testRunnable() {
		Mockito.verify(namedObject, Mockito.times(1)).updated(Mockito.any());
	}
}
