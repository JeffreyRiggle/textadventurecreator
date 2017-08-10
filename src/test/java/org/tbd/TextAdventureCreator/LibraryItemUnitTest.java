package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.library.LibraryItem;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TimerPersistenceObject;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class LibraryItemUnitTest {

	private LibraryItem item;
	
	@Before
	public void setup() {
		item = new LibraryItem();
	}
	
	@Test
	public void testLibraryName() {
		assertNotNull(item.getLibraryName());
		item.setLibraryName("My Library");
		assertEquals("My Library", item.getLibraryName());
	}

	@Test
	public void testAuthor() {
		assertNotNull(item.getAuthor());
		item.setAuthor("Someone");
		assertEquals("Someone", item.getAuthor());
	}
	
	@Test
	public void testAttributes() {
		assertNotNull(item.attributes());
		try {
			AttributePersistenceObject att = new AttributePersistenceObject();
			att.objectName("Test");
			item.attributes().add(att);
			assertTrue(item.attributes().contains(att));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCharacteristics() {
		assertNotNull(item.characteristics());
		try {
			CharacteristicPersistenceObject chr = new CharacteristicPersistenceObject();
			chr.objectName("Test");
			item.characteristics().add(chr);
			assertTrue(item.characteristics().contains(chr));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testProperties() {
		assertNotNull(item.properties());
		try {
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Test");
			item.properties().add(prop);
			assertTrue(item.properties().contains(prop));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testItems() {
		assertNotNull(item.items());
		try {
			ItemPersistenceObject itm = new ItemPersistenceObject();
			itm.itemName("Test");
			item.items().add(itm);
			assertTrue(item.items().contains(itm));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testBodyParts() {
		assertNotNull(item.bodyParts());
		try {
			BodyPartPersistenceObject bp = new BodyPartPersistenceObject();
			bp.objectName("Test");
			item.bodyParts().add(bp);
			assertTrue(item.bodyParts().contains(bp));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testPlayers() {
		assertNotNull(item.players());
		try {
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.playerName("Test");
			item.players().add(player);
			assertTrue(item.players().contains(player));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTriggers() {
		assertNotNull(item.triggers());
		try {
			TextTriggerPersistenceObject trigger = new TextTriggerPersistenceObject();
			trigger.text("Test");
			item.triggers().add(trigger);
			assertTrue(item.triggers().contains(trigger));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testActions() {
		assertNotNull(item.actions());
		try {
			ActionPersistenceObject action = new AppendTextActionPersistence();
			item.actions().add(action);
			assertTrue(item.actions().contains(action));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testOptions() {
		assertNotNull(item.options());
		try {
			OptionPersistenceObject option = new OptionPersistenceObject();
			item.options().add(option);
			assertTrue(item.options().contains(option));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTimers() {
		assertNotNull(item.timers());
		try {
			TimerPersistenceObject timer = new CompletionTimerPersistenceObject();
			item.timers().add(timer);
			assertTrue(item.timers().contains(timer));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGameStates() {
		assertNotNull(item.gameStates());
		try {
			GameStatePersistenceObject gs = new GameStatePersistenceObject("Test");
			item.gameStates().add(gs);
			assertTrue(item.gameStates().contains(gs));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testLayouts() {
		assertNotNull(item.layouts());
		try {
			LayoutPersistenceObject layout = new LayoutPersistenceObject();
			item.layouts().add(layout);
			assertTrue(item.layouts().contains(layout));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConfigurationObjectConversion() {
		try {
			LayoutPersistenceObject layout = new LayoutPersistenceObject();
			item.layouts().add(layout);
			GameStatePersistenceObject gs = new GameStatePersistenceObject("Test");
			item.gameStates().add(gs);
			TimerPersistenceObject timer = new CompletionTimerPersistenceObject();
			item.timers().add(timer);
			OptionPersistenceObject option = new OptionPersistenceObject();
			AppendTextActionPersistence act = new AppendTextActionPersistence();
			option.action(act);
			item.options().add(option);
			ActionPersistenceObject action = new AppendTextActionPersistence();
			item.actions().add(action);
			TextTriggerPersistenceObject trigger = new TextTriggerPersistenceObject();
			trigger.text("Test");
			item.triggers().add(trigger);
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.playerName("Test");
			item.players().add(player);
			BodyPartPersistenceObject bp = new BodyPartPersistenceObject();
			bp.objectName("Test");
			item.bodyParts().add(bp);
			ItemPersistenceObject itm = new ItemPersistenceObject();
			itm.itemName("Test");
			item.items().add(itm);
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Test");
			item.properties().add(prop);
			CharacteristicPersistenceObject chr = new CharacteristicPersistenceObject();
			chr.objectName("Test");
			item.characteristics().add(chr);
			AttributePersistenceObject att = new AttributePersistenceObject();
			att.objectName("Test");
			item.attributes().add(att);
			XmlConfigurationObject obj = item.toConfigurationObject();
			assertNotNull(obj);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCreateFromConfig() {
		try {
			item.setAuthor("Tester");
			item.setLibraryName("TestLib");
			LayoutPersistenceObject layout = new LayoutPersistenceObject();
			item.layouts().add(layout);
			layout.layoutType(LayoutType.ContentOnly);
			GameStatePersistenceObject gs = new GameStatePersistenceObject("Test");
			item.gameStates().add(gs);
			TimerPersistenceObject timer = new CompletionTimerPersistenceObject();
			item.timers().add(timer);
			OptionPersistenceObject option = new OptionPersistenceObject();
			AppendTextActionPersistence act = new AppendTextActionPersistence();
			option.action(act);
			item.options().add(option);
			ActionPersistenceObject action = new AppendTextActionPersistence();
			item.actions().add(action);
			TextTriggerPersistenceObject trigger = new TextTriggerPersistenceObject();
			trigger.text("Test");
			item.triggers().add(trigger);
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.playerName("Test");
			item.players().add(player);
			BodyPartPersistenceObject bp = new BodyPartPersistenceObject();
			bp.objectName("Test");
			item.bodyParts().add(bp);
			ItemPersistenceObject itm = new ItemPersistenceObject();
			itm.itemName("Test");
			item.items().add(itm);
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Test");
			item.properties().add(prop);
			CharacteristicPersistenceObject chr = new CharacteristicPersistenceObject();
			chr.objectName("Test");
			item.characteristics().add(chr);
			AttributePersistenceObject att = new AttributePersistenceObject();
			att.objectName("Test");
			item.attributes().add(att);
			XmlConfigurationObject obj = item.toConfigurationObject();
			
			LibraryItem newItem = new LibraryItem(obj);
			assertEquals("Tester", newItem.getAuthor());
			assertEquals("TestLib", newItem.getLibraryName());
			assertEquals(1, newItem.attributes().size());
			assertEquals(1, newItem.characteristics().size());
			assertEquals(1, newItem.properties().size());
			assertEquals(1, newItem.items().size());
			assertEquals(1, newItem.bodyParts().size());
			assertEquals(1, newItem.players().size());
			assertEquals(1, newItem.triggers().size());
			assertEquals(1, newItem.actions().size());
			assertEquals(1, newItem.options().size());
			assertEquals(1, newItem.gameStates().size());
			assertEquals(1, newItem.layouts().size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
