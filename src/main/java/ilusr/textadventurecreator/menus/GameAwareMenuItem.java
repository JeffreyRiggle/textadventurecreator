package ilusr.textadventurecreator.menus;

import ilusr.textadventurecreator.shell.IProviderListener;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class GameAwareMenuItem extends MenuItem implements IProviderListener {
	
	private final TextAdventureProvider provider;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to get the current text adventure.
	 * @param itemName The display name.
	 */
	public GameAwareMenuItem(TextAdventureProvider provider, String itemName) {
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
