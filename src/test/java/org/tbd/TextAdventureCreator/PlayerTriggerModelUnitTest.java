package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.trigger.PlayerTriggerModel;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class PlayerTriggerModelUnitTest {

	private PlayerTriggerPersistenceObject trigger; 
	private List<PlayerPersistenceObject> players;
	private ILanguageService languageService;
	
	private PlayerPersistenceObject player1;
	
	private PlayerTriggerModel model;
	
	@Before
	public void setup() {
		trigger = mock(PlayerTriggerPersistenceObject.class);
		when(trigger.modificationObject()).thenReturn(ModificationObject.Attribute);
		when(trigger.condition()).thenReturn(Condition.EqualTo);
		player1 = mock(PlayerPersistenceObject.class);
		when(player1.playerName()).thenReturn("Player1");
		when(player1.attributes()).thenReturn(Arrays.asList(mock(AttributePersistenceObject.class)));
		when(player1.characteristics()).thenReturn(Arrays.asList(mock(CharacteristicPersistenceObject.class)));
		
		BodyPartPersistenceObject bPart = mock(BodyPartPersistenceObject.class);
		when(bPart.objectName()).thenReturn("Head");
		when(bPart.characteristics()).thenReturn(Arrays.asList(mock(CharacteristicPersistenceObject.class)));
		when(player1.bodyParts()).thenReturn(Arrays.asList(bPart));
		
		InventoryPersistenceObject inv = mock(InventoryPersistenceObject.class);
		ItemPersistenceObject item = mock(ItemPersistenceObject.class);
		when(item.itemName()).thenReturn("Potion");
		when(item.properties()).thenReturn(Arrays.asList(mock(PropertyPersistenceObject.class)));
		when(inv.items()).thenReturn(Arrays.asList(item));
		when(player1.inventory()).thenReturn(inv);
		
		players = new ArrayList<>();
		players.add(player1);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.PROPERTY)).thenReturn("Property");
		when(languageService.getValue(DisplayStrings.ID)).thenReturn("ID");
		when(languageService.getValue(DisplayStrings.ADDITIONAL_ID)).thenReturn("Additional ID");
		when(languageService.getValue(DisplayStrings.DATA_MEMBER)).thenReturn("Data Member");
		when(languageService.getValue(DisplayStrings.ADDITIONAL_DATA_MEMBER)).thenReturn("Additional Data Member");
		when(languageService.getValue(DisplayStrings.CONDITION)).thenReturn("Condition");
		when(languageService.getValue(DisplayStrings.EXPECTED_VALUE)).thenReturn("Expected Value");
		
		model = new PlayerTriggerModel(trigger, players, languageService);
	}
	
	@Test
	public void testProperties() {
		assertEquals(5, model.properties().list().size());
	}
	
	@Test
	public void testConditions() {
		assertEquals(5, model.conditions().list().size());
	}
	
	@Test
	public void testSelectedPlayer() {
		assertEquals(1, model.players().list().size());
		model.players().selected().set("Player1");
		verify(trigger, times(1)).playerName("Player1");
	}
	
	@Test
	public void testAttributeId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("");
		model.properties().selected().set("Attribute");
		assertEquals(1, model.ids().list().size());
	}
	
	@Test
	public void testCharacteristicId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Characteristic");
		assertEquals(1, model.ids().list().size());
	}
	
	@Test
	public void testBodyPartId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Body Part");
		assertEquals(1, model.ids().list().size());
	}
	
	@Test
	public void testItemId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Item");
		assertEquals(1, model.ids().list().size());
	}
	
	@Test
	public void testId() {
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		model.ids().selected().set("Potion");
		verify(trigger, times(1)).id(captor.capture());
		assertEquals(1, captor.getValue().length);
	}
	
	@Test
	public void testAdditionalId() {
		ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
		model.ids().selected().set("Potion");
		model.additionalIds().selected().set("Consumable");
		verify(trigger, times(2)).id(captor.capture());
		assertEquals(2, captor.getValue().length);
	}
	
	@Test
	public void testItemAdditionalId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Item");
		model.ids().selected().set("Potion");
		assertEquals(1, model.additionalIds().list().size());
	}
	
	@Test
	public void testBodyPartAdditionalId() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Body Part");
		model.ids().selected().set("Head");
		assertEquals(1, model.additionalIds().list().size());
	}
	
	@Test
	public void testDataMembers() {
		model.dataMembers().selected().set("Name");
		verify(trigger, times(1)).dataMember("Name");
	}
	
	@Test
	public void testAdditionalDataMembersWhenNeeded() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Item");
		model.ids().selected().set("Potion");
		model.dataMembers().selected().set("Properties");
		model.additionalDataMembers().selected().set("Name");
		verify(trigger, times(1)).dataMember("Name");
	}
	
	@Test
	public void testAdditionalDataMembersWhenNotNeeded() {
		model.additionalDataMembers().selected().set("Test");
		verify(trigger, times(0)).dataMember("Test");
	}
	
	@Test
	public void testExpectedValue() {
		model.expectedValue().set("TestValue");
		verify(trigger, times(1)).comparisonData("TestValue");
	}
	
	@Test
	public void testAdditionalIdsNeededProperty() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Item");
		model.ids().selected().set("Potion");
		model.dataMembers().selected().set("Properties");
		assertTrue(model.additionalIdsNeeded());
	}
	
	@Test
	public void testAdditionalIdsNeededCharacteristic() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Body Part");
		model.ids().selected().set("Head");
		model.dataMembers().selected().set("Characteristic");
		assertTrue(model.additionalIdsNeeded());
	}
	
	@Test
	public void testAdditionalIdsNeededWithoutPropOrChar() {
		assertFalse(model.additionalIdsNeeded());
	}
	
	@Test
	public void testIdAndDataMemberNeededForName() {
		model.properties().selected().set("Name");
		assertFalse(model.idAndDataMemberNeeded());
	}
	
	@Test
	public void testIdAndDataMemberNeeded() {
		model.properties().selected().set("Value");
		assertTrue(model.idAndDataMemberNeeded());
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(trigger, model.persistenceObject());
	}
	
	@Test
	public void testAttributeDataMember() {
		model.players().selected().set("Player1");
		model.properties().selected().set("");
		model.properties().selected().set("Attribute");
		assertEquals(3, model.dataMembers().list().size());
		assertTrue(model.dataMembers().list().contains("Value"));
	}
	
	@Test
	public void testCharacteristicDataMember() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Characteristic");
		assertEquals(3, model.dataMembers().list().size());
		assertTrue(model.dataMembers().list().contains("Value"));
	}
	
	@Test
	public void testBodyPartDataMember() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Body Part");
		assertEquals(3, model.dataMembers().list().size());
		assertTrue(model.dataMembers().list().contains("Characteristic"));
	}
	
	@Test
	public void testItemDataMember() {
		model.players().selected().set("Player1");
		model.properties().selected().set("Item");
		assertEquals(3, model.dataMembers().list().size());
		assertTrue(model.dataMembers().list().contains("Properties"));
	}
	
	@Test
	public void testPlayerText() {
		assertEquals("Player", model.playerText().get());
	}
	
	@Test
	public void testPropertyText() {
		assertEquals("Property", model.propertyText().get());
	}
	
	@Test
	public void testIdText() {
		assertEquals("ID", model.idText().get());
	}
	
	@Test
	public void testAdditionalIdText() {
		assertEquals("Additional ID", model.additionalIdText().get());
	}
	
	@Test
	public void testDataMemberText() {
		assertEquals("Data Member", model.dataMemberText().get());
	}
	
	@Test
	public void testAdditionalDataMemberText() {
		assertEquals("Additional Data Member", model.additionalDataMemberText().get());
	}
	
	@Test
	public void testConditionText() {
		assertEquals("Condition", model.conditionText().get());
	}
	
	@Test
	public void testExepectedText() {
		assertEquals("Expected Value", model.expectedValueText().get());
	}
}
