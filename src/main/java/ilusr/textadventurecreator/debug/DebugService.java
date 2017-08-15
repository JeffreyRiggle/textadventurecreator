package ilusr.textadventurecreator.debug;

import java.util.ArrayList;
import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.gamestatemanager.GameStateManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import textadventurelib.core.ITextAdventureGameStateManager;
import textadventurelib.core.TextAdventureGameStateManager;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.TimerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugService implements IDebugService {

	private final IDialogService service;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private final DebugPlayerFactory playerFactory;
	
	/**
	 * 
	 * @param service A @see IDialogService used to display scenes.
	 * @param languageService A @see LanguageService used to create display strings.
	 * @param playerFactory A @see DebugPlayerFactory used to create players.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public DebugService(IDialogService service, 
						ILanguageService languageService,
						DebugPlayerFactory playerFactory,
						IStyleContainerService styleService,
						InternalURLProvider urlProvider) {
		this.service = service;
		this.languageService = languageService;
		this.playerFactory = playerFactory;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public void runGame(TextAdventurePersistenceObject game) {
		LogRunner.logger().info(String.format("Running Game: %s", game.gameName()));
		ITextAdventureGameStateManager tavGame = game.convertToGameStateManager();
		Stage stage = new Stage();
		AnchorPane debug = new AnchorPane();
		stage.setScene(new Scene(debug, 800, 600));
		tavGame.setStage(debug);
		service.displayModeless(stage);
		
		tavGame.addFinishListener(() -> {
			Platform.runLater(() -> {
				LogRunner.logger().info(String.format("Game: %s finished closing stage.", game.gameName()));
				stage.close();
			});
		});
		
		tavGame.start();
	}
	
	@Override
	public void debugGame(TextAdventurePersistenceObject game) {
		LogRunner.logger().info(String.format("Debugging Game: %s", game.gameName()));
		ITextAdventureGameStateManager tavGame = game.convertToGameStateManager();
		Stage stage = new Stage();
		GameDebuggerView debug = new GameDebuggerView(new GameDebuggerModel(tavGame, languageService, playerFactory), languageService, styleService, urlProvider);
		stage.setScene(new Scene(debug, 800, 600));
		tavGame.setStage(debug.gameArea());
		service.displayModeless(stage);
		
		tavGame.addFinishListener(() -> {
			Platform.runLater(() -> {
				LogRunner.logger().info(String.format("Debugged Game: %s, finished closing stage.", game.gameName()));
				stage.close();
			});
		});
		
		tavGame.start();
	}
	
	@Override
	public void debugGameState(GameStatePersistenceObject gameState) {
		LogRunner.logger().info(String.format("Debugging GameState: %s", gameState.stateId()));
		Stage stage = new Stage();
		AnchorPane pane = new AnchorPane();
		stage.setScene(new Scene(pane, 800, 600));
		
		TextAdventureGameStateManager manager = new TextAdventureGameStateManager(gameState.stateId(), gameState.convertToGameState(), pane);
		List<MockedGameState> mockedGameStates = getMocks(gameState, manager);
		for (MockedGameState state : mockedGameStates) {
			manager.addGameState(state.getId(), state);
		}
		
		manager.addFinishListener(() -> {
			Platform.runLater(() -> {
				LogRunner.logger().info(String.format("Finished debugging GameState: %s, closing stage.", gameState.stateId()));
				stage.close();
			});
		});
		
		manager.setStage(pane);
		service.displayModeless(stage);
		manager.start();
	}
	
	private List<MockedGameState> getMocks(GameStatePersistenceObject gameState, GameStateManager manager) {
		List<MockedGameState> retVal = new ArrayList<MockedGameState>();
		for (OptionPersistenceObject option : gameState.options()) {
			if (!(option.action() instanceof CompletionActionPersistence)) {
				continue;
			}
			
			CompletionActionPersistence action = (CompletionActionPersistence)option.action();
			retVal.add(new MockedGameState(action.<String>completionData(), manager, languageService));
		}
		
		for (TimerPersistenceObject timer : gameState.timers()) {
			if (!(timer instanceof CompletionTimerPersistenceObject)) {
				continue;
			}
			
			CompletionTimerPersistenceObject cTimer = (CompletionTimerPersistenceObject)timer;
			retVal.add(new MockedGameState(cTimer.<String>completionData(), manager, languageService));
		}
		
		return retVal;
	}
}
