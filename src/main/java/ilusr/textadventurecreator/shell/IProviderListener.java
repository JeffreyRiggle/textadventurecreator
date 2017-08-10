package ilusr.textadventurecreator.shell;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IProviderListener {
	/**
	 * This is called when the current project is changed.
	 * 
	 * @param textAdventure The new @see TextAdventureProjectPersistence to use
	 */
	void provided(TextAdventureProjectPersistence textAdventure);
}
