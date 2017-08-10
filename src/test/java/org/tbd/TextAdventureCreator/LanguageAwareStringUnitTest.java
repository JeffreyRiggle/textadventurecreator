package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;

public class LanguageAwareStringUnitTest {

	private ILanguageService languageService;
	private String key = "Test";
	
	private ArgumentCaptor<Runnable> cb;
	
	private LanguageAwareString str;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue("Test")).thenReturn("Test");
		
		str = new LanguageAwareString(languageService, key);
	}
	
	@Test
	public void testInitialText() {
		assertEquals("Test", str.get());
	}

	@Test
	public void testLanguageChange() {
		cb = ArgumentCaptor.forClass(Runnable.class);
		verify(languageService, times(1)).addListener(cb.capture());
		
		when(languageService.getValue("Test")).thenReturn("テスト");
		cb.getValue().run();
		assertEquals("テスト", str.get());
	}
}
