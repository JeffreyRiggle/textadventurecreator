package ilusr.textadventurecreator.debug;

import ilusr.logrunner.LogRunner;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugNamedObjectModel {

	private final INamedObject namedObject;
	private SimpleStringProperty name;
	private SimpleStringProperty value;
	private SimpleStringProperty description;
	private SimpleBooleanProperty changed;
	private SimpleBooleanProperty added;
	private SimpleBooleanProperty removed;
	
	/**
	 * 
	 * @param namedObject A @see INamedObject to assoicate with this model.
	 */
	public DebugNamedObjectModel(INamedObject namedObject) {
		this.namedObject = namedObject;
		name = new SimpleStringProperty(namedObject.name());
		value = new SimpleStringProperty(namedObject.value());
		description = new SimpleStringProperty(namedObject.description());
		changed = new SimpleBooleanProperty();
		added = new SimpleBooleanProperty();
		removed = new SimpleBooleanProperty();
		
		initialize();
	}
	
	private void initialize() {
		namedObject.updated(() -> {
			Platform.runLater(() -> {
				LogRunner.logger().info(String.format("Object %s, updated setting changed to true.", namedObject.name()));
				value.set(namedObject.value());
				changed.set(true);
			});
		});
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the name of this debug object.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the value of this debug object.
	 */
	public SimpleStringProperty value() {
		return value;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the description of this debug object.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this object was changed.
	 */
	public SimpleBooleanProperty changed() {
		return changed;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this object was added.
	 */
	public SimpleBooleanProperty added() {
		return added;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this object was removed.
	 */
	public SimpleBooleanProperty removed() {
		return removed;
	}
}
