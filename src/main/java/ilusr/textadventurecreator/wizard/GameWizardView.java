package ilusr.textadventurecreator.wizard;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class GameWizardView extends AnchorPane {

	private SimpleBooleanProperty valid;
	
	/**
	 * Creates a game wizard view.
	 */
	public GameWizardView() {
		valid = new SimpleBooleanProperty(true);
		this.getStyleClass().add("root");
	}
	
	/**
	 * 
	 * @param model The new @see GameSettingsModel to bind to.
	 */
	public abstract void setModel(GameSettingsModel model);
	
	/**
	 * 
	 * @return If the game wizard view is valid or not.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
}
