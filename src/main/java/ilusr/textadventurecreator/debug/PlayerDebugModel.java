package ilusr.textadventurecreator.debug;

import java.util.ArrayList;
import java.util.List;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;
import playerlib.equipment.IEquiped;
import playerlib.items.IItem;
import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerDebugModel {

	private final ILanguageService languageService;
	
	private IPlayer player;
	private ObservableList<DebugNamedObjectModel> attributes;
	private ObservableList<DebugNamedObjectModel> characteristics;
	private ObservableList<BodyPartDebugModel> bodyParts;
	private ObservableList<DebugItemModel> items;
	private ObservableList<EquipDebugModel> equipment;
	private LanguageAwareString attributeText;
	private LanguageAwareString characteristicText;
	private LanguageAwareString bodyPartText;
	private LanguageAwareString inventoryText;
	private LanguageAwareString equipmentText;
	
	/**
	 * 
	 * @param player A @see IPlayer to associate with this model.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayerDebugModel(IPlayer player, ILanguageService languageService) {
		this.player = player;
		this.languageService = languageService;
		
		attributes = FXCollections.observableArrayList();
		characteristics = FXCollections.observableArrayList();
		bodyParts = FXCollections.observableArrayList();
		items = FXCollections.observableArrayList();
		equipment = FXCollections.observableArrayList();
		attributeText = new LanguageAwareString(languageService, DisplayStrings.ATTRIBUTES);
		characteristicText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTICS);
		bodyPartText = new LanguageAwareString(languageService, DisplayStrings.BODY_PARTS);
		inventoryText = new LanguageAwareString(languageService, DisplayStrings.ITEMS);
		equipmentText = new LanguageAwareString(languageService, DisplayStrings.EQUIPMENT);
		
		initialize();
	}
	
	private void initialize() {
		for(IAttribute attribute : player.attributes()) {
			LogRunner.logger().info(String.format("Adding attribute %s to player %s", attribute.name(), player.name()));
			attributes.add(new DebugNamedObjectModel(new DebugAttribute(attribute)));
		}
		
		player.addAttributeChangeListener((a, r, c) -> {
			LogRunner.logger().info(String.format("Updating attributes on player %s", player.name()));
			updateAttributes(a, r);
		});
		
		for (ICharacteristic characteristic : player.characteristics()) {
			LogRunner.logger().info( String.format("Adding characteristic %s to player %s", characteristic.name(), player.name()));
			characteristics.add(new DebugNamedObjectModel(new DebugCharacteristic(characteristic)));
		}
		
		player.addCharacteristicChangeListener((a, r, c) -> {
			LogRunner.logger().info(String.format("Updating characteristics on player %s", player.name()));
			updateCharacteristics(a, r);
		});
		
		for (IBodyPart bodyPart : player.bodyParts()) {
			LogRunner.logger().info(String.format("Body Part %s to player %s", bodyPart.name(), player.name()));
			BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
			bodyParts.add(model);
			
			IItem item = player.equipment().equipted(bodyPart);
			
			if (item != null) {
				LogRunner.logger().info(String.format("Adding equipt item %s to body part %s", item.name(), bodyPart.name()));
				EquipDebugModel eModel = new EquipDebugModel(model, new DebugItemModel(item, languageService));
				equipment.add(eModel);
			}
		}
		
		player.addBodyPartChangeListener((a, r, c) -> {
			LogRunner.logger().info(String.format("Updating body parts on player %s", player.name()));
			updateBodyParts(a, r);
		});
		
		for (IItem item : player.inventory().items()) {
			LogRunner.logger().info(String.format("Adding item %s to player %s", item.name(), player.name()));
			items.add(new DebugItemModel(item, languageService));
		}
		
		player.inventory().addItemChangeListener((a, r, c) -> {
			LogRunner.logger().info(String.format("Updating items on player %s", player.name()));
			updateItems(a, r);
		});
		
		player.equipment().addEquipmentChangeListener((a, r, c) -> {
			LogRunner.logger().info(String.format("Updating equipment on player %s", player.name()));
			updateEquipment(a, c, r);
		});
	}
	
	private void updateAttributes(List<IAttribute> a, List<IAttribute> r) {
		Platform.runLater(() -> {
			for (IAttribute attribute : a) {
				LogRunner.logger().info(String.format("Adding attribute %s to player %s", attribute.name(), player.name()));
				DebugNamedObjectModel model = new DebugNamedObjectModel(new DebugAttribute(attribute));
				model.added().set(true);
				attributes.add(model);
			}
			
			for (IAttribute attribute : r) {
				for (DebugNamedObjectModel model : attributes) {
					if (model.name().get().equals(attribute.name())) {
						LogRunner.logger().info(String.format("Setting attribute %s to removed on player %s", attribute.name(), player.name()));
						model.removed().set(true);
						break;
					}
				}
			}
		});
	}
	
	private void updateCharacteristics(List<ICharacteristic> a, List<ICharacteristic> r) {
		Platform.runLater(() -> {
			for (ICharacteristic characteristic : a) {
				LogRunner.logger().info(String.format("Adding characteristic %s to player %s", characteristic.name(), player.name()));
				DebugNamedObjectModel model = new DebugNamedObjectModel(new DebugCharacteristic(characteristic));
				model.added().set(true);
				characteristics.add(model);
			}
			
			for (ICharacteristic characteristic : r) {
				for (DebugNamedObjectModel model : characteristics) {
					if (model.name().get().equals(characteristic.name())) {
						LogRunner.logger().info(String.format("Setting characteristic %s to removed on player %s", characteristic.name(), player.name()));
						model.removed().set(true);
						break;
					}
				}
			}
		});
	}
	
	private void updateBodyParts(List<IBodyPart> a, List<IBodyPart> r) {
		Platform.runLater(() -> {
			for (IBodyPart bodyPart : a) {
				LogRunner.logger().info(String.format("Adding body part %s to player %s", bodyPart.name(), player.name()));
				BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
				model.added().set(true);
				bodyParts.add(model);
			}
			
			for (IBodyPart bodyPart : r) {
				for (BodyPartDebugModel model : bodyParts) {
					if (model.name().get().equals(bodyPart.name())) {
						LogRunner.logger().info(String.format("Setting body part %s to removed on player %s", bodyPart.name(), player.name()));
						model.removed().set(true);
						break;
					}
				}
			}
		});
	}
	
	private void updateItems(List<IItem> a, List<IItem> r) {
		Platform.runLater(() -> {
			for (IItem item : a) {
				LogRunner.logger().info(String.format("Adding item %s to player %s", item.name(), player.name()));
				DebugItemModel model = new DebugItemModel(item, languageService);
				model.added().set(true);
				items.add(model);
			}
			
			for (IItem item : r) {
				for (DebugItemModel model : items) {
					if (model.name().get().equals(item.name())) {
						LogRunner.logger().info(String.format("Setting item %s to removed on player %s", item.name(), player.name()));
						model.removed().set(true);
						break;
					}
				}
			}
		});
	}
	
	private void updateEquipment(List<IEquiped> a, List<IEquiped> c, List<IEquiped> r) {
		Platform.runLater(() -> {
			for (IEquiped equiped : a) {
				LogRunner.logger().info(String.format("Adding equiped item %s to player %s", equiped.getItem().name(), player.name()));
				EquipDebugModel model = new EquipDebugModel(new BodyPartDebugModel(equiped.getBodyPart(), languageService), new DebugItemModel(equiped.getItem(), languageService));
				model.added().set(true);
				equipment.add(model);
			}
			
			for (IEquiped equiped : r) {
				for (EquipDebugModel model : equipment) {
					if (model.bodyPart().name().get().equals(equiped.getBodyPart().name())) {
						LogRunner.logger().info(String.format("Setting equiped body part %s to removed on player %s", model.bodyPart().name().get(), player.name()));
						model.removed().set(true);
						break;
					}
				}
			}
			
			for (IEquiped equiped : c) {
				for (EquipDebugModel model : equipment) {
					if (!model.bodyPart().name().get().equals(equiped.getBodyPart().name())) {
						continue;
					}
					
					LogRunner.logger().info(String.format("Changing equiped item to %s for body part %s", equiped.getItem().name(), equiped.getBodyPart().name()));
					model.changed().set(true);
					DebugItemModel item = new DebugItemModel(equiped.getItem(), languageService);
					item.added().set(true);
					model.item().set(item);
					break;
				}
			}
		});
	}
	
	/**
	 * 
	 * @return The @see IPlayer assoicated with this model.
	 */
	public IPlayer player() {
		return player;
	}

	/**
	 * 
	 * @return A @see ObservableList of type @see DebugNamedObjectModel representing the attributes for this player.
	 */
	public ObservableList<DebugNamedObjectModel> attributes() {
		return attributes;
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see DebugNamedObjectModel representing the characteristics for this player.
	 */
	public ObservableList<DebugNamedObjectModel> characteristics() {
		return characteristics;
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see BodyPartDebugModel representing the body parts for this player.
	 */
	public ObservableList<BodyPartDebugModel> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see DebugItemModel representing the items for this player.
	 */
	public ObservableList<DebugItemModel> items() {
		return items;
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see EquipDebugModel representing the equipment for this player.
	 */
	public ObservableList<EquipDebugModel> equipment() {
		return equipment;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for Attributes.
	 */
	public SimpleStringProperty attributeText() {
		return attributeText;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for Characteristics.
	 */
	public SimpleStringProperty characteristicText() {
		return characteristicText;
	}

	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for BodyParts.
	 */
	public SimpleStringProperty bodyPartText() {
		return bodyPartText;
	}

	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for Items.
	 */
	public SimpleStringProperty inventoryText() {
		return inventoryText;
	}

	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for Equipment.
	 */
	public SimpleStringProperty equipmentText() {
		return equipmentText;
	}

	/**
	 * Resets the added, changed and remove state of this player. 
	 * This will also remove removed attributes, characteristics, body parts, items and equipment.
	 */
	public void resetNotifications() {
		LogRunner.logger().info(String.format("Reseting change notifications on player %s", player.name()));
		resetAttributes();
		resetCharacteristics();
		resetBodyParts();
		resetInventory();
		resetEquipment();
	}
	
	private void resetAttributes() {
		List<DebugNamedObjectModel> removableAttributes = new ArrayList<DebugNamedObjectModel>();
		
		for (DebugNamedObjectModel aModel : attributes) {
			if (aModel.changed().get()) {
				aModel.changed().set(false);
			}
			
			if (aModel.added().get()) {
				aModel.added().set(false);
			}
			
			if (aModel.removed().get()) {
				removableAttributes.add(aModel);
			}
		}
		attributes.removeAll(removableAttributes);
	}
	
	private void resetCharacteristics() {
		List<DebugNamedObjectModel> removeable = new ArrayList<DebugNamedObjectModel>();
		
		for (DebugNamedObjectModel cModel : characteristics) {
			if (cModel.changed().get()) {
				cModel.changed().set(false);
			}
			
			if (cModel.added().get()) {
				cModel.added().set(false);
			}
			
			if (cModel.removed().get()) {
				removeable.add(cModel);
			}
		}
		characteristics.removeAll(removeable);
	}
	
	private void resetBodyParts() {
		List<BodyPartDebugModel> removable = new ArrayList<BodyPartDebugModel>();
		
		for (BodyPartDebugModel bModel : bodyParts) {
			if (bModel.removed().get()) {
				removable.add(bModel);
			}
			
			bModel.resetChangeNotifications();
		}
		
		bodyParts.removeAll(removable);
	}
	
	private void resetInventory() {
		List<DebugItemModel> removable = new ArrayList<DebugItemModel>();
		
		for (DebugItemModel iModel : items) {
			if (iModel.removed().get()) {
				removable.add(iModel);
			}
			
			iModel.resetChangeNotifications();
		}
		
		items.removeAll(removable);
	}
	
	private void resetEquipment() {
		List<EquipDebugModel> removable = new ArrayList<EquipDebugModel>();
		
		for (EquipDebugModel eModel : equipment) {
			if (eModel.removed().get()) {
				removable.add(eModel);
			}
			
			eModel.resetChangeNotifications();
		}
		
		equipment.removeAll(removable);
	}
}
