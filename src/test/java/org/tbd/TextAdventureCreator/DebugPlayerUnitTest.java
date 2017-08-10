package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.core.test.FXThread;
import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.debug.BodyPartDebugModel;
import ilusr.textadventurecreator.debug.DebugItemModel;
import ilusr.textadventurecreator.debug.DebugNamedObjectModel;
import ilusr.textadventurecreator.debug.EquipDebugModel;
import ilusr.textadventurecreator.debug.PlayerDebugModel;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.Characteristic;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.BodyPart;
import playerlib.equipment.IBodyPart;
import playerlib.items.IItem;
import playerlib.items.Item;
import playerlib.player.IPlayer;
import playerlib.player.Player;

public class DebugPlayerUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	private IPlayer player;
	private IAttribute att1;
	private ICharacteristic char1;
	private IBodyPart bp1;
	private IItem item1;
	
	private PlayerDebugModel model;
	
	@Before
	public void setup() {
		languageService = Mockito.mock(ILanguageService.class);
		Mockito.when(languageService.getValue(DisplayStrings.ATTRIBUTES)).thenReturn("Attributes");
		Mockito.when(languageService.getValue(DisplayStrings.CHARACTERISTICS)).thenReturn("Characteristics");
		Mockito.when(languageService.getValue(DisplayStrings.BODY_PARTS)).thenReturn("Body Parts");
		Mockito.when(languageService.getValue(DisplayStrings.ITEMS)).thenReturn("Items");
		Mockito.when(languageService.getValue(DisplayStrings.EQUIPMENT)).thenReturn("Equipment");
		
		player = new Player("player1");
		att1 = new Attribute("Attribute1", "Value1");
		char1 = new Characteristic("Characteristic1", "Value1");
		bp1 = new BodyPart("BodyPart1");
		item1 = new Item("Item1");
		player.addAttribute(att1);
		player.addCharacteristic(char1);
		player.addBodyPart(bp1);
		player.inventory().addItem(item1, 1);
		player.equipment().equip(bp1, item1);
		
		model = new PlayerDebugModel(player, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
		assertEquals(1, model.attributes().size());
		assertEquals(1, model.characteristics().size());
		assertEquals(1, model.bodyParts().size());
		assertEquals(1, model.items().size());
		assertEquals(1, model.equipment().size());
	}

	@Test
	@FXThread(runOnFXThread = false)
	public void testAttributeAdded() {
		IAttribute att2 = new Attribute("Attribute2", "Value2");
		player.addAttribute(att2);
		waitForPlatform();
		
		assertEquals(2, model.attributes().size());
		
		boolean attributeAdded = false;
		
		for (DebugNamedObjectModel mod : model.attributes()) {
			if (mod.name().get().equals("Attribute2")) {
				attributeAdded = mod.added().get();
				break;
			}
		}
		
		assertTrue(attributeAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testAttributeRemoved() {
		player.removeAttribute(att1);
		waitForPlatform();
		
		assertEquals(1, model.attributes().size());
		
		boolean attributeRemoved = false;
		
		for (DebugNamedObjectModel mod : model.attributes()) {
			if (mod.name().get().equals(att1.name())) {
				attributeRemoved = mod.removed().get();
				break;
			}
		}
		
		assertTrue(attributeRemoved);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testCharacteristicAdded() {
		ICharacteristic char2 = new Characteristic("Characteristic2", "Value2");
		player.addCharacteristic(char2);
		waitForPlatform();
		
		assertEquals(2, model.characteristics().size());
		
		boolean charAdded = false;
		
		for (DebugNamedObjectModel mod : model.characteristics()) {
			if (mod.name().get().equals("Characteristic2")) {
				charAdded = mod.added().get();
				break;
			}
		}
		
		assertTrue(charAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testCharacteristicRemoved() {
		player.removeCharacteristic(char1);
		waitForPlatform();
		
		assertEquals(1, model.characteristics().size());
		
		boolean charRemoved = false;
		
		for (DebugNamedObjectModel mod : model.characteristics()) {
			if (mod.name().get().equals(char1.name())) {
				charRemoved = mod.removed().get();
				break;
			}
		}
		
		assertTrue(charRemoved);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testBodyPartAdded() {
		IBodyPart bp2 = new BodyPart("Body2");
		player.addBodyPart(bp2);
		waitForPlatform();
		
		assertEquals(2, model.bodyParts().size());
		
		boolean bpAdded = false;
		
		for (BodyPartDebugModel mod : model.bodyParts()) {
			if (mod.name().get().equals("Body2")) {
				bpAdded = mod.added().get();
				break;
			}
		}
		
		assertTrue(bpAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testBodyPartRemoved() {
		player.removeBodyPart(bp1);
		waitForPlatform();
		
		assertEquals(1, model.bodyParts().size());
		
		boolean bpRemoved = false;
		
		for (BodyPartDebugModel mod : model.bodyParts()) {
			if (mod.name().get().equals(bp1.name())) {
				bpRemoved = mod.removed().get();
				break;
			}
		}
		
		assertTrue(bpRemoved);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testItemAdded() {
		IItem item2 = new Item("Item2");
		player.inventory().addItem(item2, 1);
		waitForPlatform();
		
		assertEquals(2, model.items().size());
		
		boolean itemAdded = false;
		
		for (DebugItemModel mod : model.items()) {
			if (mod.name().get().equals("Item2")) {
				itemAdded = mod.added().get();
				break;
			}
		}
		
		assertTrue(itemAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testItemRemoved() {
		player.inventory().removeItem(item1);
		waitForPlatform();
		
		assertEquals(1, model.items().size());
		
		boolean itemRemoved = false;
		
		for (DebugItemModel mod : model.items()) {
			if (mod.name().get().equals(item1.name())) {
				itemRemoved = mod.removed().get();
				break;
			}
		}
		
		assertTrue(itemRemoved);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testEquipmentAdded() {
		IBodyPart bp2 = new BodyPart("Bod2");
		IItem item2 = new Item("Item2");
		player.equipment().equip(bp2, item2);
		waitForPlatform();
		
		assertEquals(2, model.equipment().size());
		
		boolean equipAdded = false;
		
		for (EquipDebugModel mod : model.equipment()) {
			if (mod.bodyPart().name().get().equals("Bod2")) {
				equipAdded = mod.added().get();
				break;
			}
		}
		
		assertTrue(equipAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testEquipmentRemoved() {
		player.equipment().unEquip(bp1);
		waitForPlatform();
		
		assertEquals(1, model.equipment().size());
		
		boolean equipRemoved = false;
		
		for (EquipDebugModel mod : model.equipment()) {
			if (mod.bodyPart().name().get().equals(bp1.name())) {
				equipRemoved = mod.removed().get();
				break;
			}
		}
		
		assertTrue(equipRemoved);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testEquipmentChanged() {
		IItem item2 = new Item("Item2");
		player.equipment().equip(bp1, item2);
		waitForPlatform();
		
		assertEquals(1, model.equipment().size());
		
		boolean equipChanged = false;
		
		for (EquipDebugModel mod : model.equipment()) {
			if (mod.bodyPart().name().get().equals(bp1.name())) {
				equipChanged = mod.changed().get();
				break;
			}
		}
		
		assertTrue(equipChanged);
	}
	
	@Test
	public void testAttributeText() {
		assertEquals("Attributes", model.attributeText().get());
	}
	
	@Test
	public void testCharacteristicText() {
		assertEquals("Characteristics", model.characteristicText().get());
	}
	
	@Test
	public void testBodyPartText() {
		assertEquals("Body Parts", model.bodyPartText().get());
	}
	
	@Test
	public void testInventoryText() {
		assertEquals("Items", model.inventoryText().get());
	}
	
	@Test
	public void testEquipmentText() {
		assertEquals("Equipment", model.equipmentText().get());
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testResetWithoutRemove() {
		IAttribute att2 = new Attribute("Attribute2", "Value2");
		player.addAttribute(att2);
		waitForPlatform();
		model.resetNotifications();
		
		for (DebugNamedObjectModel mod : model.attributes()) {
			assertFalse(mod.changed().get());
			assertFalse(mod.removed().get());
			assertFalse(mod.added().get());
		}
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testResetWithRemove() {
		player.removeAttribute(att1);
		player.removeCharacteristic(char1);
		player.removeBodyPart(bp1);
		player.equipment().unEquip(bp1);
		player.inventory().removeItem(item1);
		waitForPlatform();
		
		model.resetNotifications();
		
		assertEquals(0, model.attributes().size());
		assertEquals(0, model.characteristics().size());
		assertEquals(0, model.bodyParts().size());
		assertEquals(0, model.equipment().size());
		assertEquals(0, model.items().size());
	}
	
	private void waitForPlatform() {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	latch.countDown();
        });
        
        try {
        	latch.await();
        } catch (Exception e) { }
	}
}
