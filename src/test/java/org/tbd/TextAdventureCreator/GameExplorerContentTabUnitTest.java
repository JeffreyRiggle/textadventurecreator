package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.GameExplorerContentTab;
import ilusr.textadventurecreator.views.GameExplorerView;

public class GameExplorerContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameExplorerView view;
	private ILanguageService languageService;
	
	private GameExplorerContentTab tab;
	
	@Before
	public void setup() {
		view = mock(GameExplorerView.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.EXPLORER)).thenReturn("Explorer");
		when(languageService.getValue(DisplayStrings.GAME_EXPLORER)).thenReturn("Game Explorer");
		
		tab = new GameExplorerContentTab(view, languageService);
	}
	
	@Test
	public void testContent() {
		assertEquals(view, tab.content().get());
	}

	@Test
	public void testToolTip() {
		assertEquals("Game Explorer", tab.toolTip().get());
	}
	
	@Test
	public void testCanClose() {
		assertTrue(tab.canClose().get());
	}
}
