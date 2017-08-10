package ilusr.textadventurecreator.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartDebugModel {

	private final ILanguageService languageService;
	
	private IBodyPart bodyPart;
	
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private SimpleStringProperty charTitle;
	private ObservableList<DebugNamedObjectModel> characteristics;
	private SimpleBooleanProperty removed;
	private SimpleBooleanProperty added;
	private SimpleBooleanProperty changed;
	
	/**
	 * 
	 * @param bodyPart A @see IBodyPart to debug.
	 * @param languageService A @see LanguageService to use for localization.
	 */
	public BodyPartDebugModel(IBodyPart bodyPart, ILanguageService languageService) {
		this.bodyPart = bodyPart;
		this.languageService = languageService;
		
		name = new SimpleStringProperty(bodyPart.name());
		description = new SimpleStringProperty(bodyPart.description());
		charTitle = new SimpleStringProperty(languageService.getValue(DisplayStrings.CHARACTERISTICS));
		characteristics = FXCollections.observableArrayList();
		removed = new SimpleBooleanProperty();
		added = new SimpleBooleanProperty();
		changed = new SimpleBooleanProperty();
		
		initialize();
	}
	
	private void initialize() {
		for (ICharacteristic characteristic : bodyPart.getCharacteristics()) {
			characteristics.add(new DebugNamedObjectModel(new DebugCharacteristic(characteristic)));
		}
		
		bodyPart.addCharacteristicChangeListener((a, r, c) -> {
			if (Platform.isFxApplicationThread()) {
				updateCharacteristics(a, r);
				return;
			}
			
			Platform.runLater(() -> {
				updateCharacteristics(a, r);
			});
		});
		
		languageService.addListener(() -> {
			charTitle.set(languageService.getValue(DisplayStrings.CHARACTERISTICS));
		});
	}
	
	private void updateCharacteristics(List<ICharacteristic> a, List<ICharacteristic>r) {
		for (ICharacteristic characteristic : a) {
			LogRunner.logger().log(Level.INFO, String.format("Characteristic %s added to body part %s", characteristic.name(), bodyPart.name()));
			DebugNamedObjectModel model = new DebugNamedObjectModel(new DebugCharacteristic(characteristic));
			model.added().set(true);
			characteristics.add(model);
		}
		
		for (ICharacteristic characteristic : r) {
			for (DebugNamedObjectModel model : characteristics) {
				if (characteristic.name().equals(model.name().get())) {
					LogRunner.logger().log(Level.INFO, String.format("Characteristic %s removed from body part %s", characteristic.name(), bodyPart.name()));
					model.removed().set(true);
					break;
				}
			}
		}
		
		changed.set(true);
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the body parts name.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the body parts description.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty represnting the characteristics display string.
	 */
	public SimpleStringProperty charTitle() {
		return charTitle;
	}
	
	/**
	 * 
	 * @return A List of @see DebugNamedObjectModel representing the characteristics assoicated with this body part.
	 */
	public ObservableList<DebugNamedObjectModel> charactersitics() {
		return characteristics;
	}
	
	/**
	 * 
	 * @return If the body part was removed.
	 */
	public SimpleBooleanProperty removed() {
		return removed;
	}
	
	/**
	 * 
	 * @return If the body part was changed.
	 */
	public SimpleBooleanProperty changed() {
		return changed;
	}
	
	/**
	 * 
	 * @return if the body part was added.
	 */
	public SimpleBooleanProperty added() {
		return added;
	}
	
	/**
	 * Resets the added, removed and changed states.
	 */
	public void resetChangeNotifications() {
		LogRunner.logger().log(Level.INFO, String.format("Reseting notifications for body part %s", bodyPart.name()));
		
		if (changed.get()) {
			changed.set(false);
		}
		
		if (added.get()) {
			added.set(false);
		}
		
		List<DebugNamedObjectModel> removeable = new ArrayList<DebugNamedObjectModel>();
		
		for (DebugNamedObjectModel model : characteristics) {
			if (model.changed().get()) {
				model.changed().set(false);
			}
			
			if (model.added().get()) {
				model.added().set(false);
			}
			
			if (model.removed().get()) {
				removeable.add(model);
			}
		}
		
		characteristics.removeAll(removeable);
	}
}
