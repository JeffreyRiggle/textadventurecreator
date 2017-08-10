package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.toolbars.GameStateAwareButton;

public class GameStateAwareButtonUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider;
	
	@Test
	public void testText() {
		provider = mock(TextAdventureProvider.class);
		TestButton btn = new TestButton(provider, "Test");
		assertEquals("Test", btn.getText());
	}
	
	@Test
	public void testCreateWithNoTextAdventure() {
		provider = mock(TextAdventureProvider.class);
		TestButton btn = new TestButton(provider, "Test");
		assertTrue(btn.disabledProperty().get());
	}

	@Test
	public void testCreateWithTextAdventure() {
		provider = mock(TextAdventureProvider.class);
		when(provider.getTextAdventureProject()).thenReturn(mock(TextAdventureProjectPersistence.class));
		TestButton btn = new TestButton(provider, "Test");
		assertFalse(btn.disabledProperty().get());
	}
	
	@Test
	public void testChangeToHavingTextAdventure() {
		provider = mock(TextAdventureProvider.class);
		TestButton btn = new TestButton(provider, "Test");
		assertTrue(btn.disabledProperty().get());
		btn.provided(mock(TextAdventureProjectPersistence.class));
		assertFalse(btn.disabledProperty().get());
	}
	
	@Test
	public void testChangeToNotHavingTextAdventure() {
		provider = mock(TextAdventureProvider.class);
		when(provider.getTextAdventureProject()).thenReturn(mock(TextAdventureProjectPersistence.class));
		TestButton btn = new TestButton(provider, "Test");
		assertFalse(btn.disabledProperty().get());
		btn.provided(null);
		assertTrue(btn.disabledProperty().get());
	}
	
	private class TestButton extends GameStateAwareButton {

		public TestButton(TextAdventureProvider provider, String itemName) {
			super(provider, itemName);
		}
	}
}
