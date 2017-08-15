package ilusr.textadventurecreator.debug;

import ilusr.logrunner.LogRunner;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EquipDebugModel {

	private SimpleStringProperty displayName;
	private BodyPartDebugModel bodyPart;
	private SimpleObjectProperty<DebugItemModel> item;
	private SimpleBooleanProperty added;
	private SimpleBooleanProperty removed;
	private SimpleBooleanProperty changed;
	
	/**
	 * 
	 * @param bodyPart A @see BodyPartDebugModel to represent the body part for this equipment.
	 * @param item A @see DebugItemModel to represent the item for this equipment.
	 */
	public EquipDebugModel(BodyPartDebugModel bodyPart, DebugItemModel item) {
		this.bodyPart = bodyPart;
		this.item = new SimpleObjectProperty<DebugItemModel>(item);
		displayName = new SimpleStringProperty(bodyPart.name().get() + " : " + item.name().get());
		added = new SimpleBooleanProperty();
		removed = new SimpleBooleanProperty();
		changed = new SimpleBooleanProperty();
		
		initialize();	
	}
	
	private void initialize() {
		item.addListener((v, o, n) -> {
			displayName.set(bodyPart.name().get() + " : " + n.name().get());
		});
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display name for this equipment. Name should be body part name : item name.
	 */
	public SimpleStringProperty displayName() {
		return displayName;
	}
	
	/**
	 * 
	 * @return A @see BodyPartDebugModel associated with this equipment.
	 */
	public BodyPartDebugModel bodyPart() {
		return bodyPart;
	}
	
	/**
	 * 
	 * @return A @see SimpleObjectProperty of type @see DebugItemModel representing the item associated with this equipment.
	 */
	public SimpleObjectProperty<DebugItemModel> item() {
		return item;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this equipment was added.
	 */
	public SimpleBooleanProperty added() {
		return added;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this equipment was removed.
	 */
	public SimpleBooleanProperty removed() {
		return removed;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this equipment was changed.
	 */
	public SimpleBooleanProperty changed() {
		return changed;
	}
	
	/**
	 * Resets all of the added, changed and removed states associated with this equipment.
	 */
	public void resetChangeNotifications() {
		LogRunner.logger().info(String.format("Reseting notifications for equipment on %s", bodyPart.name().get()));
		if (changed.get()) {
			changed.set(false);
		}
		
		if (added.get()) {
			added.set(false);
		}
		
		item.get().resetChangeNotifications();
		bodyPart.resetChangeNotifications();
	}
}
