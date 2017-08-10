package ilusr.textadventurecreator.settings;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameSettingsModel {
	
	private final TextAdventureProvider provider;
	private SimpleBooleanProperty gameLoaded;
	private SelectionAwareObservableList<String> firstGameState;
	private LanguageAwareString noSettingsText;
	private LanguageAwareString settingText;
	private LanguageAwareString startGameStateText;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public GameSettingsModel(TextAdventureProvider provider, ILanguageService languageService) {
		this.provider = provider;
		gameLoaded = new SimpleBooleanProperty();
		firstGameState = new SelectionAwareObservableList<String>();
		noSettingsText = new LanguageAwareString(languageService, DisplayStrings.NO_GAME_LOADED);
		settingText = new LanguageAwareString(languageService, DisplayStrings.GAME_SETTINGS);
		startGameStateText = new LanguageAwareString(languageService, DisplayStrings.START_GAME_STATE);
		
		initialize();
	}
	
	private void initialize() {
		provider.addListener((p) -> {
			applyProject();
		});
		
		firstGameState.selected().addListener((v, o, n) -> {
			if (n == null || n.isEmpty()) {
				return;
			}
			
			LogRunner.logger().log(Level.INFO, String.format("Updating first game state to %s", n));
			provider.getTextAdventureProject().getTextAdventure().currentGameState(n);
		});
		
		if (provider.getTextAdventureProject() != null && provider.getTextAdventureProject().getTextAdventure() != null) {
			applyProject();
		}
		
	}
	
	private void applyProject() {
		LogRunner.logger().log(Level.INFO, "Found game applying project");
		gameLoaded.set(true);
		TextAdventurePersistenceObject tav = provider.getTextAdventureProject().getTextAdventure();
		
		firstGameState.list().clear();
		firstGameState.selected().set(new String());
		
		for (GameStatePersistenceObject gameState : tav.gameStates()) {
			firstGameState.list().add(gameState.stateId());
		}
		
		LogRunner.logger().log(Level.INFO, String.format("Setting first game state to %s", tav.currentGameState()));
		firstGameState.selected().set(tav.currentGameState());
	}
	
	/**
	 * 
	 * @return If a game has been loaded or not
	 */
	public SimpleBooleanProperty gameLoaded() {
		return gameLoaded;
	}
	
	/**
	 * 
	 * @return The first game state for a text adventure game.
	 */
	public SelectionAwareObservableList<String> firstGameState() {
		return firstGameState;
	}
	
	/**
	 * 
	 * @return Display string for no settings.
	 */
	public SimpleStringProperty noSettingsText() {
		return noSettingsText;
	}
	
	/**
	 * 
	 * @return Display string for settings.
	 */
	public SimpleStringProperty settingText() {
		return settingText;
	}
	
	/**
	 * 
	 * @return Display string for start game state.
	 */
	public SimpleStringProperty startGameStateText() {
		return startGameStateText;
	}
}
