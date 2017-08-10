package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PlayerModificationActionModel;
import ilusr.textadventurecreator.views.player.InventoryItem;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import textadventurelib.core.ChangeType;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.ModificationType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerModificationActionModelUnitTest {

	private ModifyPlayerActionPersistence action;
	private List<PlayerPersistenceObject> players;
	private ILanguageService languageService;
	
	private PlayerPersistenceObject player1;
	private PlayerPersistenceObject player2;
	
	private BodyPartPersistenceObject head;
	private ItemPersistenceObject item;
	
	private PlayerModificationActionModel model;
	
	@Before
	public void setup() {
		head = mock(BodyPartPersistenceObject.class);
		when(head.objectName()).thenReturn("Head");
		
		item = mock(ItemPersistenceObject.class);
		when(item.itemName()).thenReturn("Potion");
		
		player1 = mock(PlayerPersistenceObject.class);
		when(player1.playerName()).thenReturn("Player1");
		when(player1.bodyParts()).thenReturn(Arrays.asList(head));
		InventoryPersistenceObject inv = mock(InventoryPersistenceObject.class);
		when(inv.items()).thenReturn(Arrays.asList(item));
		when(player1.inventory()).thenReturn(inv);
		
		player2 = mock(PlayerPersistenceObject.class);
		when(player2.playerName()).thenReturn("Player2");
		
		action = mock(ModifyPlayerActionPersistence.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.PROPERTY)).thenReturn("Property");
		when(languageService.getValue(DisplayStrings.ID)).thenReturn("Id");
		when(languageService.getValue(DisplayStrings.DATA_MEMBER)).thenReturn("Data Member");
		when(languageService.getValue(DisplayStrings.MODIFICATION_TYPE)).thenReturn("Modification Type");
		when(languageService.getValue(DisplayStrings.CHANGE_TYPE)).thenReturn("Change Type");
		when(languageService.getValue(DisplayStrings.MODIFICATION_DATA)).thenReturn("Modification Data");
		
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
		
		model = new PlayerModificationActionModel(action, players, languageService);
	}
	
	@Test
	public void testCreateWithExistingData() {
		AttributePersistenceObject clz = mock(AttributePersistenceObject.class);
		when(clz.objectName()).thenReturn("Class");
		when(player1.attributes()).thenReturn(Arrays.asList(clz));
		ModifyPlayerActionPersistence act = mock(ModifyPlayerActionPersistence.class);
		when(act.playerName()).thenReturn("Player1");
		when(act.modificationObj()).thenReturn(ModificationObject.Attribute);
		when(act.changeType()).thenReturn(ChangeType.Add);
		when(act.id()).thenReturn("Class");
		when(act.data()).thenReturn(clz);
		
		PlayerModificationActionModel mod = new PlayerModificationActionModel(act, players, languageService);
		assertTrue(mod.id().list().contains("Class"));
		assertEquals(mod.id().selected().get(), "Class");
		assertEquals(mod.players().selected().get(), "Player1");
		assertEquals(mod.modificationObject().selected().get(), "Attribute");
		assertEquals(mod.changeTypes().selected().get(), "Add");
	}
	
	@Test
	public void testPlayers() {
		assertTrue(model.players().list().contains("Player1"));
		assertTrue(model.players().list().contains("Player2"));
	}

	@Test
	public void testModifications() {
		assertEquals(3, model.modifications().list().size());
	}
	
	@Test
	public void testChangeTypes() {
		assertEquals(3, model.changeTypes().list().size());
	}
	
	@Test
	public void testModificationObjects() {
		assertEquals(6, model.modificationObject().list().size());
	}
	
	@Test
	public void testSetId() {
		model.id().selected().set("Test");
		verify(action, times(1)).id("Test");
	}
	
	@Test
	public void testSetIdToEquipment() {
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Equipment");
		model.id().selected().set("Head");
		verify(action, times(1)).id(head);
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(action, model.getPersistenceObject());
	}
	
	@Test
	public void testIsChangeWhenSelectedIsNull() {
		assertFalse(model.isChange());
	}
	
	@Test
	public void testIsChangeWhenSelectedIsNotChange() {
		model.modifications().selected().set("Add");
		assertFalse(model.isChange());
	}
	
	@Test
	public void testIsChangeWhenSelectedIsChange() {
		model.modifications().selected().set("Change");
		assertTrue(model.isChange());
	}
	
	@Test
	public void testIsAddWhenSelectedIsNull() {
		assertFalse(model.isAdd());
	}
	
	@Test
	public void testIsAddWhenSelectedIsNotAdd() {
		model.modifications().selected().set("Change");
		assertFalse(model.isAdd());
	}
	
	@Test
	public void testIsAddWhenSelectedIsAdd() {
		model.modifications().selected().set("Add");
		assertTrue(model.isAdd());
	}
	
	@Test
	public void testIsRemoveWhenSelectedIsNull() {
		assertFalse(model.isRemove());
	}
	
	@Test
	public void testIsRemoveWhenSelectedIsNotRemove() {
		model.modifications().selected().set("Change");
		assertFalse(model.isRemove());
	}
	
	@Test
	public void testIsRemoveWhenSelectedIsRemove() {
		model.modifications().selected().set("Remove");
		assertTrue(model.isRemove());
	}
	
	@Test
	public void testNeedsDataMember() {
		assertFalse(model.needsDataMember());
	}
	
	@Test
	public void testIsBodyPartModificationWhenSelectedIsNull() {
		assertFalse(model.isBodyModification());
	}
	
	@Test
	public void testIsBodyPartModificationWhenSelectedIsNotBodyPart() {
		model.modificationObject().selected().set("Attribute");
		assertFalse(model.isBodyModification());
	}
	
	@Test
	public void testIsBodyPartModifictionWhenSelectedIsBodyPart() {
		model.modificationObject().selected().set("BodyPart");
		assertTrue(model.isBodyModification());
	}
	
	@Test
	public void testIsEquipmentModificationWhenSelectedIsNull() {
		assertFalse(model.isEquipmentModification());
	}
	
	@Test
	public void testIsEquipmentModificationWhenSelectedIsNotEquipment() {
		model.modificationObject().selected().set("BodyPart");
		assertFalse(model.isEquipmentModification());
	}
	
	@Test
	public void testIsEquipmentModificationWhenSelectedIsEquipment() {
		model.modificationObject().selected().set("Equipment");
		assertTrue(model.isEquipmentModification());
	}
	
	@Test
	public void testIsAssignWhenSelectedIsNull() {
		assertFalse(model.isAssign());
	}
	
	@Test
	public void testIsAssignWhenSelectedIsNotAssign() {
		model.changeTypes().selected().set("Add");
		assertFalse(model.isAssign());
	}
	
	@Test
	public void testIsAssignWhenSelectedIsAssign() {
		model.changeTypes().selected().set("Assign");
		assertTrue(model.isAssign());
	}
	
	@Test
	public void testIsSubtractWhenSelectedIsNull() {
		assertFalse(model.isSubtract());
	}
	
	@Test
	public void testIsSubtractWhenSelectedIsNotSubtract() {
		model.changeTypes().selected().set("Add");
		assertFalse(model.isSubtract());
	}
	
	@Test
	public void testIsSubtractWhenSelectedIsSubtract() {
		model.changeTypes().selected().set("Subtract");
		assertTrue(model.isSubtract());
	}
	
	@Test
	public void testIsAddChangeWhenSelectedIsNull() {
		assertFalse(model.isAdd());
	}
	
	@Test
	public void testIsAddChangeWhenSelectedIsNotAdd() {
		model.changeTypes().selected().set("Subtract");
		assertFalse(model.isAdd());
	}
	
	@Test
	public void testIsAddChangeWhenSelectedIsAdd() {
		model.changeTypes().selected().set("Add");
		assertFalse(model.isAdd());
	}
	
	@Test
	public void testGetCurrentBodyPartWhenNotBodyPart() {
		assertNull(model.getCurrentBodyPart());
	}
	
	@Test
	public void testGetCurrentBodyPartWithNoSelectedPlayer() {
		model.modificationObject().selected().set("BodyPart");
		assertNull(model.getCurrentBodyPart());
	}
	
	@Test
	public void testGetCurrentBodyPartWithPlayer() {
		model.modificationObject().selected().set("BodyPart");
		model.players().selected().set("Player1");
		assertNull(model.getCurrentBodyPart());
	}
	
	@Test
	public void testGetCurrentBodyPartWithPlayerAndId() {
		model.modificationObject().selected().set("BodyPart");
		model.players().selected().set("Player1");
		model.id().selected().set("Head");
		assertEquals(head, model.getCurrentBodyPart());
	}
	
	@Test
	public void testGetCurrentInventoryWithoutEquipment() {
		assertNull(model.getCurrentInventory());
	}
	
	@Test
	public void testGetCurrentInventoryWithoutPlayer() {
		model.modificationObject().selected().set("Equipment");
		assertNull(model.getCurrentInventory());
	}
	
	@Test
	public void testGetCurrentInventoryWithPlayer() {
		model.modificationObject().selected().set("Equipment");
		when(action.playerName()).thenReturn("Player1");
		
		assertNotNull(model.getCurrentInventory());
	}
	
	@Test
	public void testPlayerText() {
		assertEquals(model.playerText().get(), "Player");
	}
	
	@Test
	public void testPropertyText() {
		assertEquals(model.propertyText().get(), "Property");
	}
	
	@Test
	public void testIdText() {
		assertEquals(model.idText().get(), "Id");
	}
	
	@Test
	public void testDataMemberText() {
		assertEquals(model.dataMemberText().get(), "Data Member");
	}
	
	@Test
	public void testModTypeText() {
		assertEquals(model.modTypeText().get(), "Modification Type");
	}
	
	@Test
	public void testChangeTypeText() {
		assertEquals(model.changeTypeText().get(), "Change Type");
	}
	
	@Test
	public void testModDataText() {
		assertEquals(model.modDataText().get(), "Modification Data");
	}
	
	@Test
	public void testIdUpdateToAttribute() {
		AttributePersistenceObject att = mock(AttributePersistenceObject.class);
		when(att.objectName()).thenReturn("Age");
		when(player1.attributes()).thenReturn(Arrays.asList(att));
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Attribute");
		assertTrue(model.id().list().contains("Age"));
	}
	
	@Test
	public void testIdUpdateToBodyPart() {
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("BodyPart");
		assertTrue(model.id().list().contains("Head"));
	}
	
	@Test
	public void testIdUpdateToCharacteristic() {
		CharacteristicPersistenceObject chr = mock(CharacteristicPersistenceObject.class);
		when(chr.objectName()).thenReturn("HairColor");
		when(player1.characteristics()).thenReturn(Arrays.asList(chr));
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Characteristic");
		assertTrue(model.id().list().contains("HairColor"));
	}
	
	@Test
	public void testIdUpdateToEquipment() {
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Equipment");
		assertTrue(model.id().list().contains("Head"));
	}
	
	@Test
	public void testIdUpdateToInventory() {
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Inventory");
		assertTrue(model.id().list().contains("Potion"));
	}
	
	@Test
	public void testIdUpdateToPlayer() {
		model.players().selected().set("Player1");
		model.modificationObject().selected().set("Player");
		assertTrue(model.id().list().isEmpty());
	}
	
	@Test
	public void testUpdateModificationDataAttribute() {
		when(action.modificationObj()).thenReturn(ModificationObject.Attribute);
		model.modificationData().set("Age");
		verify(action, times(1)).data("Age");
	}
	
	@Test
	public void testUpdateModificationDataBodyPart() {
		when(action.modificationObj()).thenReturn(ModificationObject.BodyPart);
		model.modificationData().set("Head");
		verify(action, times(1)).data("Head");
	}
	
	@Test
	public void testUpdateModificationDataCharacteristic() {
		when(action.modificationObj()).thenReturn(ModificationObject.Characteristic);
		model.modificationData().set("HairColor");
		verify(action, times(1)).data("HairColor");
	}
	
	@Test
	public void testUpdateModificationDataEquipment() {
		when(action.modificationObj()).thenReturn(ModificationObject.Equipment);
		model.modificationData().set("Potion");
		verify(action, times(1)).data("Potion");
	}
	
	@Test
	public void testUpdateModificationDataInventory() {
		InventoryItem mItem = mock(InventoryItem.class);
		when(mItem.amount()).thenReturn(new SimpleIntegerProperty(12));
		when(mItem.getAmount()).thenReturn(12);
		when(mItem.item()).thenReturn(new SimpleObjectProperty<>(item));
		when(mItem.getItem()).thenReturn(item);
		when(action.modificationObj()).thenReturn(ModificationObject.Inventory);
		model.modificationData().set(mItem);
		
		verify(action, times(1)).data(12);
		verify(action, times(1)).id(any(ItemPersistenceObject.class));
	}
	
	@Test
	public void testUpdateModificationDataPlayer() {
		when(action.modificationObj()).thenReturn(ModificationObject.Player);
		model.players().selected().set("Player1");
		model.modificationData().set("Player1");
		verify(action, times(1)).data(player1);
	}
	
	@Test
	public void testSelectModification() {
		model.modifications().selected().set("Add");
		verify(action, times(1)).modificationType(ModificationType.Add);
		assertEquals(model.modifications().selected().get(), "Add");
	}
	
	@Test
	public void testSelectChangeType() {
		model.changeTypes().selected().set("Subtract");
		verify(action, times(1)).changeType(ChangeType.Subtract);
		assertEquals(model.changeTypes().selected().get(), "Subtract");
	}
	
	@Test
	public void testChangeToRemoveAttribute() {
		AttributePersistenceObject age = mock(AttributePersistenceObject.class);
		when(age.objectName()).thenReturn("Age");
		when(player1.attributes()).thenReturn(Arrays.asList(age));
		when(action.modificationObj()).thenReturn(ModificationObject.Attribute);
		model.players().selected().set("Player1");
		model.id().selected().set("Age");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(age);
	}
	
	@Test
	public void testChangeToRemoveBodyPart() {
		when(action.modificationObj()).thenReturn(ModificationObject.BodyPart);
		model.players().selected().set("Player1");
		model.id().selected().set("Head");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(head);
	}
	
	@Test
	public void testChangeToRemoveCharacteristic() {
		CharacteristicPersistenceObject hc = mock(CharacteristicPersistenceObject.class);
		when(hc.objectName()).thenReturn("HairColor");
		when(player1.characteristics()).thenReturn(Arrays.asList(hc));
		when(action.modificationObj()).thenReturn(ModificationObject.Characteristic);
		model.players().selected().set("Player1");
		model.id().selected().set("HairColor");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(hc);
	}
	
	@Test
	public void testChangeToRemoveEquipment() {
		when(action.modificationObj()).thenReturn(ModificationObject.Equipment);
		model.players().selected().set("Player1");
		model.id().selected().set("Potion");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(item);
	}
	
	@Test
	public void testChangeToRemoveInventory() {
		when(action.modificationObj()).thenReturn(ModificationObject.Inventory);
		model.players().selected().set("Player1");
		model.id().selected().set("Potion");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(any(ItemPersistenceObject.class));
	}
	
	@Test
	public void testChangeToRemovePlayer() {
		when(action.modificationObj()).thenReturn(ModificationObject.Player);
		model.players().selected().set("Player1");
		model.modifications().selected().set("Remove");
		verify(action, times(1)).data(player1);
	}
}
