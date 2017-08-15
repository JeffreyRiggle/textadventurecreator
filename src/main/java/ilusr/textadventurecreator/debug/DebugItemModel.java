package ilusr.textadventurecreator.debug;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import playerlib.items.IItem;
import playerlib.items.IProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugItemModel {
	
	private IItem item;
	
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private LanguageAwareString propTitle;
	private ObservableList<DebugNamedObjectModel> properties;
	private SimpleBooleanProperty added;
	private SimpleBooleanProperty removed;
	
	/**
	 * 
	 * @param item The @see IItem to associate this model with.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public DebugItemModel(IItem item, ILanguageService languageService) {
		this.item = item;
		name = new SimpleStringProperty(item.name());
		description = new SimpleStringProperty(item.description());
		propTitle = new LanguageAwareString(languageService, DisplayStrings.PROPERTIES);
		added = new SimpleBooleanProperty();
		removed = new SimpleBooleanProperty();
		properties = FXCollections.observableArrayList();
		
		initialize();
	}
	
	private void initialize() {
		for(IProperty prop : item.properties()) {
			properties.add(new DebugNamedObjectModel(new DebugProperty(prop)));
		}
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the items name.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the items description.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display value for Properties.
	 */
	public SimpleStringProperty propTitle() {
		return propTitle;
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see DebugNamedObject representing the properies assoicated with this item.
	 */
	public ObservableList<DebugNamedObjectModel> properties() {
		return properties;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this item was added.
	 */
	public SimpleBooleanProperty added() {
		return added;
	}
	
	/**
	 * 
	 * @return A @see SimpleBooleanProperty representing if this item was removed.
	 */
	public SimpleBooleanProperty removed() {
		return removed;
	}
	
	/**
	 * Resets the added, updated and removed notifications associated with this model.
	 */
	public void resetChangeNotifications() {
		LogRunner.logger().info(String.format("Reseting notifications for item: %s", item.name()));
		if (added.get()) {
			added.set(false);
		}
		
		for (DebugNamedObjectModel model : properties) {
			if (model.changed().get()) {
				model.changed().set(false);
			}
			
			if (model.added().get()) {
				model.added().set(false);
			}
		}
	}
}
