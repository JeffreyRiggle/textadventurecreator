package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.shell.LayoutApplicationService;

public class LayoutApplicationServiceUnitTest {

	private LayoutApplicationService service;
	
	@Before
	public void setup() {
		service = new LayoutApplicationService();
	}
	
	@Test
	public void testAddListener() {
		LayoutListener listener = new LayoutListener();
		service.addListener(listener);
		assertFalse(listener.ran());
		service.applyLayout();
		assertTrue(listener.ran());
	}

	@Test
	public void testRemoveListener() {
		LayoutListener listener = new LayoutListener();
		service.addListener(listener);
		service.removeListener(listener);
		assertFalse(listener.ran());
		service.applyLayout();
		assertFalse(listener.ran());
	}
	
	private class LayoutListener implements Runnable {

		private boolean ran;
		
		public LayoutListener() {
			ran = false;
		}
		
		@Override
		public void run() {
			ran = true;
		}
		
		public boolean ran() {
			return ran;
		}
	}
}
