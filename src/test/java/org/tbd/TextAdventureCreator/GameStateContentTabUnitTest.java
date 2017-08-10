package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.gamestate.GameStateContentTab;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.gamestate.GameStateView;
import javafx.beans.property.SimpleStringProperty;

public class GameStateContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameStateView view;
	private ILanguageService languageService;
	
	private GameStateContentTab tab;
	
	@Before
	public void setup() {
		GameStateModel model = mock(GameStateModel.class);
		when(model.stateId()).thenReturn(new SimpleStringProperty("GS1"));
		view = mock(GameStateView.class);
		when(view.model()).thenReturn(model);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.GAME_STATE_INFO)).thenReturn("Game state info");
		
		tab = new GameStateContentTab(view, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(tab);
	}

	@Test
	public void testToolTip() {
		assertEquals("Game state info GS1", tab.toolTip().get());
	}
	
	@Test
	public void testClose() {
		assertTrue(tab.canClose().get());
	}
	
	@Test
	public void testCustomData() {
		assertEquals("GameStateName: GS1", tab.customData().get());
	}
}
