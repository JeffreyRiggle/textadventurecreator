package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.views.player.InventoryItem;
import textadventurelib.persistence.player.ItemPersistenceObject;

public class InventoryItemUnitTest {

	private ItemPersistenceObject itm;
	private int amount;
	
	private InventoryItem item;
	
	@Before
	public void setup() {
		itm = mock(ItemPersistenceObject.class);
		when(itm.itemName()).thenReturn("TestItem");
		amount = 3;
		
		item = new InventoryItem(itm, amount);
	}
	
	@Test
	public void testItem() {
		assertEquals(itm, item.getItem());
		assertEquals(itm, item.item().get());
		ItemPersistenceObject itm2 = mock(ItemPersistenceObject.class);
		item.setItem(itm2);
		assertEquals(itm2, item.getItem());
		assertEquals(itm2, item.item().get());
	}

	@Test
	public void testAmount() {
		assertEquals(amount, item.amount().get());
		assertEquals(amount, item.getAmount());
		item.setAmount(5);
		assertEquals(5, item.amount().get());
		assertEquals(5, item.getAmount());
	}
	
	@Test
	public void testValid() {
		ItemPersistenceObject itm2 = mock(ItemPersistenceObject.class);
		assertTrue(item.valid().get());
		item.setItem(itm2);
		assertFalse(item.valid().get());
	}
}
