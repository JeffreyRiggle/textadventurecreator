package ilusr.textadventurecreator.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import playerlib.player.IPlayer;
import textadventurelib.core.ICompletionListener;
import textadventurelib.core.ITextAdventureGameStateManager;
import textadventurelib.gamestates.GameStateCompletionData;
import textadventurelib.gamestates.GameStateRuntimeData;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameDebuggerModel implements ICompletionListener {

	private String statePrefix;
	private final ITextAdventureGameStateManager gameStateManager;
	private final ILanguageService languageService;
	private final DebugPlayerFactory playerFactory;
	
	private SimpleStringProperty currentGameState;
	private List<PlayerDebugModel> players;
	
	/**
	 * 
	 * @param gameStateManager A @see ITextAdventureGameStateManager to use for this model.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param playerFactory A @see DebugPlayerFactory to create players.
	 */
	public GameDebuggerModel(ITextAdventureGameStateManager gameStateManager, 
							 ILanguageService languageService,
							 DebugPlayerFactory playerFactory) {
		this.gameStateManager = gameStateManager;
		this.languageService = languageService;
		this.playerFactory = playerFactory;
		
		statePrefix = languageService.getValue(DisplayStrings.GAMESTATE_PREFIX);
		
		currentGameState = new SimpleStringProperty(statePrefix);
		players = new ArrayList<PlayerDebugModel>();
		
		initialize();
	}
	
	private void initialize() {
		if (gameStateManager.runtimeData() instanceof GameStateRuntimeData) {
			GameStateRuntimeData data = (GameStateRuntimeData)gameStateManager.runtimeData();
			currentGameState.set(statePrefix + data.currentGameState());
		}
		
		gameStateManager.addCompletionListener(this);
		
		for (IPlayer player : gameStateManager.players()) {
			LogRunner.logger().log(Level.INFO, String.format("Adding Player: %s", player.name()));
			players.add(playerFactory.create(player));
		}
		
		languageService.addListener(() -> {
			statePrefix = languageService.getValue(DisplayStrings.GAMESTATE_PREFIX);
		});
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the current game state.
	 */
	public SimpleStringProperty currentGameState() {
		return currentGameState;
	}

	/**
	 * 
	 * @return A @see List of type @see PlayerDebugModel representing the players in this game.
	 */
	public List<PlayerDebugModel> players() {
		return players;
	}
	
	@Override
	public <T> void completed(T data) {		
		Platform.runLater(() -> {
			for (PlayerDebugModel player : players) {
				LogRunner.logger().log(Level.INFO, String.format("Reseting Notifications for player: %s", player.player().name()));
				player.resetNotifications();
			}
			
			LogRunner.logger().log(Level.INFO, String.format("Game State completed with data: %s", ((GameStateCompletionData)data).completionData().toString()));
			currentGameState.set(statePrefix + ((GameStateCompletionData)data).completionData());
		});
	}
}
