package ilusr.textadventurecreator.views.player;

import ilusr.iroshell.services.TabContent;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerContentTab extends TabContent {

	private final String PLAYER_NAME_PERSISTENCE = "PlayerName: ";
	private final String PLAYER = "Player";
	private final String PLAYER_INFO = "Information about player";
	
	/**
	 * 
	 * @param view The @see PlayerView to show in this tab.
	 */
	public PlayerContentTab(PlayerView view) {
		super.content().set(view);
		super.titleGraphic(PLAYER);
		super.toolTip().set(PLAYER_INFO);
		super.canClose().set(true);
		
		String name = view.model().playerID().get();
		
		if (name != null) {
			super.titleGraphic(String.format("%s %s", PLAYER, name));
			super.toolTip().set(String.format("%s %s", PLAYER_INFO, name));
			super.customData().set(PLAYER_NAME_PERSISTENCE + name);
		}
		
		view.model().playerID().addListener((v, o, n) -> {
			super.titleGraphic(String.format("%s %s", PLAYER, n));
			super.toolTip().set(String.format("%s %s", PLAYER_INFO, n));
			super.customData().set(PLAYER_NAME_PERSISTENCE + n);
		});
	}
}
