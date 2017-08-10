package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PropertyType;
import ilusr.textadventurecreator.views.macro.MacroBuilderModel;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class MacroBuilderModelUnitTest {

	private List<PlayerPersistenceObject> players;
	private PlayerPersistenceObject player;
	private ILanguageService languageService;
	
	private MacroBuilderModel model;
	
	@Before
	public void setup() {
		player = mock(PlayerPersistenceObject.class);
		when(player.playerName()).thenReturn("Player1");
		AttributePersistenceObject att = mock(AttributePersistenceObject.class);
		when(att.objectName()).thenReturn("Age");
		when(player.attributes()).thenReturn(Arrays.asList(att));
		CharacteristicPersistenceObject chr = mock(CharacteristicPersistenceObject.class);
		when(chr.objectName()).thenReturn("SkinColor");
		when(player.characteristics()).thenReturn(Arrays.asList(chr));
		BodyPartPersistenceObject bPart = mock(BodyPartPersistenceObject.class);
		when(bPart.objectName()).thenReturn("Head");
		CharacteristicPersistenceObject bchr = mock(CharacteristicPersistenceObject.class);
		when(bchr.objectName()).thenReturn("HairColor");
		when(bPart.characteristics()).thenReturn(Arrays.asList(bchr));
		when(player.bodyParts()).thenReturn(Arrays.asList(bPart));
		InventoryPersistenceObject inv = mock(InventoryPersistenceObject.class);
		ItemPersistenceObject item = mock(ItemPersistenceObject.class);
		when(item.itemName()).thenReturn("Item1");
		PropertyPersistenceObject prop = mock(PropertyPersistenceObject.class);
		when(prop.objectName()).thenReturn("Prop1");
		when(item.properties()).thenReturn(Arrays.asList(prop));
		when(inv.items()).thenReturn(Arrays.asList(item));
		when(player.inventory()).thenReturn(inv);
		
		players = Arrays.asList(player);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.BUILD)).thenReturn("Build");
		when(languageService.getValue(DisplayStrings.MACRO_WITH_COLON)).thenReturn("Macro:");
		
		model = new MacroBuilderModel(players, languageService);
	}
	
	@Test
	public void testPlayers() {
		assertEquals(players, model.players());
	}

	@Test
	public void testSelectedPlayer() {
		assertNull(model.selectedPlayer().get());
		model.selectedPlayer().set("Player1");
		assertEquals(model.selectedPlayer().get(), "Player1");
	}
	
	@Test
	public void testAttributes() {
		assertEquals(0, model.attributes().list().size());
		model.selectedPlayer().set("Player1");
		assertEquals(1, model.attributes().list().size());
	}
	
	@Test
	public void testCharacteristics() {
		assertEquals(0, model.characteristics().list().size());
		model.selectedPlayer().set("Player1");
		assertEquals(1, model.characteristics().list().size());
	}
	
	@Test
	public void testBodyParts() {
		assertEquals(0, model.bodyParts().list().size());
		model.selectedPlayer().set("Player1");
		assertEquals(1, model.bodyParts().list().size());
	}
	
	@Test
	public void testItems() {
		assertEquals(0, model.items().list().size());
		model.selectedPlayer().set("Player1");
		assertEquals(1, model.items().list().size());
	}
	
	@Test
	public void testProperties() {
		assertEquals(0, model.properties().list().size());
		model.selectedPlayer().set("Player1");
		model.items().selected().set("Item1");
		assertEquals(1, model.properties().list().size());
	}
	
	@Test
	public void testPropertyTypes() {
		assertTrue(model.propertyTypes().contains("ID"));
		assertTrue(model.propertyTypes().contains("Attribute"));
		assertTrue(model.propertyTypes().contains("Body Part"));
		assertTrue(model.propertyTypes().contains("Characteristic"));
		assertTrue(model.propertyTypes().contains("Item"));
	}
	
	@Test
	public void testSelectedPropertyType() {
		assertNull(model.selectedPropertyType().get());
		model.selectedPropertyType().set("ID");
		assertEquals(model.selectedPropertyType().get(), "ID");
	}
	
	@Test
	public void testNamedPropertyTypes() {
		assertTrue(model.namedPropertyTypes().contains("Name"));
		assertTrue(model.namedPropertyTypes().contains("Description"));
		assertTrue(model.namedPropertyTypes().contains("Value"));
	}
	
	@Test
	public void testSelectedNamedPropertyType() {
		assertNull(model.selectedNamedProperty().get());
		model.selectedNamedProperty().set("Value");
		assertEquals(model.selectedNamedProperty().get(), "Value");
	}
	
	@Test
	public void testItemOptions() {
		assertTrue(model.itemOptions().list().contains("Name"));
		assertTrue(model.itemOptions().list().contains("Description"));
		assertTrue(model.itemOptions().list().contains("Properties"));
	}
	
	@Test
	public void testBodyPartOptions() {
		assertTrue(model.bodyPartOptions().list().contains("Name"));
		assertTrue(model.bodyPartOptions().list().contains("Description"));
		assertTrue(model.bodyPartOptions().list().contains("Characteristic"));
	}
	
	@Test
	public void testBuildText() {
		assertEquals("Build", model.buildText().get());
	}
	
	@Test
	public void testMacroText() {
		assertEquals("Macro:", model.macroText().get());
	}
	
	@Test
	public void testConvertPropertyTypeAttribute() {
		assertEquals(PropertyType.Attribute, model.convertPropertyType("Attribute"));
	}
	
	@Test
	public void testConvertPropertyTypeBodyPart() {
		assertEquals(PropertyType.BodyPart, model.convertPropertyType("Body Part"));
	}
	
	@Test
	public void testConvertPropertyTypeCharacteristic() {
		assertEquals(PropertyType.Characteristic, model.convertPropertyType("Characteristic"));
	}
	
	@Test
	public void testConvertPropertyTypeItem() {
		assertEquals(PropertyType.Item, model.convertPropertyType("Item"));
	}
	
	@Test
	public void testConvertPropertyTypeID() {
		assertEquals(PropertyType.ID, model.convertPropertyType("ID"));
	}
	
	@Test
	public void testConvertPropertyTypeProperty() {
		assertEquals(PropertyType.Property, model.convertPropertyType("Properties"));
	}
	
	@Test
	public void testConvertPropertyTypeDefault() {
		assertEquals(PropertyType.ID, model.convertPropertyType("INVALID"));
	}
	
	@Test
	public void testCurrentMacro() {
		assertNull(model.currentMacro());
	}
	
	@Test
	public void testCreateAttributeMacro() {
		model.selectedPlayer().set("Player1");
		model.selectedPropertyType().set("Attribute");
		model.attributes().selected().set("Age");
		model.selectedNamedProperty().set("Value");
		assertEquals("{[player<<Player1>>@attribute<<Age>>@value]}", model.createMacro());
		assertTrue(model.valid().get());
	}
	
	@Test
	public void testCreateBodyPartMacro() {
		model.selectedPlayer().set("Player1");
		model.selectedPropertyType().set("Body Part");
		model.bodyParts().selected().set("Head");
		model.bodyPartOptions().selected().set("Characteristic");
		model.selectedNamedProperty().set("Description");
		model.characteristics().selected().set("HairColor");
		assertEquals("{[player<<Player1>>@bodyPart<<Head>>@characteristic<<HairColor>>@description]}", model.createMacro());
		assertTrue(model.valid().get());
	}
	
	@Test
	public void testCreateIDMacro() {
		model.selectedPlayer().set("Player1");
		model.selectedPropertyType().set("ID");
		assertEquals("{[player<<Player1>>@name]}", model.createMacro());
		assertTrue(model.valid().get());
	}
	
	@Test
	public void testCreateCharacteristicMacro() {
		model.selectedPlayer().set("Player1");
		model.selectedPropertyType().set("Characteristic");
		model.characteristics().selected().set("SkinColor");
		model.selectedNamedProperty().set("Name");
		assertEquals("{[player<<Player1>>@characteristic<<SkinColor>>@name]}", model.createMacro());
		assertTrue(model.valid().get());
	}
	
	@Test
	public void testCreateItemMacro() {
		model.selectedPlayer().set("Player1");
		model.selectedPropertyType().set("Item");
		model.items().selected().set("Item1");
		model.itemOptions().selected().set("Properties");
		model.properties().selected().set("Prop1");
		model.selectedNamedProperty().set("Value");
		assertEquals("{[player<<Player1>>@inventory<<Item1>>@property<<Prop1>>@value]}", model.createMacro());
		assertTrue(model.valid().get());
	}
}
