package ilusr.textadventurecreator.views.action;

import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class BaseActionView extends AnchorPane {

	/**
	 * 
	 * @return The associated persistence object.
	 */
	public abstract ActionPersistenceObject getPersistenceObject();
}
