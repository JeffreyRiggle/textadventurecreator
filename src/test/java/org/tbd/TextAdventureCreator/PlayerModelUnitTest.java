package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.InventoryItem;
import ilusr.textadventurecreator.views.player.PlayerModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.EquipPersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService; 
	private LibraryService libraryService; 
	private PlayerPersistenceObject player;
	private EquipmentPersistenceObject equipment;
	private InventoryPersistenceObject inventory;
	private ILanguageService languageService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private PlayerModel model;
	
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		libraryService = mock(LibraryService.class);
		player = mock(PlayerPersistenceObject.class);
		inventory = mock(InventoryPersistenceObject.class);
		when(player.inventory()).thenReturn(inventory);
		equipment = mock(EquipmentPersistenceObject.class);
		when(player.equipment()).thenReturn(equipment);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.PLAYER_ID)).thenReturn("Player ID");
		when(languageService.getValue(DisplayStrings.ATTRIBUTES)).thenReturn("Attributes");
		when(languageService.getValue(DisplayStrings.CHARACTERISTICS)).thenReturn("Characteristics");
		when(languageService.getValue(DisplayStrings.BODY_PARTS)).thenReturn("Body Parts");
		when(languageService.getValue(DisplayStrings.BODY_PART)).thenReturn("Body Part");
		when(languageService.getValue(DisplayStrings.INVENTORY)).thenReturn("Inventory");
		when(languageService.getValue(DisplayStrings.EQUIPMENT)).thenReturn("Equipment");
		when(languageService.getValue(DisplayStrings.ITEM)).thenReturn("Item");
		
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new PlayerModel(dialogService, libraryService, player, languageService, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testAllowLibraryAddWithLibrary() {
		assertTrue(model.allowLibraryAdd());
	}

	@Test
	public void testAllowLibraryAddWithoutLibrary() {
		PlayerModel bmodel = new PlayerModel(dialogService, null, player, languageService, dialogProvider, styleService, urlProvider);
		assertFalse(bmodel.allowLibraryAdd());
	}
	
	@Test
	public void testPersistablePlayer() {
		assertEquals(player, model.persistablePlayer());
	}
	
	@Test
	public void testPlayerId() {
		assertNull(model.playerID().get());
		model.playerID().set("Player1");
		assertEquals("Player1", model.playerID().get());
		verify(player, times(1)).playerName("Player1");
	}
	
	@Test
	public void testValid() {
		assertFalse(model.valid().get());
		model.playerID().set("Player1");
		assertTrue(model.valid().get());
	}
	
	@Test
	public void testPlayerText() {
		assertEquals("Player ID", model.playerText().get());
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
		assertEquals("Inventory", model.inventoryText().get());
	}
	
	@Test
	public void testEquipmentText() {
		assertEquals("Equipment", model.equipmentText().get());
	}
	
	@Test
	public void testAddAttributeKey() {
		assertNotNull(model.addAttributeKey());
	}
	
	@Test
	public void testAddAttribute() {
		model.addAttribute();
		assertEquals(1, model.attributes().size());
		verify(player, times(1)).addAttribute(any());
	}
	
	@Test
	public void testRemoveAttribute() {
		ArgumentCaptor<AttributePersistenceObject> captor = ArgumentCaptor.forClass(AttributePersistenceObject.class);
		model.addAttribute();
		verify(player, times(1)).addAttribute(captor.capture());
		model.attributes().remove(0);
		assertEquals(0, model.attributes().size());
		verify(player, times(1)).removeAttribute(captor.getValue());
	}
	
	@Test
	public void testAddAttributeFromLibrary() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		when(dialogProvider.create(any(), any())).thenReturn(mock(Dialog.class));
		
		model.addAttributeFromLibrary();
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
		
		captor.getValue().run();
	}
	
	@Test
	public void testAddCharacteristicKey() {
		assertNotNull(model.addCharacteristicKey());
	}
	
	@Test
	public void testAddCharacteristic() {
		model.addCharacteristic();
		assertEquals(1, model.characteristics().size());
		verify(player, times(1)).addCharacteristic(any());
	}
	
	@Test
	public void testRemoveCharacteristic() {
		ArgumentCaptor<CharacteristicPersistenceObject> captor = ArgumentCaptor.forClass(CharacteristicPersistenceObject.class);
		model.addCharacteristic();
		verify(player, times(1)).addCharacteristic(captor.capture());
		model.characteristics().remove(0);
		assertEquals(0, model.attributes().size());
		verify(player, times(1)).removeCharacteristic(captor.getValue());
	}
	
	@Test
	public void testAddCharacteristicFromLibrary() {
		model.addCharacteristicFromLibrary();
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testAddBodyPartKey() {
		assertNotNull(model.addBodyPartKey());
	}
	
	@Test
	public void testAddBodyPart() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		model.addBodyPart();
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Body Part"));
		
		captor.getValue().run();
		
		assertEquals(1, model.bodyParts().size());
		verify(player, times(1)).addBodyPart(any());
	}
	
	@Test
	public void testRemoveBodyPart() {
		BodyPartPersistenceObject bPart = mock(BodyPartPersistenceObject.class);
		model.bodyParts().add(bPart);
		model.bodyParts().remove(bPart);
		verify(player, times(1)).removeBodyPart(bPart);
	}
	
	@Test
	public void testAddBodyPartFromLibrary() {
		model.addBodyPartFromLibrary();
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testEditBodyPart() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		BodyPartPersistenceObject bPart = mock(BodyPartPersistenceObject.class);
		model.getEditBodyPartAction().execute(bPart);
		
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Body Part"));
	}
	
	@Test
	public void testAddEquipmentKey() {
		assertNotNull(model.addEquipKey());
	}
	
	@Test
	public void testAddEquipment() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		model.getAddEquipmentAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Equipment"));
		
		captor.getValue().run();
		assertEquals(1, model.equipment().size());
		verify(equipment, times(1)).equip(any(), any());
	}
	
	@Test
	public void testRemoveEquipment() {
		BodyPartPersistenceObject bPart = mock(BodyPartPersistenceObject.class);
		EquipPersistenceObject equip = mock(EquipPersistenceObject.class);
		when(equip.bodyPart()).thenReturn(bPart);
		model.equipment().add(equip);
		model.equipment().remove(equip);
		verify(equipment, times(1)).unequip(bPart);
	}
	
	@Test
	public void testEditEquipment() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		EquipPersistenceObject equip = mock(EquipPersistenceObject.class);
		ItemPersistenceObject item = mock(ItemPersistenceObject.class);
		when(item.itemName()).thenReturn("TestItem");
		when(equip.item()).thenReturn(item);
		BodyPartPersistenceObject bpart = mock(BodyPartPersistenceObject.class);
		when(bpart.objectName()).thenReturn("TestPart");
		when(equip.bodyPart()).thenReturn(bpart);
		
		model.getEditEquipmentAction().execute(equip);
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Equipment"));
	}
	
	@Test
	public void testAddItemKey() {
		assertNotNull(model.addItemKey());
	}
	
	@Test
	public void testAddItem() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		model.addItem();
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Item"));
		
		captor.getValue().run();
		assertEquals(1, model.items().size());
		verify(inventory, times(1)).addItem(any(), eq(0));
	}
	
	@Test
	public void testRemoveItem() {
		InventoryItem item = mock(InventoryItem.class);
		ItemPersistenceObject pItem = mock(ItemPersistenceObject.class);
		when(item.getItem()).thenReturn(pItem);
		model.items().add(item);
		model.items().remove(item);
		verify(inventory, times(1)).removeItem(pItem);
	}
	
	@Test
	public void testEditItem() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		InventoryItem item = mock(InventoryItem.class);
		ItemPersistenceObject pItem = mock(ItemPersistenceObject.class);
		when(item.getItem()).thenReturn(pItem);
		when(item.valid()).thenReturn(new SimpleBooleanProperty());
		
		model.getEditInventoryAction().execute(item);
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Item"));
	}
}
