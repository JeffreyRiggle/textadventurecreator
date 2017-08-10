package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.style.ILocalThemeListener;
import ilusr.textadventurecreator.style.Theme;

public class ThemeUnitTest {

	private String name;
	
	private Theme theme;
	
	@Before
	public void setup() {
		name = "TestTheme";
		
		theme = new Theme(name);
	}
	
	@Test
	public void testName() {
		assertEquals(name, theme.getName());
	}

	@Test
	public void testStyles() {
		assertNotNull(theme.styles());
		theme.styles().put("TestStyle", mock(File.class));
		assertEquals(1, theme.styles().entrySet().size());
	}
	
	@Test
	public void testAddListener() {
		TListener listener = new TListener();
		theme.addListener(listener);
		theme.update("Test", mock(File.class));
		try {
			Thread.sleep(750);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(1, listener.times());
	}
	
	@Test
	public void testRemoveListener() {
		TListener listener = new TListener();
		theme.addListener(listener);
		theme.removeListener(listener);
		theme.update("Test", mock(File.class));
		try {
			Thread.sleep(750);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(0, listener.times());
	}
	
	@Test
	public void testRapidUpdates() {
		TListener listener = new TListener();
		theme.addListener(listener);
		theme.update("Test", mock(File.class));
		theme.update("Test", mock(File.class));
		theme.update("Test", mock(File.class));
		theme.update("Test", mock(File.class));
		try {
			Thread.sleep(750);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(1, listener.times());
	}
	
	private class TListener implements ILocalThemeListener {

		private int times;
		
		public TListener() {
			times = 0;
		}
		
		public int times() {
			return times;
		}
		
		@Override
		public void updated(String style, File file) {
			times++;
		}
		
	}
}
