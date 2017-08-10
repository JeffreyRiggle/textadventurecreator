package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.shell.IProviderListener;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;

public class TextAdventureProviderUnitTest {

	private TextAdventureProjectPersistence tav1;
	private TextAdventureProjectPersistence tav2;
	
	private TextAdventureProvider provider;
	
	@Before
	public void setup() {
		tav1 = mock(TextAdventureProjectPersistence.class);
		tav2 = mock(TextAdventureProjectPersistence.class);
		
		provider = new TextAdventureProvider();
	}
	
	@Test
	public void testAddListener() {
		Listener listen = new Listener();
		provider.addListener(listen);
		
		provider.setTextAdventure(tav1);
		assertTrue(listen.ran());
		assertEquals(tav1, listen.tav());
	}

	@Test
	public void testRemoveListener() {
		Listener listen = new Listener();
		provider.addListener(listen);
		provider.removeListener(listen);
		
		provider.setTextAdventure(tav1);
		assertFalse(listen.ran());
		assertNull(listen.tav());
	}
	
	@Test
	public void testGetAdventure() {
		provider.setTextAdventure(tav1);
		assertEquals(tav1, provider.getTextAdventureProject());
		
		provider.setTextAdventure(tav2);
		assertEquals(tav2, provider.getTextAdventureProject());
	}
	
	private class Listener implements IProviderListener {
		
		private boolean ran;
		private TextAdventureProjectPersistence tav;
		
		public Listener() {
			ran = false;
		}

		@Override
		public void provided(TextAdventureProjectPersistence textAdventure) {
			ran = true;
			tav = textAdventure;
		}
		
		public boolean ran() {
			return ran;
		}
		
		public TextAdventureProjectPersistence tav() {
			return tav;
		}
	}
}
