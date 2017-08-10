package ilusr.textadventurecreator.views.action;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class PlayerDataView extends AnchorPane {
	/**
	 * 
	 * @return The data associated with the player.
	 */
	public abstract SimpleObjectProperty<? extends Object> getData();
}
