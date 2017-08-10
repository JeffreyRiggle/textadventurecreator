package ilusr.textadventurecreator.toolbars;

import ilusr.textadventurecreator.shell.IProviderListener;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.scene.control.Button;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class GameStateAwareButton extends Button implements IProviderListener {
	
	private final TextAdventureProvider provider;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param itemName The string to display for this button.
	 */
	public GameStateAwareButton(TextAdventureProvider provider, String itemName) {
		super(itemName);
		this.provider = provider;
		
		initialize();
	}
	
	private void initialize() {
		super.setDisable(provider.getTextAdventureProject() == null);
		provider.addListener(this);
	}

	@Override
	public void provided(TextAdventureProjectPersistence textAdventure) {
		super.setDisable(textAdventure == null);
	}
}
