package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LandingPageContentTab;
import ilusr.textadventurecreator.views.LandingPageView;

public class LandingPageContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private LandingPageView view;
	private ILanguageService languageService;
	
	private LandingPageContentTab tab;
	
	@Before
	public void setup() {
		view = mock(LandingPageView.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.GREETINGS)).thenReturn("Greetings");
		when(languageService.getValue(DisplayStrings.WELCOMETOOLTIP)).thenReturn("Welcome");
		
		tab = new LandingPageContentTab(view, languageService);
	}
	
	@Test
	public void testContent() {
		assertEquals(view, tab.content().get());
	}

	@Test
	public void testToolTip() {
		assertEquals("Welcome", tab.toolTip().get());
	}
	
	@Test
	public void testCanClose() {
		assertTrue(tab.canClose().get());
	}
}
