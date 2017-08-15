package ilusr.textadventurecreator.shell;

import java.util.ArrayList;
import java.util.List;

import ilusr.logrunner.LogRunner;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureProvider {

	private TextAdventureProjectPersistence textAdventure;
	private List<IProviderListener> listeners;
	
	/**
	 * Creates a text adventure provider.
	 */
	public TextAdventureProvider() {
		listeners = new ArrayList<IProviderListener>();
	}
	
	/**
	 * 
	 * @param listener A @see IProviderListener to be notified when the text adventure changes.
	 */
	public void addListener(IProviderListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener A @see IProviderListener to be notified when the text adventure changes.
	 */
	public void removeListener(IProviderListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * 
	 * @param textAdventure The new @see TextAdventureProjectPersistence to provide.
	 */
	public void setTextAdventure(TextAdventureProjectPersistence textAdventure) {
		LogRunner.logger().info("Got a new text adventure notifying listeners");
		this.textAdventure = textAdventure;
		
		for (IProviderListener listener : listeners) {
			listener.provided(this.textAdventure);
		}
	}
	
	/**
	 * 
	 * @return The current @see TextAdventureProjectPersistence
	 */
	public TextAdventureProjectPersistence getTextAdventureProject() {
		return textAdventure;
	}
}
