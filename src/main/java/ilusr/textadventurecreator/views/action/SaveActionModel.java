package ilusr.textadventurecreator.views.action;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.SaveActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveActionModel {
	
	private final String DEFAULT_SAVE_FILE = "./save/save1.xml";
	
	private SaveActionPersistenceObject action;
	private SimpleStringProperty saveLocation;
	private SimpleBooleanProperty blocking;
	private LanguageAwareString saveLocationText;
	private LanguageAwareString blockingText;
	private LanguageAwareString yesText;
	private LanguageAwareString noText;
	
	/**
	 * 
	 * @param action A @see SaveActionPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public SaveActionModel(SaveActionPersistenceObject action, ILanguageService languageService) {
		this.action = action;
		
		saveLocation = new SimpleStringProperty(action.saveLocation());
		blocking = new SimpleBooleanProperty(action.blocking());
		saveLocationText = new LanguageAwareString(languageService, DisplayStrings.SAVE_LOCATION);
		blockingText = new LanguageAwareString(languageService, DisplayStrings.BLOCKING);
		yesText = new LanguageAwareString(languageService, DisplayStrings.YES);
		noText = new LanguageAwareString(languageService, DisplayStrings.NO);
		
		initialize();
	}
	
	private void initialize() {
		saveLocation.addListener((v, o, n) -> {
			action.saveLocation(n);
		});
		
		if (saveLocation.get() == null || saveLocation.get().isEmpty()) {
			LogRunner.logger().info("No save location set. Using default location.");
			saveLocation.set(DEFAULT_SAVE_FILE);
		}
		
		blocking.addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating blocking to %s", n));
			action.blocking(n);
		});
	}
	
	/**
	 * 
	 * @return The location to save to.
	 */
	public SimpleStringProperty saveLocation() {
		return saveLocation;
	}
	
	/**
	 * 
	 * @return The display string for save location.
	 */
	public SimpleStringProperty saveLocationText() {
		return saveLocationText;
	}
	
	/**
	 * 
	 * @return The display string for blocking.
	 */
	public SimpleStringProperty blockingText() {
		return blockingText;
	}
	
	/**
	 * 
	 * @return The display string for yes.
	 */
	public SimpleStringProperty yesText() {
		return yesText;
	}
	
	/**
	 * 
	 * @return The display string for no.
	 */
	public SimpleStringProperty noText() {
		return noText;
	}
	
	/**
	 * 
	 * @return If the save is blocking.
	 */
	public SimpleBooleanProperty blocking() {
		return blocking;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public SaveActionPersistenceObject getPersistenceObject() {
		return action;
	}
}
