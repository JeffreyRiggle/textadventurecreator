package ilusr.textadventurecreator.views.macro;

import java.util.ArrayList;
import java.util.List;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import ilusr.textadventurecreator.views.action.PropertyType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MacroBuilderModel {
	
	private final String ATTRIBUTE = "Attribute";
	private final String BODY_PART = "Body Part";
	private final String CHARACTERISTIC = "Characteristic";
	private final String ITEM = "Item";
	private final String ID = "ID";
	private final String NAME = "Name";
	private final String DESCRIPTION = "Description";
	private final String VALUE = "Value";
	private final String PROPERTIES = "Properties";
	
	private final List<PlayerPersistenceObject> players;
	private final List<String> propertyTypes;
	private final List<String> namedProperties;
	private final SelectionAwareObservableList<String> itemOptions;
	private final SelectionAwareObservableList<String> bodyPartOptions;
	
	private SimpleStringProperty selectedPlayer;
	private SimpleStringProperty selectedProperty;
	private SimpleStringProperty selectedPropertyType;
	private SelectionAwareObservableList<String> attributes;
	private SelectionAwareObservableList<String> characteristics;
	private SelectionAwareObservableList<String> bodyParts;
	private SelectionAwareObservableList<String> items;
	private SelectionAwareObservableList<String> properties;
	private String currentMacro;
	private LanguageAwareString buildText;
	private LanguageAwareString macroText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param players The players to use to build a macro.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MacroBuilderModel(List<PlayerPersistenceObject> players, ILanguageService languageService) {
		this.players = players;
		
		selectedPlayer = new SimpleStringProperty();
		selectedPlayer.addListener((v,o,n) -> { playerChanged(n); });
		
		selectedProperty = new SimpleStringProperty();
		selectedPropertyType = new SimpleStringProperty();
		
		attributes = new SelectionAwareObservableList<String>();
		characteristics = new SelectionAwareObservableList<String>();
		bodyParts = new SelectionAwareObservableList<String>();
		bodyParts.selected().addListener((v,o,n) -> { bodyPartChanged(n); });
		items = new SelectionAwareObservableList<String>();
		items.selected().addListener((v,o,n) -> { itemChanged(n); });
		properties = new SelectionAwareObservableList<String>();
		buildText = new LanguageAwareString(languageService, DisplayStrings.BUILD);
		macroText = new LanguageAwareString(languageService, DisplayStrings.MACRO_WITH_COLON);
		valid = new SimpleBooleanProperty();
		
		propertyTypes = new ArrayList<String>();
		propertyTypes.add(ID);
		propertyTypes.add(ATTRIBUTE);
		propertyTypes.add(BODY_PART);
		propertyTypes.add(CHARACTERISTIC);
		propertyTypes.add(ITEM);
		
		namedProperties = new ArrayList<String>();
		namedProperties.add(NAME);
		namedProperties.add(DESCRIPTION);
		namedProperties.add(VALUE);
		
		itemOptions = new SelectionAwareObservableList<String>();
		itemOptions.list().add(NAME);
		itemOptions.list().add(DESCRIPTION);
		itemOptions.list().add(PROPERTIES);
		
		bodyPartOptions = new SelectionAwareObservableList<String>();
		bodyPartOptions.list().add(NAME);
		bodyPartOptions.list().add(DESCRIPTION);
		bodyPartOptions.list().add(CHARACTERISTIC);
	}
	
	/**
	 * 
	 * @return The players to use for the macro.
	 */
	public List<PlayerPersistenceObject> players() {
		return players;
	}
	
	/**
	 * 
	 * @return The selected player to use for the macro.
	 */
	public SimpleStringProperty selectedPlayer() {
		return selectedPlayer;
	}
	
	/**
	 * 
	 * @return The attributes available for the selected player.
	 */
	public SelectionAwareObservableList<String> attributes() {
		return attributes;
	}
	
	/**
	 * 
	 * @return The characteristics available for the selected player.
	 */
	public SelectionAwareObservableList<String> characteristics() {
		return characteristics;
	}
	
	/**
	 * 
	 * @return The body parts available for the selected player.
	 */
	public SelectionAwareObservableList<String> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * 
	 * @return The items available for the selected player.
	 */
	public SelectionAwareObservableList<String> items() {
		return items;
	}
	
	/**
	 * 
	 * @return The properties available for the selected player.
	 */
	public SelectionAwareObservableList<String> properties() {
		return properties;
	}
	
	/**
	 * 
	 * @return The properties that can be used for the player.
	 */
	public List<String> propertyTypes() {
		return propertyTypes;
	}
	
	/**
	 * 
	 * @return The selected property to use for the player.
	 */
	public SimpleStringProperty selectedPropertyType() {
		return selectedPropertyType;
	}
	
	/**
	 * 
	 * @return The additional properties for the player.
	 */
	public List<String> namedPropertyTypes() {
		return namedProperties;
	}
	
	/**
	 * 
	 * @return The selected additional property for the player.
	 */
	public SimpleStringProperty selectedNamedProperty() {
		return selectedProperty;
	}
	
	/**
	 * 
	 * @return The properties to pull from the selected item.
	 */
	public SelectionAwareObservableList<String> itemOptions() {
		return itemOptions;
	}
	
	/**
	 * 
	 * @return The properties to pull from the body part.
	 */
	public SelectionAwareObservableList<String> bodyPartOptions() {
		return bodyPartOptions;
	}
	
	/**
	 * 
	 * @return Display text for build.
	 */
	public SimpleStringProperty buildText() {
		return buildText;
	}
	
	/**
	 * 
	 * @return Display text for macro.
	 */
	public SimpleStringProperty macroText() {
		return macroText;
	}
	
	/**
	 * 
	 * @param prop The property name.
	 * @return The associated @see PropertyType.
	 */
	public PropertyType convertPropertyType(String prop) {
		switch (prop) {
			case ATTRIBUTE:
				return PropertyType.Attribute;
			case BODY_PART:
				return PropertyType.BodyPart;
			case CHARACTERISTIC:
				return PropertyType.Characteristic;
			case ITEM:
				return PropertyType.Item;
			case ID:
				return PropertyType.ID;
			case PROPERTIES:
				return PropertyType.Property;
			default:
				return PropertyType.ID;	
		}
	}
	
	/**
	 * 
	 * @return The last build macro.
	 */
	public String currentMacro() {
		return currentMacro;
	}
	
	/**
	 * 
	 * @return The macro built off of the selected player and properties.
	 */
	public String createMacro() {
		LogRunner.logger().info("Building macro");
		StringBuilder builder = new StringBuilder();
		PlayerPersistenceObject player = findPlayer(selectedPlayer.get());
		
		if (player == null) {
			LogRunner.logger().info(String.format("Unable to find player %s", selectedPlayer.get()));
			valid.set(false);
			return "Unable to find player";
		}
		
		LogRunner.logger().info(String.format("Building macro for player %s", selectedPlayer.get()));
		builder.append("{[");
		builder.append("player");
		builder.append("<<");
		builder.append(selectedPlayer.get());
		builder.append(">>");
		builder.append("@");
		
		switch (convertPropertyType(selectedPropertyType.get())) {
			case Attribute:
				buildAttribute(builder, player);
				break;
			case BodyPart:
				buildBodyPart(builder, player);
				break;
			case ID:
				builder.append("name");
				break;
			case Characteristic:
				buildCharacteristic(builder, findCharacteristic(player, characteristics.selected().get()));
				break;
			case Property:
				break;
			case Item:
				buildItem(builder, player);
				break;
		}
		
		builder.append("]}");
		currentMacro = builder.toString();
		valid.set(true);
		return currentMacro;
	}
	
	/**
	 * 
	 * @return If the macro has been built successfully.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	private void buildAttribute(StringBuilder builder, PlayerPersistenceObject player) {
		AttributePersistenceObject attribute = findAttribute(player, attributes.selected().get());
		
		if (attribute == null) {
			LogRunner.logger().info(String.format("Unable to find attribute %s", attributes.selected().get()));
			return;
		}

		LogRunner.logger().info(String.format("Building attribute %s", attributes.selected().get()));
		builder.append("attribute");
		builder.append("<<");
		builder.append(attribute.objectName());
		builder.append(">>");
		buildPropertyType(builder, selectedProperty.get());
	}
	
	private void buildCharacteristic(StringBuilder builder, CharacteristicPersistenceObject characteristic) {
		if (characteristic == null) {
			return;
		}
		
		builder.append("characteristic");
		builder.append("<<");
		builder.append(characteristic.objectName());
		builder.append(">>");
		buildPropertyType(builder, selectedProperty.get());
	}
	
	private void buildItem(StringBuilder builder, PlayerPersistenceObject player) {
		ItemPersistenceObject item = findItem(player, items.selected().get());
		
		if (item == null) {
			LogRunner.logger().info(String.format("Unable to find item %s", items.selected().get()));
			return;
		}
		
		LogRunner.logger().info(String.format("Building item %s", items.selected().get()));
		builder.append("inventory");
		builder.append("<<");
		builder.append(item.itemName());
		builder.append(">>");
		
		if (!itemOptions.selected().get().equals(PROPERTIES)) {
			buildPropertyType(builder, itemOptions.selected().get());
			return;
		}
		
		buildProperty(builder, item);
	}
	
	private void buildProperty(StringBuilder builder, ItemPersistenceObject item) {
		PropertyPersistenceObject prop = findProperty(item, properties.selected().get());
		
		if (prop == null) {
			LogRunner.logger().info(String.format("Unable to find property %s", properties.selected().get()));
			return;
		}
		
		LogRunner.logger().info(String.format("Building property %s", properties.selected().get()));
		builder.append("@");
		builder.append("property");
		builder.append("<<");
		builder.append(properties.selected().get());
		builder.append(">>");
		
		buildPropertyType(builder, selectedProperty.get());
	}
	
	private void buildBodyPart(StringBuilder builder, PlayerPersistenceObject player) {
		BodyPartPersistenceObject bPart = findBodyPart(player, bodyParts.selected().get());
		
		if (bPart == null) {
			LogRunner.logger().info(String.format("Unable to find body part %s", bodyParts.selected().get()));
			return;
		}
		
		LogRunner.logger().info(String.format("Building body part %s", attributes.selected().get()));
		builder.append("bodyPart");
		builder.append("<<");
		builder.append(bPart.objectName());
		builder.append(">>");
		
		if (!bodyPartOptions.selected().get().equals(CHARACTERISTIC)) {
			buildPropertyType(builder, itemOptions.selected().get());
			return;
		}
		
		builder.append("@");
		buildCharacteristic(builder, findCharacteristic(bPart, characteristics.selected().get()));
	}
	
	private void buildPropertyType(StringBuilder builder, String prop) {
		builder.append("@");
		switch (prop) {
			case NAME:
				builder.append("name");
				break;
			case DESCRIPTION:
				builder.append("description");
				break;
			case VALUE:
				builder.append("value");
				break;
		}
	}
	
	private void playerChanged(String player) {
		for (PlayerPersistenceObject p : players) {
			if (!p.playerName().equals(player)) {
				continue;
			}
			
			LogRunner.logger().info(String.format("Player changed to %s", p.playerName()));
			updateAttributes(p);
			updateCharacteristics(p);
			updateBodyParts(p);
			updateItems(p);
			break;
		}
	}
	
	private PlayerPersistenceObject findPlayer(String player) {
		PlayerPersistenceObject retVal = null;
		for (PlayerPersistenceObject p : players) {
			if (p.playerName().equals(player)) {
				retVal = p;
				break;
			}
		}
		
		return retVal;
	}
	
	private AttributePersistenceObject findAttribute(PlayerPersistenceObject player, String attribute) {
		AttributePersistenceObject retVal = null;
		for (AttributePersistenceObject att : player.attributes()) {
			if (att.objectName().equals(attribute)) {
				retVal = att;
				break;
			}
		}
		
		return retVal;
	}
	
	private CharacteristicPersistenceObject findCharacteristic(PlayerPersistenceObject player, String characteristic) {
		CharacteristicPersistenceObject retVal = null;
		for (CharacteristicPersistenceObject chr : player.characteristics()) {
			if (chr.objectName().equals(characteristic)) {
				retVal = chr;
				break;
			}
		}
		
		return retVal;
	}
	
	private CharacteristicPersistenceObject findCharacteristic(BodyPartPersistenceObject bPart, String characteristic) {
		CharacteristicPersistenceObject retVal = null;
		for (CharacteristicPersistenceObject chr : bPart.characteristics()) {
			if (chr.objectName().equals(characteristic)) {
				retVal = chr;
				break;
			}
		}
		
		return retVal;
	}
	
	private ItemPersistenceObject findItem(PlayerPersistenceObject player, String item) {
		ItemPersistenceObject retVal = null;
		for (ItemPersistenceObject pItem : player.inventory().items()) {
			if (pItem.itemName().equals(item)) {
				retVal = pItem;
				break;
			}
		}
		
		return retVal;
	}
	
	private PropertyPersistenceObject findProperty(ItemPersistenceObject item, String prop) {
		PropertyPersistenceObject retVal = null;
		for (PropertyPersistenceObject pProp : item.properties()) {
			if (pProp.objectName().equals(prop)) {
				retVal = pProp;
				break;
			}
		}
		
		return retVal;
	}
	
	private BodyPartPersistenceObject findBodyPart(PlayerPersistenceObject player, String part) {
		BodyPartPersistenceObject retVal = null;
		for (BodyPartPersistenceObject bPart : player.bodyParts()) {
			if (bPart.objectName().equals(part)) {
				retVal = bPart;
				break;
			}
		}
		
		return retVal;
	}
	
	private void updateAttributes(PlayerPersistenceObject player) {
		attributes.list().clear();
		for (AttributePersistenceObject attribute : player.attributes()) {
			attributes.list().add(attribute.objectName());
		}
	}
	
	private void updateCharacteristics(PlayerPersistenceObject player) {
		characteristics.list().clear();
		for (CharacteristicPersistenceObject characteristic : player.characteristics()) {
			characteristics.list().add(characteristic.objectName());
		}
	}

	private void updateCharacteristics(BodyPartPersistenceObject bodyPart) {
		characteristics.list().clear();
		for (CharacteristicPersistenceObject characteristic : bodyPart.characteristics()) {
			characteristics.list().add(characteristic.objectName());
		}
	}
	
	private void updateBodyParts(PlayerPersistenceObject player) {
		bodyParts.list().clear();
		for (BodyPartPersistenceObject bodyPart : player.bodyParts()) {
			bodyParts.list().add(bodyPart.objectName());
		}
	}

	private void updateItems(PlayerPersistenceObject player) {
		items.list().clear();
		for (ItemPersistenceObject item : player.inventory().items()) {
			items.list().add(item.itemName());
		}
	}
	
	private void itemChanged(String item) {
		PlayerPersistenceObject player = findPlayer(selectedPlayer.get());
		
		if (player == null) {
			return;
		}
		
		ItemPersistenceObject pItem = findItem(player, item);
		
		if (pItem != null) {
			updateProperties(pItem);
		}
	}
	
	private void bodyPartChanged(String bPart) {
		PlayerPersistenceObject player = findPlayer(selectedPlayer.get());
		
		if (player == null) {
			return;
		}
		
		BodyPartPersistenceObject part = findBodyPart(player, bPart);
		
		if (part != null) {
			updateCharacteristics(part);
		}
	}
	
	private void updateProperties(ItemPersistenceObject item) {
		properties.list().clear();
		for (PropertyPersistenceObject prop : item.properties()) {
			properties.list().add(prop.objectName());
		}
	}
}
