package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.textadventurecreator.debug.DebugItemModel;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import playerlib.items.IItem;
import playerlib.items.Item;
import playerlib.items.Property;

public class DebugItemUnitTest {

	private IItem item;
	private ILanguageService languageService;
	
	@Before
	public void setup() {
		languageService = Mockito.mock(ILanguageService.class);
		item = new Item("TestItem");
	}
	
	@Test
	public void testCreate() {
		DebugItemModel model = new DebugItemModel(item, languageService);
		assertNotNull(model);
	}

	@Test
	public void testCreateWithProperties() {
		item.addProperty(new Property("Prop1", "Value1"));
		DebugItemModel model = new DebugItemModel(item, languageService);
		assertEquals(1, model.properties().size());
	}
	
	@Test
	public void testName() {
		DebugItemModel model = new DebugItemModel(item, languageService);
		assertEquals("TestItem", model.name().get());
	}
	
	@Test
	public void testDescription() {
		item.description("this is a test");
		DebugItemModel model = new DebugItemModel(item, languageService);
		assertEquals("this is a test", model.description().get());
	}
	
	@Test
	public void testPropertyTitle() {
		Mockito.when(languageService.getValue(DisplayStrings.PROPERTIES)).thenReturn("Properties");
		DebugItemModel model = new DebugItemModel(item, languageService);
		assertEquals("Properties", model.propTitle().get());
	}
}
