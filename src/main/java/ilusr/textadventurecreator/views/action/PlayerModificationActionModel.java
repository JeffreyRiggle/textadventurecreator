package ilusr.textadventurecreator.views.action;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import ilusr.textadventurecreator.views.player.InventoryItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import textadventurelib.core.ChangeType;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.ModificationType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationActionModel {
	
	private final List<PlayerPersistenceObject> players;
	
	private SelectionAwareObservableList<String> selectedPlayers;
	private SelectionAwareObservableList<String> changeTypes;
	private SelectionAwareObservableList<String> modifications;
	private SimpleObjectProperty<Object> modificationData;
	private SelectionAwareObservableList<String> modificationObject;
	private SelectionAwareObservableList<String> id;
	private ChangeListener<? super ItemPersistenceObject> itemChangeListener;
	private ChangeListener<? super Number> amountChangeListener;
	private LanguageAwareString playerText;
	private LanguageAwareString propertyText;
	private LanguageAwareString idText;
	private LanguageAwareString dataMemberText;
	private LanguageAwareString modTypeText;
	private LanguageAwareString changeTypeText;
	private LanguageAwareString modDataText;
	
	private ModifyPlayerActionPersistence action;
	
	/**
	 * 
	 * @param action A @see ModifyPlayerActionPersistence to bind to.
	 * @param players A list of players to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayerModificationActionModel(ModifyPlayerActionPersistence action, 
										 List<PlayerPersistenceObject> players,
										 ILanguageService languageService) {
		this.action = action;
		this.players = players;
		
		modificationData = new SimpleObjectProperty<Object>();
		selectedPlayers = new SelectionAwareObservableList<String>();
		modifications = new SelectionAwareObservableList<String>();
		changeTypes = new SelectionAwareObservableList<String>();
		modificationObject = new SelectionAwareObservableList<String>();
		id = new SelectionAwareObservableList<String>();
		playerText = new LanguageAwareString(languageService, DisplayStrings.PLAYER);
		propertyText = new LanguageAwareString(languageService, DisplayStrings.PROPERTY);
		idText = new LanguageAwareString(languageService, DisplayStrings.ID);
		dataMemberText = new LanguageAwareString(languageService, DisplayStrings.DATA_MEMBER);
		modTypeText = new LanguageAwareString(languageService, DisplayStrings.MODIFICATION_TYPE);
		changeTypeText = new LanguageAwareString(languageService, DisplayStrings.CHANGE_TYPE);
		modDataText = new LanguageAwareString(languageService, DisplayStrings.MODIFICATION_DATA);
		
		initialize();
	}
	
	private void initialize() {
		itemChangeListener = (v, o, n) -> {
			try {
				LogRunner.logger().info(String.format("Setting id to item %s", n.itemName()));
				action.id(new ItemPersistenceObject(n));
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		amountChangeListener = (v, o, n) -> {
			action.data(n);
		};
		
		setupLists();
		setupBindings();
	}
	
	private void setupLists() {
		selectedPlayers.list().addAll(players.stream().map(PlayerPersistenceObject::playerName).collect(Collectors.toList()));
		
		for (ModificationType m : ModificationType.values()) {
			modifications.list().add(m.toString());
		}
		
		for (ChangeType t : ChangeType.values()) {
			changeTypes.list().add(t.toString());
		}
		
		for (ModificationObject o : ModificationObject.values()) {
			modificationObject.list().add(o.toString());
		}
	}
	
	private void setupBindings() {
		if (action.playerName() != null && !action.playerName().isEmpty()) {
			selectedPlayers.selected().set(action.playerName());
		}
		
		selectedPlayers.selected().addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating selected player to %s", n));
			action.playerName(n);
		});
		
		modificationData.addListener((v, o, n) -> {
			updateModificationData(o, n);
		});
		
		modificationObject.selected().addListener((v, o, n) -> {
			action.modificationObj(ModificationObject.valueOf(n));
			updateIds(ModificationObject.valueOf(n));
		});
		
		if (action.modificationObj() != null) {
			modificationObject.selected().set(action.modificationObj().toString());
		}
		
		modifications.selected().addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating modification to %s", n));
			action.modificationType(ModificationType.valueOf(n));
			if (isRemove()) {
				updateDataToId();
			}
		});
		
		if (action.modificationType() != null) {
			modifications.selected().set(action.modificationType().toString());
		}
		
		if (action.changeType() != null) {
			changeTypes.selected().set(action.changeType().toString());
		}
		
		changeTypes.selected().addListener((v, o, n) -> {
			action.changeType(ChangeType.valueOf(n));
		});
		
		if (action.id() != null) {
			if (action.id() instanceof ItemPersistenceObject) {
				id.selected().set(((ItemPersistenceObject)action.id()).itemName());
			}else if (action.id() instanceof BodyPartPersistenceObject) {
				id.selected().set(((BodyPartPersistenceObject)action.id()).objectName());
			} else if (action.id() instanceof String) {
				id.selected().set(action.id());
			}
		}
		
		id.selected().addListener((v, o, n) -> {
			action.id(n);
			
			if (isEquipmentModification()) {
				action.id(findBodyPart(n));
			}
			
			if (isRemove()) {
				updateDataToId();
			}
		});
	}
	
	/**
	 * 
	 * @return The available players to modify.
	 */
	public SelectionAwareObservableList<String> players() {
		return selectedPlayers;
	}
	
	/**
	 * 
	 * @return The types of modifications.
	 */
	public SelectionAwareObservableList<String> modifications() {
		return modifications;
	}
	
	/**
	 * 
	 * @return The way in which the id can be changed.
	 */
	public SelectionAwareObservableList<String> changeTypes() {
		return changeTypes;
	}
	
	/**
	 * 
	 * @return The type of object to modify
	 */
	public SelectionAwareObservableList<String> modificationObject() {
		return modificationObject;
	}
	
	/**
	 * 
	 * @return The object to modify
	 */
	public SelectionAwareObservableList<String> id() {
		return id;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public ModifyPlayerActionPersistence getPersistenceObject() {
		return action;
	}
	
	/**
	 * 
	 * @return The data to use in the modification.
	 */
	public SimpleObjectProperty<Object> modificationData() {
		return modificationData;
	}
	
	/**
	 * 
	 * @return If this is a change modification.
	 */
	public boolean isChange() {
		return modifications.selected().get() != null && ModificationType.valueOf(modifications.selected().get()) == ModificationType.Change;
	}
	
	/**
	 * 
	 * @return If this is an add modification.
	 */
	public boolean isAdd() {
		return modifications.selected().get() != null && ModificationType.valueOf(modifications.selected().get()) == ModificationType.Add;
	}
	
	/**
	 * 
	 * @return If this is a remove modification.
	 */
	public boolean isRemove() {
		return modifications.selected().get() != null && ModificationType.valueOf(modifications.selected().get()) == ModificationType.Remove;
	}
	
	/**
	 * 
	 * @return If a data member is needed.
	 */
	public boolean needsDataMember() {
		return false;
	}
	
	/**
	 * 
	 * @return If this is a modification to a body part.
	 */
	public boolean isBodyModification() {
		return modificationObject.selected().get() != null && ModificationObject.valueOf(modificationObject.selected().get()) == ModificationObject.BodyPart;
	}
	
	/**
	 * 
	 * @return If this is a modification to equipment.
	 */
	public boolean isEquipmentModification() {
		return modificationObject.selected().get() != null && ModificationObject.valueOf(modificationObject.selected().get()) == ModificationObject.Equipment;
	}
	
	/**
	 * 
	 * @return If the change is an assignment.
	 */
	public boolean isAssign() {
		return changeTypes.selected().get() != null && ChangeType.valueOf(changeTypes.selected().get()) == ChangeType.Assign;
	}
	
	/**
	 * 
	 * @return If the change is a subtraction.
	 */
	public boolean isSubtract() {
		return changeTypes.selected().get() != null && ChangeType.valueOf(changeTypes.selected().get()) == ChangeType.Subtract;
	}
	
	/**
	 * 
	 * @return If the change is an addition.
	 */
	public boolean isAddChange() {
		return changeTypes.selected().get() != null && ChangeType.valueOf(changeTypes.selected().get()) == ChangeType.Add;
	}
	
	/**
	 * 
	 * @return The currently selected body part. If none is selected null will be returned.
	 */
	public BodyPartPersistenceObject getCurrentBodyPart() {
		if (!isBodyModification() || id.selected().get() == null || id.selected().get().isEmpty()) {
			return null;
		}
		
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return null;
		}
		
		Optional<BodyPartPersistenceObject> bodyPart = player.get().bodyParts().stream().filter((b -> b.objectName().equals(id.selected().get()))).findFirst();
		
		if (!bodyPart.isPresent()) {
			return null;
		}
		
		return bodyPart.get();
	}
	
	/**
	 * 
	 * @return The current inventory. If this is not applicable null will be returned.
	 */
	public InventoryPersistenceObject getCurrentInventory() {
		if (!isEquipmentModification()) {
			return null;
		}
		
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(action.playerName())).findFirst();
		if (!player.isPresent()) {
			return null;
		}
		
		return player.get().inventory();
	}
	
	/**
	 * 
	 * @return Display string for player.
	 */
	public SimpleStringProperty playerText() {
		return playerText;
	}
	
	/**
	 * 
	 * @return Display string for property.
	 */
	public SimpleStringProperty propertyText() {
		return propertyText;
	}
	
	/**
	 * 
	 * @return Display string for id.
	 */
	public SimpleStringProperty idText() {
		return idText;
	}
	
	/**
	 * 
	 * @return Display string for data member.
	 */
	public SimpleStringProperty dataMemberText() {
		return dataMemberText;
	}
	
	/**
	 * 
	 * @return Display string for modification type.
	 */
	public SimpleStringProperty modTypeText() {
		return modTypeText;
	}
	
	/**
	 * 
	 * @return Display string for change type.
	 */
	public SimpleStringProperty changeTypeText() {
		return changeTypeText;
	}
	
	/**
	 * 
	 * @return Display string for modification type.
	 */
	public SimpleStringProperty modDataText() {
		return modDataText;
	}
	
	private void updateIds(ModificationObject type) {
		LogRunner.logger().info("Updating id list");
		id.list().clear();
		
		switch (type) {
			case Attribute:
				updateAttributes();
				break;
			case BodyPart:
				updateBodyParts();
				break;
			case Characteristic:
				updateCharacteristics();
				break;
			case Equipment:
				updateEquipment();
				break;
			case Inventory:
				updateInventory();
				break;
			case Player:
				break;
		}
	}
	
	private void updateAttributes() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		LogRunner.logger().info(String.format("Updating ids with attributes for player %s", player.get().playerName()));
		id.list().addAll(player.get().attributes().stream().map(AttributePersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void updateBodyParts() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		LogRunner.logger().info(String.format("Updating ids with body parts for player %s", player.get().playerName()));
		id.list().addAll(player.get().bodyParts().stream().map(BodyPartPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void updateCharacteristics() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		LogRunner.logger().info(String.format("Updating ids with characteristics for player %s", player.get().playerName()));
		id.list().addAll(player.get().characteristics().stream().map(CharacteristicPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void updateEquipment() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		LogRunner.logger().info(String.format("Updating ids with equipment for player %s", player.get().playerName()));
		id.list().addAll(player.get().bodyParts().stream().map(BodyPartPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void updateInventory() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		LogRunner.logger().info(String.format("Updating ids with items for player %s", player.get().playerName()));
		id.list().addAll(player.get().inventory().items().stream().map(ItemPersistenceObject::itemName).collect(Collectors.toList()));
	}
	
	private void updateDataToId() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		switch (action.modificationObj()) {
			case Attribute:
				Optional<AttributePersistenceObject> att = player.get().attributes().stream().filter(a -> a.objectName().equals(id.selected().get())).findFirst();
				if (att.isPresent()) {
					action.data(att.get());
				}
				break;
			case BodyPart:
				Optional<BodyPartPersistenceObject> body = player.get().bodyParts().stream().filter(b -> b.objectName().equals(id.selected().get())).findFirst();
				if (body.isPresent()) {
					action.data(body.get());
				}
				break;
			case Characteristic:
				Optional<CharacteristicPersistenceObject> character = player.get().characteristics().stream().filter(c -> c.objectName().equals(id.selected().get())).findFirst();
				if (character.isPresent()) {
					action.data(character.get());
				}
				break;
			case Equipment:
				Optional<ItemPersistenceObject> eItem = player.get().inventory().items().stream().filter(i -> i.itemName().equals(id.selected().get())).findFirst();
				if (eItem.isPresent()) {
					action.data(eItem.get());
				}
				break;
			case Inventory:
				Optional<ItemPersistenceObject> item = player.get().inventory().items().stream().filter(i -> i.itemName().equals(id.selected().get())).findFirst();
				if (item.isPresent()) {
					try {
						action.data(new ItemPersistenceObject(item.get()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			case Player:
				action.data(player.get());
				break;
		}
	}
	
	private BodyPartPersistenceObject findBodyPart(String id) {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return null;
		}
		
		Optional<BodyPartPersistenceObject> bodyPart = player.get().bodyParts().stream().filter(i -> i.objectName().equals(id)).findFirst();
		if (!bodyPart.isPresent()) {
			return null;
		}
		
		return bodyPart.get();
	}
	
	private void updateModificationData(Object o, Object n) {
		LogRunner.logger().info("Updating modification data.");
		
		if (o instanceof InventoryItem) {
			removeItemBindings((InventoryItem)o);
		}
		
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		
		switch (action.modificationObj()) {
			case Attribute:
			case BodyPart:
			case Characteristic:
			case Equipment:
				action.data(n);
				break;
			case Inventory:
				if (!(n instanceof InventoryItem)) {
					break;
				}
				
				bindItem((InventoryItem)n);
				break;
			case Player:
				action.data(player.get());
				break;
		}
	}
	
	private void bindItem(InventoryItem item) {
		try {
			action.data(item.getAmount());
			action.id(new ItemPersistenceObject(item.getItem()));
			setupItemBindings(item);
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	private void removeItemBindings(InventoryItem item) {
		LogRunner.logger().info(String.format("Removing bindings for item %s", item.getItem().itemName()));
		item.amount().removeListener(amountChangeListener);
		item.item().removeListener(itemChangeListener);
	}
	
	private void setupItemBindings(InventoryItem item) {
		LogRunner.logger().info(String.format("Setting up bindings for item %s", item.getItem().itemName()));
		item.amount().addListener(amountChangeListener);
		item.item().addListener(itemChangeListener);
	}
}
