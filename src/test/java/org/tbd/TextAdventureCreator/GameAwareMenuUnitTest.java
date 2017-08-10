package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.menus.GameAwareMenuItem;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;

public class GameAwareMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private String itemName = "Item";
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
	}
	
	@Test
	public void testName() {
		GameAwareMenuItem item = new TestMenu(provider, itemName);
		assertEquals("Item", item.textProperty().get());
	}

	@Test
	public void testInitNullProvider() {
		when(provider.getTextAdventureProject()).thenReturn(null);
		GameAwareMenuItem item = new TestMenu(provider, itemName);
		assertTrue(item.disableProperty().get());
	}
	
	@Test
	public void testInitProvider() {
		when(provider.getTextAdventureProject()).thenReturn(mock(TextAdventureProjectPersistence.class));
		GameAwareMenuItem item = new TestMenu(provider, itemName);
		assertFalse(item.disableProperty().get());
	}
	
	@Test
	public void testChangeNull() {
		when(provider.getTextAdventureProject()).thenReturn(mock(TextAdventureProjectPersistence.class));
		GameAwareMenuItem item = new TestMenu(provider, itemName);
		item.provided(null);
		assertTrue(item.disableProperty().get());
	}
	
	@Test
	public void testChange() {
		when(provider.getTextAdventureProject()).thenReturn(null);
		GameAwareMenuItem item = new TestMenu(provider, itemName);
		item.provided(mock(TextAdventureProjectPersistence.class));
		assertFalse(item.disableProperty().get());
	}
	
	private class TestMenu extends GameAwareMenuItem {

		public TestMenu(TextAdventureProvider provider, String itemName) {
			super(provider, itemName);
		}
	}
}
