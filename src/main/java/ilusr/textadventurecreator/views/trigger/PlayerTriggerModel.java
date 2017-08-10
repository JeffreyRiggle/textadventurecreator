package ilusr.textadventurecreator.views.trigger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
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
public class PlayerTriggerModel {

	private final String ATTRIBUTE = "Attribute";
	private final String BODY_PART = "Body Part";
	private final String CHARACTERISTIC = "Characteristic";
	private final String ITEM = "Item";
	private final String NAME = "Name";
	private final String DESCRIPTION = "Description";
	private final String VALUE = "Value";
	private final String PROPERTIES = "Properties";
	private final List<PlayerPersistenceObject> players;
	
	private SelectionAwareObservableList<String> selectedPlayers;
	private SelectionAwareObservableList<String> properties;
	private SelectionAwareObservableList<String> ids;
	private SelectionAwareObservableList<String> additionalIds;
	private SelectionAwareObservableList<String> dataMembers;
	private SelectionAwareObservableList<String> additionalDataMembers;
	private SelectionAwareObservableList<String> conditions;
	private SimpleStringProperty expectedValue;
	private PlayerTriggerPersistenceObject trigger;
	private LanguageAwareString playerText;
	private LanguageAwareString propertyText;
	private LanguageAwareString idText;
	private LanguageAwareString additionalIdText;
	private LanguageAwareString dataMemberText;
	private LanguageAwareString additionalDataMemberText;
	private LanguageAwareString conditionText;
	private LanguageAwareString expectedText;
	
	/**
	 * 
	 * @param trigger The trigger to bind to.
	 * @param players A list of players to use for the trigger.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayerTriggerModel(PlayerTriggerPersistenceObject trigger, 
							  List<PlayerPersistenceObject> players,
							  ILanguageService languageService) {
		this.trigger = trigger;
		this.players = players;
		
		expectedValue = new SimpleStringProperty();
		selectedPlayers = new SelectionAwareObservableList<String>();
		properties = new SelectionAwareObservableList<String>();
		ids = new SelectionAwareObservableList<String>();
		additionalIds = new SelectionAwareObservableList<String>();
		dataMembers = new SelectionAwareObservableList<String>();
		additionalDataMembers = new SelectionAwareObservableList<String>();
		conditions = new SelectionAwareObservableList<String>();
		playerText = new LanguageAwareString(languageService, DisplayStrings.PLAYER);
		propertyText = new LanguageAwareString(languageService, DisplayStrings.PROPERTY);
		idText = new LanguageAwareString(languageService, DisplayStrings.ID);
		additionalIdText = new LanguageAwareString(languageService, DisplayStrings.ADDITIONAL_ID);
		dataMemberText = new LanguageAwareString(languageService, DisplayStrings.DATA_MEMBER);
		additionalDataMemberText = new LanguageAwareString(languageService, DisplayStrings.ADDITIONAL_DATA_MEMBER);
		conditionText = new LanguageAwareString(languageService, DisplayStrings.CONDITION);
		expectedText = new LanguageAwareString(languageService, DisplayStrings.EXPECTED_VALUE);
		
		initialize();
	}
	
	private void initialize() {
		setupLists();
		setupBindings();
	}
	
	private void setupLists() {
		selectedPlayers.list().addAll(players.stream().map(PlayerPersistenceObject::playerName).collect(Collectors.toList()));
		
		properties.list().add(ATTRIBUTE);
		properties.list().add(BODY_PART);
		properties.list().add(CHARACTERISTIC);
		properties.list().add(ITEM);
		properties.list().add(NAME);
		
		for (Condition c : Condition.values()) {
			conditions.list().add(c.toString());
		}
		
		setupAdditionalDataMembers();
	}
	
	private void setupBindings() {
		bindPlayer();
		bindProperties();
		bindIds();
		bindDataMembers();
		bindConditions();
		bindExpectedValue();
	}
	
	private void bindPlayer() {
		if (trigger.playerName() != null && !trigger.playerName().isEmpty()) {
			selectedPlayers.selected().set(trigger.playerName());
		}
		
		selectedPlayers.selected().addListener((v, o, n) -> {
			this.trigger.playerName(n);
		});
	}
	
	private void bindProperties() {
		properties.selected().addListener((v, o, n) -> {
			updateIDs(n);
			updateDataMembers(n);
			updateModificationObject(n);
		});
		
		properties.selected().set(convertModificationObject(trigger.modificationObject()));
	}
	
	private void bindIds() {
		String[] tids = trigger.id();
		
		ids.selected().addListener((v, o, n) -> {
			updateAdditionalIds();
			trigger.id(new String[] {n});
		});
		
		if (tids != null && tids.length > 0) {
			ids.selected().set(trigger.id()[0]);
		}
		
		additionalIds.selected().addListener((v, o, n) -> {
			if (n == null || n.isEmpty()) {
				return;
			}
			
			trigger.id(new String[] { ids.selected().get(), n});
		});
		
		if (tids != null && tids.length > 1) {
			additionalIds.selected().set(tids[1]);
		}
	}
	
	private void bindDataMembers() {
		selectDataMembers();
		
		dataMembers.selected().addListener((v, o, n) -> {
			trigger.dataMember(n);
		});
		
		additionalDataMembers.selected().addListener((v, o, n) -> {
			if (additionalIdsNeeded()) {
				trigger.dataMember(n);
			}
		});
	}
	
	private void selectDataMembers() {
		boolean hasAdditionalId = additionalIds.selected().get() != null && !additionalIds.selected().get().isEmpty();
		
		if (!hasAdditionalId) {
			dataMembers.selected().set(trigger.dataMember());
			return;
		}
		
		if (properties.selected().get() == BODY_PART) {
			dataMembers.selected().set(CHARACTERISTIC);
		} else {
			dataMembers.selected().set(PROPERTIES);
		}
		
		additionalDataMembers.selected().set(trigger.dataMember());
	}
	
	private void bindConditions() {
		conditions.selected().set(trigger.condition().toString());
		conditions.selected().addListener((v, o, n) -> {
			trigger.condition(Condition.valueOf(n));
		});
	}
	
	private void bindExpectedValue() {
		if (trigger.comparisonData() != null) {
			expectedValue.set(trigger.comparisonData());
		}
		
		expectedValue.addListener((v, o, n) -> {
			trigger.comparisonData(n);
		});
	}
	
	private String convertModificationObject(ModificationObject mod) {
		String retVal = new String();
		
		switch (mod) {
			case Attribute:
				retVal = ATTRIBUTE;
				break;
			case BodyPart:
				retVal = BODY_PART;
				break;
			case Characteristic:
				retVal = CHARACTERISTIC;
				break;
			case Equipment:
			case Inventory:
				retVal = ITEM;
				break;
			case Player:
				retVal = NAME;
				break;
		}
		
		return retVal;
	}
	
	private void updateModificationObject(String mod) {
		switch (mod) {
			case ATTRIBUTE:
				trigger.modificationObject(ModificationObject.Attribute);
				break;
			case BODY_PART:
				trigger.modificationObject(ModificationObject.BodyPart);
				break;
			case CHARACTERISTIC:
				trigger.modificationObject(ModificationObject.Characteristic);
				break;
			case ITEM:
				//TODO Audit.
				trigger.modificationObject(ModificationObject.Inventory);
				break;
			case NAME:
				trigger.modificationObject(ModificationObject.Player);
				break;
		}
	}
	
	/**
	 * 
	 * @return The players available to this trigger.
	 */
	public SelectionAwareObservableList<String> players() {
		return selectedPlayers;
	}
	
	/**
	 * 
	 * @return The property type to use from the player
	 */
	public SelectionAwareObservableList<String> properties() {
		return properties;
	}
	
	/**
	 * 
	 * @return The id to trigger off of.
	 */
	public SelectionAwareObservableList<String> ids() {
		return ids;
	}
	
	/**
	 * 
	 * @return The additional id to trigger off of.
	 */
	public SelectionAwareObservableList<String> additionalIds() {
		return additionalIds;
	}
	
	/**
	 * 
	 * @return The data members associated with the selected id.
	 */
	public SelectionAwareObservableList<String> dataMembers() {
		return dataMembers;
	}
	
	/**
	 * 
	 * @return The data members associated with the additional id.
	 */
	public SelectionAwareObservableList<String> additionalDataMembers() {
		return additionalDataMembers;
	}
	
	/**
	 * 
	 * @return The condition to use for the trigger.
	 */
	public SelectionAwareObservableList<String> conditions() {
		return conditions;
	}
	
	/**
	 * 
	 * @return The expected value for the trigger to fire.
	 */
	public SimpleStringProperty expectedValue() {
		return expectedValue;
	}
	
	/**
	 * 
	 * @return If additional ids are needed for selected id and data member.
	 */
	public boolean additionalIdsNeeded() {
		String dataMember = dataMembers.selected().get();
		if (dataMember == null) {
			return false;
		}
		
		if (!additionalIds.list().isEmpty() && (dataMember.equals(PROPERTIES) || dataMember.equals(CHARACTERISTIC))) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return If the id and data member are needed.
	 */
	public boolean idAndDataMemberNeeded() {
		String prop = properties.selected().get();
		
		if (prop == null || prop.isEmpty() || prop.equals(NAME)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return The associated trigger.
	 */
	public PlayerTriggerPersistenceObject persistenceObject() {
		return trigger;
	}
	
	private void updateIDs(String property) {
		ids.list().clear();
		
		switch (property) {
			case ATTRIBUTE:
				setupAttributeIDs();
				break;
			case CHARACTERISTIC:
				setupCharacteristicIDs();
				break;
			case BODY_PART:
				setupBodyPartIDs();
				break;
			case ITEM:
				setupItemIDs();
				break;
			case NAME:
				break;
		}
	}
	
	private void setupAttributeIDs() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		ids.list().addAll(player.get().attributes().stream().map(AttributePersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void setupCharacteristicIDs() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		ids.list().addAll(player.get().characteristics().stream().map(CharacteristicPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void setupBodyPartIDs() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		ids.list().addAll(player.get().bodyParts().stream().map(BodyPartPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void setupItemIDs() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		ids.list().addAll(player.get().inventory().items().stream().map(ItemPersistenceObject::itemName).collect(Collectors.toList()));
	}
	
	private void updateAdditionalIds() {
		additionalIds.list().clear();
		String prop = properties.selected().get();
		if (!prop.equals(BODY_PART) && !prop.equals(ITEM)) {
			return;
		}
		
		if (prop.equals(ITEM)) {
			setupAdditionalItemIds();
		} else {
			setupAdditionalBodyPartIds();
		}
	}
	
	private void setupAdditionalItemIds() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		Optional<ItemPersistenceObject> item = player.get().inventory().items().stream().filter(i -> i.itemName().equals(ids.selected().get())).findFirst();
		if (!item.isPresent()) {
			return;
		}
		
		additionalIds.list().addAll(item.get().properties().stream().map(PropertyPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void setupAdditionalBodyPartIds() {
		Optional<PlayerPersistenceObject> player = players.stream().filter(p -> p.playerName().equals(selectedPlayers.selected().get())).findFirst();
		if (!player.isPresent()) {
			return;
		}
		
		Optional<BodyPartPersistenceObject> bPart = player.get().bodyParts().stream().filter(b -> b.objectName().equals(ids.selected().get())).findFirst();
		if (!bPart.isPresent()) {
			return;
		}
		
		additionalIds.list().addAll(bPart.get().characteristics().stream().map(CharacteristicPersistenceObject::objectName).collect(Collectors.toList()));
	}
	
	private void updateDataMembers(String property) {
		dataMembers.list().clear();
		
		switch (property) {
			case ATTRIBUTE:
			case CHARACTERISTIC:
				setupFullDataMembers();
				break;
			case BODY_PART:
				setupBodyPartMembers();
				break;
			case ITEM:
				setupItemMembers();
				break;
			case NAME:
				break;
		}
	}
	
	private void setupAdditionalDataMembers() {
		additionalDataMembers.list().add(NAME);
		additionalDataMembers.list().add(DESCRIPTION);
		additionalDataMembers.list().add(VALUE);
	}
	
	private void setupFullDataMembers() {
		dataMembers.list().add(NAME);
		dataMembers.list().add(DESCRIPTION);
		dataMembers.list().add(VALUE);
	}
	
	private void setupBodyPartMembers() {
		dataMembers.list().add(NAME);
		dataMembers.list().add(DESCRIPTION);
		dataMembers.list().add(CHARACTERISTIC);
	}
	
	private void setupItemMembers() {
		dataMembers.list().add(NAME);
		dataMembers.list().add(DESCRIPTION);
		dataMembers.list().add(PROPERTIES);
	}
	
	/**
	 * 
	 * @return The display string for player.
	 */
	public SimpleStringProperty playerText() {
		return playerText;
	}
	
	/**
	 * 
	 * @return The display string for property.
	 */
	public SimpleStringProperty propertyText() {
		return propertyText;
	}
	
	/**
	 * 
	 * @return The display string for id.
	 */
	public SimpleStringProperty idText() {
		return idText;
	}
	
	/**
	 * 
	 * @return The display string for additional id.
	 */
	public SimpleStringProperty additionalIdText() {
		return additionalIdText;
	}
	
	/**
	 * 
	 * @return The display string for data member.
	 */
	public SimpleStringProperty dataMemberText() {
		return dataMemberText;
	}
	
	/**
	 * 
	 * @return The display string for additional data member.
	 */
	public SimpleStringProperty additionalDataMemberText() {
		return additionalDataMemberText;
	}
	
	/**
	 * 
	 * @return The display string for condition.
	 */
	public SimpleStringProperty conditionText() {
		return conditionText;
	}
	
	/**
	 * 
	 * @return The display string for expected value.
	 */
	public SimpleStringProperty expectedValueText() {
		return expectedText;
	}
}
