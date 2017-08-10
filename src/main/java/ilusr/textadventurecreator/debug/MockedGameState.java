package ilusr.textadventurecreator.debug;

import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.GameStateManager;
import ilusr.textadventurecreator.language.ILanguageService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MockedGameState extends GameState {

	private String id;
	
	/**
	 * 
	 * @param id The id of the game state.
	 * @param manager A @see GameStateManager.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MockedGameState(String id, GameStateManager manager, ILanguageService languageService) {
		super(new MockedGameStateViewHost(id, manager, languageService));
		this.id = id;
	}
	
	/**
	 * 
	 * @return The id of the game state.
	 */
	public String getId() {
		return id;
	}
}
