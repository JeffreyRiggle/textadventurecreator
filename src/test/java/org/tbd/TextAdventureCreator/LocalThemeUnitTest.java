package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ilusr.core.io.DirectoryWatcher;
import ilusr.textadventurecreator.style.LocalTheme;

public class LocalThemeUnitTest {

	private String name;
	private DirectoryWatcher watcher;
	private LocalTheme theme;
	
	@Before
	public void setup() {
		name = "TestTheme";
		watcher = mock(DirectoryWatcher.class);
		when(watcher.watchedDirectory()).thenReturn("c:/test");
		
		try {
			theme = new LocalTheme(name, watcher);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testName() {
		assertEquals(name, theme.getName());
	}
}
