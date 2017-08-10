package ilusr.textadventurecreator.debug;

import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IDebugService {
	/**
	 * Creates a new scene to play a game. This will play a game in a non-debug mode.
	 * 
	 * @param game A @see TextAdventurePersistenceObject to play.
	 */
	void runGame(TextAdventurePersistenceObject game);
	/**
	 * Creates a new scene to play a game. This game will be played in debug mode.
	 * 
	 * @param game A @see TextAdventurePersistenceObject to play.
	 */
	void debugGame(TextAdventurePersistenceObject game);
	/**
	 * Creates a scene to play a single game state. This game state will be played in debug mode.
	 * 
	 * @param gameState A @see GameStatePersistenceObject to play.
	 */
	void debugGameState(GameStatePersistenceObject gameState);
}
