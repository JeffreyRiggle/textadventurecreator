package ilusr.textadventurecreator.library;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.ActionPersistenceConverter;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TimerPersistenceConverter;
import textadventurelib.persistence.TimerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceConverter;
import textadventurelib.persistence.TriggerPersistenceObject;
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
public class LibraryItem {

	private final TriggerPersistenceConverter triggerConverter;
	private final ActionPersistenceConverter actionConverter;
	private final TimerPersistenceConverter timerConverter;
	
	private List<AttributePersistenceObject> attributes;
	private List<CharacteristicPersistenceObject> characteristics;
	private List<PropertyPersistenceObject> properties;
	private List<ItemPersistenceObject> items;
	private List<BodyPartPersistenceObject> bodyParts;
	private List<PlayerPersistenceObject> players;
	private List<TriggerPersistenceObject> triggers;
	private List<ActionPersistenceObject> actions;
	private List<OptionPersistenceObject> options;
	private List<TimerPersistenceObject> timers;
	private List<GameStatePersistenceObject> gameStates;
	private List<LayoutPersistenceObject> layouts;
	private String name;
	private String author;
	
	/**
	 * Creates a Library Item.
	 */
	public LibraryItem() {
		this(new String(), new String(), new TriggerPersistenceConverter(), new ActionPersistenceConverter(), new TimerPersistenceConverter());
	}
	
	/**
	 * Creates a Library Item from a @see XmlConfigurationObject.
	 * @param config The @see XmlConfigurationObject to load data from.
	 */
	public LibraryItem(XmlConfigurationObject config) {
		this();
		convertFromPersistence(config);
	}
	
	/**
	 * Creates a Library Item with a name.
	 * 
	 * @param name The name of the library item.
	 * @param triggerConverter A @see TriggerPersistenceConverter to convert triggers.
	 * @param actionConverter A @see ActionPersistenceConverter to convert actions.
	 * @param timerConverter A @see TimerPersistenceConverter to convert timers.
	 */
	public LibraryItem(String name, 
					   TriggerPersistenceConverter triggerConverter, 
					   ActionPersistenceConverter actionConverter,
					   TimerPersistenceConverter timerConverter) {
		this(name, new String(), triggerConverter, actionConverter, timerConverter);
	}
	
	/**
	 * Creates a Library Item with a name.
	 * 
	 * @param name The name of the library item.
	 * @param author The author for the library item.
	 * @param triggerConverter A @see TriggerPersistenceConverter to convert triggers.
	 * @param actionConverter A @see ActionPersistenceConverter to convert actions.
	 * @param timerConverter A @see TimerPersistenceConverter to convert timers.
	 */
	public LibraryItem(String name, 
			   		   String author,
					   TriggerPersistenceConverter triggerConverter, 
			   		   ActionPersistenceConverter actionConverter,
			   		   TimerPersistenceConverter timerConverter) {
		this.triggerConverter = triggerConverter;
		this.actionConverter = actionConverter;
		this.timerConverter = timerConverter;
		
		this.name = name;
		this.author = author;
		attributes = new ArrayList<AttributePersistenceObject>();
		characteristics = new ArrayList<CharacteristicPersistenceObject>();
		properties = new ArrayList<PropertyPersistenceObject>();
		items = new ArrayList<ItemPersistenceObject>();
		bodyParts = new ArrayList<BodyPartPersistenceObject>();
		players = new ArrayList<PlayerPersistenceObject>();
		triggers = new ArrayList<TriggerPersistenceObject>();
		actions = new ArrayList<ActionPersistenceObject>();
		options = new ArrayList<OptionPersistenceObject>();
		timers = new ArrayList<TimerPersistenceObject>();
		gameStates = new ArrayList<GameStatePersistenceObject>();
		layouts = new ArrayList<LayoutPersistenceObject>();
	}
	
	/**
	 * 
	 * @return The Libraries name.
	 */
	public String getLibraryName() {
		return name;
	}
	
	/**
	 * 
	 * @param name The new Library name.
	 */
	public void setLibraryName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return The author of the library.
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * 
	 * @param author The new author of the library.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * 
	 * @return A List of @see AttributePersistenceObject associated with this library.
	 */
	public List<AttributePersistenceObject> attributes() {
		return attributes;
	}

	/**
	 * 
	 * @return A List of @see CharacteristicPersistenceObject associated with this library.
	 */
	public List<CharacteristicPersistenceObject> characteristics() {
		return characteristics;
	}
	
	/**
	 * 
	 * @return A List of @see PropertyPersistenceObject associated with this library.
	 */
	public List<PropertyPersistenceObject> properties() {
		return properties;
	}
	
	/**
	 * 
	 * @return A List of @see ItemPersistenceObject associated with this library.
	 */
	public List<ItemPersistenceObject> items() {
		return items;
	}
	
	/**
	 * 
	 * @return A List of @see BodyPartPersistenceObject associated with this library.
	 */
	public List<BodyPartPersistenceObject> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * 
	 * @return A List of @see PlayerPersistenceObject associated with this library.
	 */
	public List<PlayerPersistenceObject> players() {
		return players;
	}
	
	/**
	 * 
	 * @return A List of @see TriggerPersistenceObject associated with this library.
	 */
	public List<TriggerPersistenceObject> triggers() {
		return triggers;
	}
	
	/**
	 * 
	 * @return A List of @see ActionPersistenceObject associated with this library.
	 */
	public List<ActionPersistenceObject> actions() {
		return actions;
	}
	
	/**
	 * 
	 * @return A List of @see OptionPersistenceObject associated with this library.
	 */
	public List<OptionPersistenceObject> options() {
		return options;
	}
	
	/**
	 * 
	 * @return A List of @see TimerPersistenceObject associated with this library.
	 */
	public List<TimerPersistenceObject> timers() {
		return timers;
	}
	
	/**
	 * 
	 * @return A List of @see GameStatePersistenceObject associated with this library.
	 */
	public List<GameStatePersistenceObject> gameStates() {
		return gameStates;
	}
	
	/**
	 * 
	 * @return A List of @see LayoutPersistenceObject associated with this library.
	 */
	public List<LayoutPersistenceObject> layouts() {
		return layouts;
	}
	
	/**
	 * 
	 * @return A @see XmlConfigurationObject representing this library item.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public XmlConfigurationObject toConfigurationObject() throws TransformerConfigurationException, ParserConfigurationException {
		LogRunner.logger().info(String.format("Converting Libary Item: %s to a configuration object", name));
		XmlConfigurationObject retVal = new XmlConfigurationObject("LibraryItem");
		XmlConfigurationObject libName = new XmlConfigurationObject("LibraryName", name);
		retVal.addChild(libName);
		XmlConfigurationObject libAuthor = new XmlConfigurationObject("LibraryAuthor", author);
		retVal.addChild(libAuthor);
		retVal.addChild(buildAttributes());
		retVal.addChild(buildCharacteristics());
		retVal.addChild(buildProperties());
		retVal.addChild(buildItems());
		retVal.addChild(buildBodyParts());
		retVal.addChild(buildPlayers());
		retVal.addChild(buildTriggers());
		retVal.addChild(buildActions());
		retVal.addChild(buildOptions());
		retVal.addChild(buildTimers());
		retVal.addChild(buildGameStates());
		retVal.addChild(buildLayouts());
		
		return retVal;
	}
	
	private void convertFromPersistence(XmlConfigurationObject config) {
		LogRunner.logger().info("Converting persistence object to library item");
		for (PersistXml child : config.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			switch (cChild.name()) {
				case "LibraryName":
					setLibraryName(cChild.value());
					break;
				case "LibraryAuthor":
					setAuthor(cChild.value());
					break;
				case "Attributes":
					convertAttributes(cChild);
					break;
				case "Characteristics":
					convertCharacteristics(cChild);
					break;
				case "Properties":
					convertProperties(cChild);
					break;
				case "Items":
					convertItems(cChild);
					break;
				case "BodyParts":
					convertBodyParts(cChild);
					break;
				case "Players":
					convertPlayers(cChild);
					break;
				case "Triggers":
					convertTriggers(cChild);
					break;
				case "Actions":
					convertActions(cChild);
					break;
				case "Options":
					convertOptions(cChild);
					break;
				case "Timers":
					convertTimers(cChild);
					break;
				case "GameStates":
					convertGameStates(cChild);
					break;
				case "Layouts":
					convertLayouts(cChild);
					break;
			}
		}
	}
	
	private XmlConfigurationObject buildAttributes() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Attributes");
		for (AttributePersistenceObject att : attributes) {
			att.prepareXml();
			retVal.addChild(att);
		}
		
		return retVal;
	}
	
	private void convertAttributes(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				AttributePersistenceObject att = new AttributePersistenceObject();
				att.convertFromPersistence((XmlConfigurationObject)child);
				attributes.add(att);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildCharacteristics() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Characteristics");
		for (CharacteristicPersistenceObject chr : characteristics) {
			chr.prepareXml();
			retVal.addChild(chr);
		}
		
		return retVal;
	}
	
	private void convertCharacteristics(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				CharacteristicPersistenceObject chr = new CharacteristicPersistenceObject();
				chr.convertFromPersistence((XmlConfigurationObject)child);
				characteristics.add(chr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildProperties() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Properties");
		for (PropertyPersistenceObject prop : properties) {
			prop.prepareXml();
			retVal.addChild(prop);
		}
		
		return retVal;
	}
	
	private void convertProperties(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				PropertyPersistenceObject prop = new PropertyPersistenceObject();
				prop.convertFromPersistence((XmlConfigurationObject)child);
				properties.add(prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildItems() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Items");
		for (ItemPersistenceObject item : items) {
			item.prepareXml();
			retVal.addChild(item);
		}
		
		return retVal;
	}
	
	private void convertItems(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				ItemPersistenceObject item = new ItemPersistenceObject();
				item.convertFromPersistence((XmlConfigurationObject)child);
				items.add(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildBodyParts() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("BodyParts");
		for (BodyPartPersistenceObject part : bodyParts) {
			part.prepareXml();
			retVal.addChild(part);
		}
		
		return retVal;
	}
	
	private void convertBodyParts(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				BodyPartPersistenceObject part = new BodyPartPersistenceObject();
				part.convertFromPersistence((XmlConfigurationObject)child);
				bodyParts.add(part);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildPlayers() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Players");
		for (PlayerPersistenceObject player : players) {
			player.prepareXml();
			retVal.addChild(player);
		}
		
		return retVal;
	}
	
	private void convertPlayers(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				PlayerPersistenceObject player = new PlayerPersistenceObject();
				player.convertFromPersistence((XmlConfigurationObject)child);
				players.add(player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildTriggers() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Triggers");
		for (TriggerPersistenceObject trigger : triggers) {
			trigger.prepareXml();
			retVal.addChild(trigger);
		}
		
		return retVal;
	}
	
	private void convertTriggers(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				TriggerPersistenceObject trigger = triggerConverter.convert((XmlConfigurationObject)child);
				if (trigger != null) {
					triggers.add(trigger);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildActions() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Actions");
		for (ActionPersistenceObject action : actions) {
			action.prepareXml();
			retVal.addChild(action);
		}
		
		return retVal;
	}
	
	private void convertActions(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				ActionPersistenceObject action = actionConverter.convert((XmlConfigurationObject)child);
				if (action != null) {
					actions.add(action);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildOptions() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Options");
		for (OptionPersistenceObject option : options) {
			option.prepareXml();
			retVal.addChild(option);
		}
		
		return retVal;
	}
	
	private void convertOptions(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				OptionPersistenceObject option = new OptionPersistenceObject();
				option.convertFromPersistence((XmlConfigurationObject)child);
				options.add(option);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildTimers() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Timers");
		for (TimerPersistenceObject timer : timers) {
			timer.prepareXml();
			retVal.addChild(timer);
		}
		
		return retVal;
	}
	
	private void convertTimers(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				TimerPersistenceObject timer = timerConverter.convert((XmlConfigurationObject)child);
				if (timer != null) {
					timers.add(timer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private XmlConfigurationObject buildGameStates() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("GameStates");
		for (GameStatePersistenceObject gameState : gameStates) {
			gameState.prepareXml();
			retVal.addChild(gameState);
		}
		
		return retVal;
	}
	
	private void convertGameStates(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				GameStatePersistenceObject gameState = new GameStatePersistenceObject(new String());
				gameState.convertFromPersistence((XmlConfigurationObject)child);
				gameStates.add(gameState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private XmlConfigurationObject buildLayouts() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject("Layouts");
		for (LayoutPersistenceObject layout : layouts) {
			layout.prepareXml();
			retVal.addChild(layout);
		}
		
		return retVal;
	}
	
	private void convertLayouts(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			try {
				LayoutPersistenceObject layout = new LayoutPersistenceObject();
				layout.convertFromPersistence((XmlConfigurationObject)child);
				layouts.add(layout);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
