package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerDebugView extends AnchorPane implements Initializable {

	@FXML
	private Label player;
	
	@FXML
	private VBox attributeArea;

	@FXML
	private VBox characteristicArea;
	
	@FXML
	private VBox bodyPartArea;
	
	@FXML
	private VBox inventoryArea;
	
	@FXML
	private VBox equipmentArea;
	
	@FXML
	private TitledPane attributePane;
	
	@FXML
	private TitledPane characteristicPane;
	
	@FXML
	private TitledPane bodyPartPane;
	
	@FXML
	private TitledPane inventoryPane;
	
	@FXML
	private TitledPane equipmentPane;
	
	private PlayerDebugModel model;
	private Map<DebugNamedObjectModel, DebugNamedObject> attributes;
	private Map<DebugNamedObjectModel, DebugNamedObject> characteristics;
	private Map<BodyPartDebugModel, BodyPartDebugView> bodyParts;
	private Map<DebugItemModel, DebugItemView> items;
	private Map<EquipDebugModel, EquipmentDebugView> equipment;
	
	/**
	 * 
	 * @param model A @see PlayerDebugModel to associate with this view.
	 */
	public PlayerDebugView(PlayerDebugModel model) {
		this.model = model;
		attributes = new HashMap<DebugNamedObjectModel, DebugNamedObject>();
		characteristics = new HashMap<DebugNamedObjectModel, DebugNamedObject>();
		bodyParts = new HashMap<BodyPartDebugModel, BodyPartDebugView>();
		items = new HashMap<DebugItemModel, DebugItemView>();
		equipment = new HashMap<EquipDebugModel, EquipmentDebugView>();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerDebugView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (model != null) {
			setupBindings();
		}
		
		attributePane.textProperty().bind(model.attributeText());
		characteristicPane.textProperty().bind(model.characteristicText());
		bodyPartPane.textProperty().bind(model.bodyPartText());
		inventoryPane.textProperty().bind(model.inventoryText());
		equipmentPane.textProperty().bind(model.equipmentText());
		
		attributePane.expandedProperty().set(false);
		characteristicPane.expandedProperty().set(false);
		bodyPartPane.expandedProperty().set(false);
		inventoryPane.expandedProperty().set(false);
		equipmentPane.expandedProperty().set(false);
	}
	
	private void setupBindings() {
		player.textProperty().set(model.player().name());
		
		addAttributes(model.attributes());
		
		model.attributes().addListener((Change<? extends DebugNamedObjectModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addAttributes(c.getAddedSubList());
			removeAttributes(c.getRemoved());
		});
		
		addCharacteristics(model.characteristics());
		
		model.characteristics().addListener((Change<? extends DebugNamedObjectModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addCharacteristics(c.getAddedSubList());
			removeCharacteristics(c.getRemoved());
		});
		
		addBodyParts(model.bodyParts());
		
		model.bodyParts().addListener((Change<? extends BodyPartDebugModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addBodyParts(c.getAddedSubList());
			removeBodyParts(c.getRemoved());
		});
		
		addItems(model.items());
		model.items().addListener((Change<? extends DebugItemModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addItems(c.getAddedSubList());
			removeItems(c.getRemoved());
		});
		
		addEquip(model.equipment());
		model.equipment().addListener((Change<? extends EquipDebugModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addEquip(c.getAddedSubList());
			removeEquip(c.getRemoved());
		});
	}
	
	private void addAttributes(List<? extends DebugNamedObjectModel> attribs) {
		for (DebugNamedObjectModel attribute : attribs) {
			if (attributes.containsKey(attribute)) {
				continue;
			}
			
			DebugNamedObject attView = new DebugNamedObject(attribute);
			attributes.put(attribute, attView);
			attributeArea.getChildren().add(attView);
		}
	}
	
	private void removeAttributes(List<? extends DebugNamedObjectModel> attribs) {
		for (DebugNamedObjectModel attribute : attribs) {
			if (!attributes.containsKey(attribute)) {
				continue;
			}
			
			attributeArea.getChildren().remove(attributes.get(attribute));
			attributes.remove(attribute);
		}
	}
	
	private void addCharacteristics(List<? extends DebugNamedObjectModel> chars) {
		for (DebugNamedObjectModel characteristic : chars) {
			if (characteristics.containsKey(characteristic)) {
				continue;
			}
			
			DebugNamedObject charView = new DebugNamedObject(characteristic);
			characteristics.put(characteristic, charView);
			characteristicArea.getChildren().add(charView);
		}
	}
	
	private void removeCharacteristics(List<? extends DebugNamedObjectModel> chars) {
		for (DebugNamedObjectModel attribute : chars) {
			if (!characteristics.containsKey(attribute)) {
				continue;
			}
			
			characteristicArea.getChildren().remove(characteristics.get(attribute));
			characteristics.remove(attribute);
		}
	}
	
	private void addBodyParts(List<? extends BodyPartDebugModel> parts) {
		for (BodyPartDebugModel bodyPart : parts) {
			if (bodyParts.containsKey(bodyPart)) {
				continue;
			}
			
			BodyPartDebugView bPart = new BodyPartDebugView(bodyPart);
			bodyParts.put(bodyPart, bPart);
			bodyPartArea.getChildren().add(bPart);
		}
	}
	
	private void removeBodyParts(List<? extends BodyPartDebugModel> parts) {
		for (BodyPartDebugModel bodyPart : parts) {
			if (!bodyParts.containsKey(bodyPart)) {
				continue;
			}
			
			bodyPartArea.getChildren().remove(bodyParts.get(bodyPart));
			bodyParts.remove(bodyPart);
		}
	}
	
	private void addItems(List<? extends DebugItemModel> itms) {
		for (DebugItemModel item : itms) {
			if (items.containsKey(item)) {
				continue;
			}
			
			DebugItemView itemView = new DebugItemView(item);
			items.put(item, itemView);
			inventoryArea.getChildren().add(itemView);
		}
	}
	
	private void removeItems(List<? extends DebugItemModel> itms) {
		for (DebugItemModel item : itms) {
			if (!items.containsKey(item)) {
				continue;
			}
			
			inventoryArea.getChildren().remove(items.get(item));
			items.remove(item);
		}
	}
	
	private void addEquip(List<? extends EquipDebugModel> eqp) {
		for (EquipDebugModel equip : eqp) {
			if (equipment.containsKey(equip)) {
				continue;
			}
			
			EquipmentDebugView equipView = new EquipmentDebugView(equip);
			equipment.put(equip, equipView);
			equipmentArea.getChildren().add(equipView);
		}
	}
	
	private void removeEquip(List<? extends EquipDebugModel> eqp) {
		for (EquipDebugModel equip : eqp) {
			if (!equipment.containsKey(equip)) {
				continue;
			}
			
			equipmentArea.getChildren().remove(equipment.get(equip));
			equipment.remove(equip);
		}
	}
}
