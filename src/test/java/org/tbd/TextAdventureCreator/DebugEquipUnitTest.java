package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.textadventurecreator.debug.BodyPartDebugModel;
import ilusr.textadventurecreator.debug.DebugItemModel;
import ilusr.textadventurecreator.debug.EquipDebugModel;
import javafx.beans.property.SimpleStringProperty;

public class DebugEquipUnitTest {

	private BodyPartDebugModel bodyPart;
	private DebugItemModel item;
	
	private EquipDebugModel model;
	
	@Before
	public void setup() {
		bodyPart = Mockito.mock(BodyPartDebugModel.class);
		Mockito.when(bodyPart.name()).thenReturn(new SimpleStringProperty("Head"));
		item = Mockito.mock(DebugItemModel.class);
		Mockito.when(item.name()).thenReturn(new SimpleStringProperty("Hat"));
		model = new EquipDebugModel(bodyPart, item);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}

	@Test
	public void testBodyPart() {
		assertEquals(bodyPart, model.bodyPart());
	}
	
	@Test
	public void testItem() {
		assertEquals(item, model.item().get());
	}
	
	@Test
	public void testName() {
		assertEquals("Head : Hat", model.displayName().get());
	}
	
	@Test
	public void testItemChange() {
		DebugItemModel newItem = Mockito.mock(DebugItemModel.class);
		Mockito.when(newItem.name()).thenReturn(new SimpleStringProperty("Top Hat"));
		model.item().set(newItem);
		assertEquals("Head : Top Hat", model.displayName().get());
	}
	
	@Test
	public void testReset() {
		model.changed().set(true);
		model.resetChangeNotifications();
		assertFalse(model.changed().get());
	}
}
