package ilusr.textadventurecreator.views.trigger;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerModel {

	private final String MULTI_PART = "Multi-Part";
	private final String TEXT = "Text";
	private final String PLAYER = "Player";
	private final String SCRIPT = "Script";
	private final ILanguageService languageService;
	
	private SimpleObjectProperty<TriggerPersistenceObject> trigger;
	private SelectionAwareObservableList<String> types;
	private LanguageAwareString typeText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public TriggerModel(ILanguageService languageService) {
		this(null, languageService);
	}
	
	/**
	 * 
	 * @param trigger The trigger to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public TriggerModel(TriggerPersistenceObject trigger, ILanguageService languageService) {
		this.trigger = new SimpleObjectProperty<TriggerPersistenceObject>(trigger);
		this.languageService = languageService;
		
		typeText = new LanguageAwareString(languageService, DisplayStrings.TYPE);
		valid = new SimpleBooleanProperty(trigger != null && trigger.type() != null && !trigger.type().isEmpty());
		types = new SelectionAwareObservableList<String>();
		types.list().add(TEXT);
		types.list().add(PLAYER);
		types.list().add(SCRIPT);
		types.list().add(MULTI_PART);
		
		initialize();
		bind();
	}
	
	private void initialize() {
		if (trigger.get() == null) {
			LogRunner.logger().log(Level.INFO, "No trigger associated yet.");
			return;
		}
		
		TriggerPersistenceObject trig = trigger.get();
		if (trig instanceof TextTriggerPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Current trigger is text.");
			types.selected().set(TEXT);
		} else if (trig instanceof PlayerTriggerPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Current trigger is player.");
			types.selected().set(PLAYER);
		} else if (trig instanceof ScriptedTriggerPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Current trigger is script.");
			types.selected().set(SCRIPT);
		} else if (trig instanceof MultiPartTriggerPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Current trigger is multipart.");
			types.selected().set(MULTI_PART);
		}
	}
	
	private void bind() {
		trigger.addListener((v, o, n) -> {
			valid.set(n != null && n.type() != null && !n.type().isEmpty());
		});
	}
	
	/**
	 * 
	 * @return The trigger associated with this model.
	 */
	public SimpleObjectProperty<TriggerPersistenceObject> persistenceObject() {
		return trigger;
	}
	
	/**
	 * 
	 * @return The type of trigger to use.
	 */
	public SelectionAwareObservableList<String> types() {
		return types;
	}
	
	/**
	 * 
	 * @return Display string for type.
	 */
	public SimpleStringProperty typeText() {
		return typeText;
	}
	
	/**
	 * 
	 * @return If the trigger is valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return The language service.
	 */
	public ILanguageService getLanguageService() {
		return languageService;
	}
	
	/**
	 * 
	 * @return Multi Part trigger.
	 */
	public String multiPartId() {
		return MULTI_PART;
	}
	
	/**
	 * 
	 * @return Text Trigger.
	 */
	public String textId() {
		return TEXT;
	}
	
	/**
	 * 
	 * @return Player Trigger.
	 */
	public String playerId() {
		return PLAYER;
	}
	
	/**
	 * 
	 * @return Script Trigger.
	 */
	public String scriptId() {
		return SCRIPT;
	}
}
