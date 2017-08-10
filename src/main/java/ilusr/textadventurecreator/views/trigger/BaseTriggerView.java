package ilusr.textadventurecreator.views.trigger;

import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class BaseTriggerView extends AnchorPane {

	/**
	 * 
	 * @return The @see TriggerPersistenceObject associated with this view.
	 */
	public abstract TriggerPersistenceObject triggerPersistenceObject();
}
