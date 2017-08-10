package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.textadventurecreator.debug.PlayerDebugModel;
import ilusr.textadventurecreator.debug.PlayersDebugModel;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;

public class DebugPlayersUnitTest {

	private ILanguageService languageService;
	private PlayerDebugModel player1;
	private PlayerDebugModel player2;
	
	private PlayersDebugModel model;
	
	@Before
	public void setup() {
		languageService = Mockito.mock(ILanguageService.class);
		Mockito.when(languageService.getValue(DisplayStrings.PLAYERS)).thenReturn("Players");
		Mockito.when(languageService.getValue(DisplayStrings.NO_PLAYERS)).thenReturn("No Players");
		player1 = Mockito.mock(PlayerDebugModel.class);
		player2 = Mockito.mock(PlayerDebugModel.class);
		
		model = new PlayersDebugModel(Arrays.asList(player1, player2), languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}

	@Test
	public void testPlayers() {
		assertEquals(2, model.players().size());
	}
	
	@Test
	public void testPlayersText() {
		assertEquals("Players", model.playersText().get());
	}
	
	@Test
	public void testNoPlayersText() {
		assertEquals("No Players", model.noPlayersText().get());
	}
}
