package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.core.test.FXThread;
import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.debug.DebugPlayerFactory;
import ilusr.textadventurecreator.debug.GameDebuggerModel;
import ilusr.textadventurecreator.debug.PlayerDebugModel;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import playerlib.player.IPlayer;
import textadventurelib.core.ITextAdventureGameStateManager;
import textadventurelib.gamestates.GameStateCompletionData;
import textadventurelib.gamestates.GameStateRuntimeData;

public class GameDebuggerModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ITextAdventureGameStateManager gameStateManager;
	private ILanguageService languageService;
	private DebugPlayerFactory playerFactory;
	private GameStateRuntimeData runtimeData;
	
	private PlayerDebugModel player1;
	private PlayerDebugModel player2;
	
	private GameDebuggerModel model;
	
	@Before
	public void setup() {
		gameStateManager = Mockito.mock(ITextAdventureGameStateManager.class);
		
		IPlayer p1 = Mockito.mock(IPlayer.class);
		Mockito.when(p1.name()).thenReturn("player1");
		IPlayer p2 = Mockito.mock(IPlayer.class);
		Mockito.when(p2.name()).thenReturn("player2");
		Mockito.when(gameStateManager.players()).thenReturn(Arrays.asList(p1, p2));
		
		runtimeData = Mockito.mock(GameStateRuntimeData.class);
		Mockito.when(runtimeData.currentGameState()).thenReturn("GS1");
		Mockito.when(gameStateManager.runtimeData()).thenReturn(runtimeData);
		
		languageService = Mockito.mock(ILanguageService.class);
		Mockito.when(languageService.getValue(DisplayStrings.GAMESTATE_PREFIX)).thenReturn("GameState:");
		
		playerFactory = Mockito.mock(DebugPlayerFactory.class);
		player1 = Mockito.mock(PlayerDebugModel.class);
		Mockito.when(player1.player()).thenReturn(p1);
		player2 = Mockito.mock(PlayerDebugModel.class);
		Mockito.when(player2.player()).thenReturn(p2);
		Mockito.when(playerFactory.create(p1)).thenReturn(player1);
		Mockito.when(playerFactory.create(p2)).thenReturn(player2);
		
		model = new GameDebuggerModel(gameStateManager, languageService, playerFactory);
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
	public void testCurrentState() {
		assertEquals("GameState:GS1", model.currentGameState().get());
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testCompeletion() {
		GameStateCompletionData data = Mockito.mock(GameStateCompletionData.class);
		Mockito.when(data.completionData()).thenReturn("GS2");
		model.completed(data);
		
		waitForPlatform();
		assertEquals("GameState:GS2", model.currentGameState().get());
		Mockito.verify(player1, Mockito.times(1)).resetNotifications();
		Mockito.verify(player2, Mockito.times(1)).resetNotifications();
	}
	
	private void waitForPlatform() {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	latch.countDown();
        });
        
        try {
        	latch.await();
        } catch (Exception e) { }
	}
}
