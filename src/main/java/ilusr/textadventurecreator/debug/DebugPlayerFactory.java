package ilusr.textadventurecreator.debug;

import ilusr.textadventurecreator.language.ILanguageService;
import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugPlayerFactory {

	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public DebugPlayerFactory(ILanguageService languageService) {
		this.languageService = languageService;
	}
	
	/**
	 * 
	 * @param player A @see IPlayer to use to create this model.
	 * @return The @see PlayerDebugModel that was created.
	 */
	public PlayerDebugModel create(IPlayer player) {
		return new PlayerDebugModel(player, languageService);
	}
}
