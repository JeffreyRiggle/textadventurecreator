package ilusr.textadventurecreator.views.player;

import java.util.Map.Entry;
import java.util.List;
import java.util.UUID;

import ilusr.core.interfaces.Callback;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.AttributeFinder;
import ilusr.textadventurecreator.search.AttributeFinderModel;
import ilusr.textadventurecreator.search.BodyPartFinder;
import ilusr.textadventurecreator.search.BodyPartFinderModel;
import ilusr.textadventurecreator.search.CharacteristicFinder;
import ilusr.textadventurecreator.search.CharacteristicFinderModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.EquipPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModel {

	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private PlayerPersistenceObject player;
	private AttributePersistenceObject addAttributeKey;
	private ObservableList<AttributePersistenceObject> attributes;
	private CharacteristicPersistenceObject addCharacteristicKey;
	private ObservableList<CharacteristicPersistenceObject> characteristics;
	private BodyPartPersistenceObject addBodyPartKey;
	private ObservableList<BodyPartPersistenceObject> bodyParts;
	private EquipPersistenceObject addEquipKey;
	private ObservableList<EquipPersistenceObject> equipment;
	private InventoryItem addItemKey;
	private ObservableList<InventoryItem> items;
	private SimpleStringProperty playerID;
	private LanguageAwareString playerText;
	private LanguageAwareString attributeText;
	private LanguageAwareString characteristicText;
	private LanguageAwareString bodyPartText;
	private LanguageAwareString inventoryText;
	private LanguageAwareString equipmentText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param player A @see PlayerPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to provide dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public PlayerModel(IDialogService dialogService, 
					   LibraryService libraryService, 
					   PlayerPersistenceObject player,
					   ILanguageService languageService,
					   IDialogProvider dialogProvider,
					   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.player = player;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		attributes = FXCollections.observableArrayList();
		characteristics = FXCollections.observableArrayList();
		bodyParts = FXCollections.observableArrayList();
		equipment = FXCollections.observableArrayList();
		items = FXCollections.observableArrayList();
		playerID = new SimpleStringProperty(player.playerName());
		playerText = new LanguageAwareString(languageService, DisplayStrings.PLAYER_ID);
		attributeText = new LanguageAwareString(languageService, DisplayStrings.ATTRIBUTES);
		characteristicText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTICS);
		bodyPartText = new LanguageAwareString(languageService, DisplayStrings.BODY_PARTS);
		inventoryText = new LanguageAwareString(languageService, DisplayStrings.INVENTORY);
		equipmentText = new LanguageAwareString(languageService, DisplayStrings.EQUIPMENT);
		valid = new SimpleBooleanProperty(player.playerName() != null && !player.playerName().isEmpty());

		setupPlayer();
		setupKeys();
		bind();
	}
	
	private void setupPlayer() {
		for (AttributePersistenceObject attribute : player.attributes()) {
			attributes.add(attribute);
		}
		
		for (CharacteristicPersistenceObject characteristic : player.characteristics()) {
			characteristics.add(characteristic);
		}
		
		for (BodyPartPersistenceObject bPart : player.bodyParts()) {
			bodyParts.add(bPart);
		}
		
		for (ItemPersistenceObject item : player.inventory().items()) {
			items.add(new InventoryItem(item, player.inventory().getAmount(item)));
		}
		
		for (Entry<BodyPartPersistenceObject, ItemPersistenceObject> equip : player.equipment().equipment().entrySet()) {
			tryAddEquipment(equip);
		}
	}
	
	private void tryAddEquipment(Entry<BodyPartPersistenceObject, ItemPersistenceObject> equip) {
		try {
			EquipPersistenceObject pEquip = new EquipPersistenceObject();
			pEquip.bodyPart(equip.getKey());
			pEquip.item(equip.getValue());
			equipment.add(pEquip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setupKeys() {
		try {
			addAttributeKey = new AttributePersistenceObject();
			addAttributeKey.objectName(UUID.randomUUID().toString());
			
			addCharacteristicKey = new CharacteristicPersistenceObject();
			addCharacteristicKey.objectName(UUID.randomUUID().toString());
			
			addBodyPartKey = new BodyPartPersistenceObject();
			addBodyPartKey.objectName(UUID.randomUUID().toString());
			
			addEquipKey = new EquipPersistenceObject();
			addEquipKey.name(UUID.randomUUID().toString());
			
			addItemKey = new InventoryItem(new ItemPersistenceObject(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bind() {
		playerID.addListener((v, o, n) -> {
			valid.set(n != null && !n.isEmpty());
			player.playerName(n);
		});
		
		attributes.addListener((Change<? extends AttributePersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends AttributePersistenceObject> rAtt = c.getRemoved();
			List<? extends AttributePersistenceObject> aAtt = c.getList();
			
			for (AttributePersistenceObject model : rAtt) {
				if (aAtt.contains(model)) {
					continue;
				}
				
				player.removeAttribute(model);
			}
		});
		
		characteristics.addListener((Change<? extends CharacteristicPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends CharacteristicPersistenceObject> rChar = c.getRemoved();
			List<? extends CharacteristicPersistenceObject> aChar = c.getList();
			
			for (CharacteristicPersistenceObject model : rChar) {
				if (aChar.contains(model)) {
					continue;
				}
				
				player.removeCharacteristic(model);
			}
		});
		
		bodyParts.addListener((Change<? extends BodyPartPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends BodyPartPersistenceObject> rBod = c.getRemoved();
			List<? extends BodyPartPersistenceObject> aBod = c.getList();
			
			for (BodyPartPersistenceObject model : rBod) {
				if (aBod.contains(model)) {
					continue;
				}
				
				player.removeBodyPart(model);
			}
		});
		
		equipment.addListener((Change<? extends EquipPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends EquipPersistenceObject> rEqiup = c.getRemoved();
			List<? extends EquipPersistenceObject> aEqiup = c.getList();
			
			for (EquipPersistenceObject model : rEqiup) {
				if (aEqiup.contains(model)) {
					continue;
				}
				
				player.equipment().unequip(model.bodyPart());
			}
		});
		
		items.addListener((Change<? extends InventoryItem> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends InventoryItem> rItem = c.getRemoved();
			List<? extends InventoryItem> aItem = c.getList();
			
			for (InventoryItem model : rItem) {
				if (aItem.contains(model)) {
					continue;
				}
				
				player.inventory().removeItem(model.getItem());
			}
		});
	}
	
	/**
	 * 
	 * @return If library actions are allowed.
	 */
	public boolean allowLibraryAdd() {
		return libraryService != null;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public PlayerPersistenceObject persistablePlayer() {
		return player;
	}
	
	/**
	 * 
	 * @return The players id.
	 */
	public SimpleStringProperty playerID() {
		return playerID;
	}
	
	/**
	 * 
	 * @return If the player is currently valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
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
	 * @return Display string for attribute.
	 */
	public SimpleStringProperty attributeText() {
		return attributeText;
	}
	
	/**
	 * 
	 * @return Display string for characteristic
	 */
	public SimpleStringProperty characteristicText() {
		return characteristicText;
	}
	
	/**
	 * 
	 * @return Display string for body part.
	 */
	public SimpleStringProperty bodyPartText() {
		return bodyPartText;
	}
	
	/**
	 * 
	 * @return Display string for inventory.
	 */
	public SimpleStringProperty inventoryText() {
		return inventoryText;
	}
	
	/**
	 * 
	 * @return Display string for equipment.
	 */
	public SimpleStringProperty equipmentText() {
		return equipmentText;
	}
	
	/**
	 * 
	 * @return A Attribute to be used as an add key.
	 */
	public AttributePersistenceObject addAttributeKey() {
		return addAttributeKey;
	}
	
	/**
	 * 
	 * @return The attributes associated with this player.
	 */
	public ObservableList<AttributePersistenceObject> attributes() {
		return attributes;
	}
	
	/**
	 * Adds an attribute to this player.
	 */
	public void addAttribute() {
		try {
			LogRunner.logger().info("Adding attribute to player");
			AttributePersistenceObject attribute = new AttributePersistenceObject();
			player.addAttribute(attribute);
			attributes.add(attribute);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempts to add an attribute to this player from a library item.
	 */
	public void addAttributeFromLibrary() {
		try {
			AttributeFinderModel finder = new AttributeFinderModel(libraryService, languageService, dialogService);
			
			Dialog dialog = dialogProvider.create(new AttributeFinder(finder, styleService, urlProvider), () -> {
				if (finder.foundValue() == null) {
					return;
				}
				
				LogRunner.logger().info(String.format("Adding attribute  %s to player", finder.foundValue().objectName()));
				player.addAttribute(finder.foundValue());
				attributes.add(finder.foundValue());
			});
			
			LogRunner.logger().info("Attempting to Add attribute to player from library");
			dialogService.displayModal(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return A characteristic to use as an add key.
	 */
	public CharacteristicPersistenceObject addCharacteristicKey() {
		return addCharacteristicKey;
	}
	
	/**
	 * 
	 * @return The characteristics associated with this player.
	 */
	public ObservableList<CharacteristicPersistenceObject> characteristics() {
		return characteristics;
	}
	
	/**
	 * Adds a characteristic to this player.
	 */
	public void addCharacteristic() {
		try {
			LogRunner.logger().info("Adding characteristic to player");
			CharacteristicPersistenceObject characteristic = new CharacteristicPersistenceObject();
			player.addCharacteristic(characteristic);
			characteristics.add(characteristic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempts to add a characteristic to this player from a library item.
	 */
	public void addCharacteristicFromLibrary() {
		try {
			CharacteristicFinderModel finder = new CharacteristicFinderModel(libraryService, languageService, dialogService);
			
			Dialog dialog = dialogProvider.create(new CharacteristicFinder(finder, styleService, urlProvider), () -> {
				if (finder.foundValue() == null) {
					return;
				}
				
				LogRunner.logger().info(String.format("Adding characteristic %s to player", finder.foundValue().objectName()));
				player.addCharacteristic(finder.foundValue());
				characteristics.add(finder.foundValue());
			});
			
			LogRunner.logger().info("Attempting to add characteristic to player from library item.");
			dialogService.displayModal(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return A body part to use as an add key.
	 */
	public BodyPartPersistenceObject addBodyPartKey() {
		return addBodyPartKey;
	}
	
	/**
	 * 
	 * @return The body parts associated with this player.
	 */
	public ObservableList<BodyPartPersistenceObject> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * Adds a body part to this player.
	 */
	public void addBodyPart() {
		try {
			BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
			BodyPartModel model = new BodyPartModel(bodyPart, libraryService, dialogService, player, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new BodyPartViewer(model, languageService, styleService, urlProvider), () -> {
				player.addBodyPart(bodyPart);
				bodyParts.add(bodyPart);
			});
			dialog.isValid().bind(model.valid());
			
			LogRunner.logger().info("Attempting to add body part to player");
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.BODY_PART));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempts to add a body part to this player from a library item.
	 */
	public void addBodyPartFromLibrary() {
		try {
			BodyPartFinderModel finder = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new BodyPartFinder(finder, styleService, urlProvider), () -> {
				if (finder.foundValue() == null) {
					return;
				}
				
				LogRunner.logger().info(String.format("Adding body part %s to player", finder.foundValue().objectName()));
				player.addBodyPart(finder.foundValue());
				bodyParts.add(finder.foundValue());
			});
			
			LogRunner.logger().info("Attempting to add body part to player from library");
			dialogService.displayModal(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return Equipment to use as an add key.
	 */
	public EquipPersistenceObject addEquipKey() {
		return addEquipKey;
	}
	
	/**
	 * 
	 * @return The equipment associated with this player.
	 */
	public ObservableList<EquipPersistenceObject> equipment() {
		return equipment;
	}
	
	private void addEquipment() {
		try {
			EquipPersistenceObject equip = new EquipPersistenceObject();
			EquipViewer viewer = new EquipViewer(dialogService, libraryService, player, equip, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(viewer, () -> {
				player.equipment().equip(equip.item(), equip.bodyPart());
				equipment.add(equip);
			});
			dialog.isValid().bind(viewer.valid());
			
			LogRunner.logger().info("Attempting to add equipment to player");
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.EQUIPMENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return Item to use as an add key.
	 */
	public InventoryItem addItemKey() {
		return addItemKey;
	}
	
	/**
	 * 
	 * @return Items associated with this player.
	 */
	public ObservableList<InventoryItem> items() {
		return items;
	}
	
	/**
	 * Adds an item to this player.
	 */
	public void addItem() {
		try {
			InventoryItem item = new InventoryItem(new ItemPersistenceObject(), 0);
			InventoryItemViewer viewer = new InventoryItemViewer(dialogService, item, libraryService, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(viewer, () -> {
				player.inventory().addItem(item.getItem(), item.getAmount());
				items.add(item);
			});
			dialog.isValid().bind(item.valid());
			
			LogRunner.logger().info("Attempting to add item to player");
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return A @see Callbackt to run to edit a body part.
	 */
	public Callback<BodyPartPersistenceObject> getEditBodyPartAction() {
		return (bodyPart) -> {
			LogRunner.logger().info(String.format("Editing body part %s", bodyPart.objectName()));
			BodyPartModel model = new BodyPartModel(bodyPart, libraryService, dialogService, player, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new BodyPartViewer(model, languageService, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.BODY_PART));
		};
	}
	
	/**
	 * 
	 * @return A EventHandler to run to add equipment to this player.
	 */
	public EventHandler<ActionEvent> getAddEquipmentAction() {
		return (e) -> { addEquipment(); };
	}
	
	/**
	 * 
	 * @return A Callback to run to edit equipment associated with this player.
	 */
	public Callback<EquipPersistenceObject> getEditEquipmentAction() {
		return (equip) -> {
			LogRunner.logger().info("Editing equipment on player");
			EquipViewer viewer = new EquipViewer(dialogService, libraryService, player, equip, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(viewer);
			dialog.isValid().bind(viewer.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.EQUIPMENT));
		};
	}
	
	/**
	 * 
	 * @return A Callback to run to edit an item.
	 */
	public Callback<InventoryItem> getEditInventoryAction() {
		return (item) -> {
			LogRunner.logger().info(String.format("Editing item %s on player", item.getItem().itemName()));
			
			Dialog dialog = dialogProvider.create(new InventoryItemViewer(dialogService, item, libraryService, languageService, dialogProvider, styleService, urlProvider));
			dialog.isValid().bind(item.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
		};
	}
}
