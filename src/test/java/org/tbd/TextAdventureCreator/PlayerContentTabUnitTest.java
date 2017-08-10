package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.player.PlayerContentTab;
import ilusr.textadventurecreator.views.player.PlayerModel;
import ilusr.textadventurecreator.views.player.PlayerView;
import javafx.beans.property.SimpleStringProperty;

public class PlayerContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private PlayerView view;
	private PlayerModel model;
	
	private SimpleStringProperty id;
	
	private PlayerContentTab tab;
	
	@Before
	public void setup() {
		id = new SimpleStringProperty();
		model = mock(PlayerModel.class);
		when(model.playerID()).thenReturn(id);
		
		view = mock(PlayerView.class);
		when(view.model()).thenReturn(model);
		
		tab = new PlayerContentTab(view);
	}
	
	@Test
	public void testClose() {
		assertTrue(tab.canClose().get());
	}

	@Test
	public void testToolTip() {
		assertEquals("Information about player", tab.toolTip().get());
	}
	
	@Test
	public void testCustomData() {
		assertNull(tab.customData().get());
	}
	
	@Test
	public void testPlayerIdChange() {
		id.set("Player1");
		assertEquals("Information about player Player1", tab.toolTip().get());
		assertEquals("PlayerName: Player1", tab.customData().get());
	}
}
