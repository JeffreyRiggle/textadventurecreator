package ilusr.textadventurecreator.mod;

import ilusr.core.ioc.ServiceManager;

/**
 * 
 * @author Jeff Riggle
 *
 *	This interface is used to load mods into the TextAventureCreator.
 *  In order to create a mod you must have one class that implements this interface.
 */
public interface IMod {
	/**
	 * Returns a display name for the mod.
	 * 
	 * @return The name of the mod.
	 */
	String name();
	/**
	 * A unique identifier for the mod. This should be a UUID string representation.
	 * @return A unique identifier for a mod.
	 */
	String id();
	/**
	 * This is the method that will be called to load your mod.
	 * This is where all of your initialization logic should occur.
	 * 
	 * @param data A @see LoadData. This allows the mod to pull from our service manager.
	 */
	void load(ServiceManager manager);
	/**
	 * Unloads this mod. This occurs when someone has deactivated the mod.
	 * In this case you should clean up your mod.
	 */
	void unload();
}
