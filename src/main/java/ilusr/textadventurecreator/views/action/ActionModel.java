package ilusr.textadventurecreator.views.action;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionModel {
	
	//TODO: Figure this part out (l10n).
	private final String APPEND_ACTION = "Append Text";
	private final String COMPLETION_ACTION = "Complete";
	private final String EXECUTION_ACTION = "Execute";
	private final String MOD_PLAYER_ACTION = "Modify Player";
	private final String SAVE_ACTION = "Save action";
	private final String SCRIPT_ACTION = "Script Action";
	private final String FINISH_ACTION = "Finish Action";
	
	private SimpleObjectProperty<ActionPersistenceObject> action;
	private SelectionAwareObservableList<String> types;
	private LanguageAwareString actionText;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ActionModel(ILanguageService languageService) {
		this(null, languageService);
	}
	
	/**
	 * 
	 * @param action A @see ActionPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ActionModel(ActionPersistenceObject action, ILanguageService languageService) {
		this.action = new SimpleObjectProperty<ActionPersistenceObject>(action);
		
		types = new SelectionAwareObservableList<String>();
		actionText = new LanguageAwareString(languageService, DisplayStrings.ACTION);
		
		initialize();
	}
	
	private void initialize() {
		addTypes();
		selectType();
	}
	
	private void addTypes() {
		types.list().add(APPEND_ACTION);
		types.list().add(COMPLETION_ACTION);
		types.list().add(EXECUTION_ACTION);
		types.list().add(MOD_PLAYER_ACTION);
		types.list().add(SAVE_ACTION);
		types.list().add(SCRIPT_ACTION);
		types.list().add(FINISH_ACTION);
	}
	
	private void selectType() {
		if (action.get() == null) {
			LogRunner.logger().log(Level.INFO, "Action does not yet exist not selecting a type.");
			return;
		}
		
		if (action.get() instanceof AppendTextActionPersistence) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to append.");
			types.selected().set(APPEND_ACTION);
		} else if (action.get() instanceof CompletionActionPersistence) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to complete.");
			types.selected().set(COMPLETION_ACTION);
		} else if (action.get() instanceof ExecutionActionPersistence) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to execute.");
			types.selected().set(EXECUTION_ACTION);
		} else if (action.get() instanceof ModifyPlayerActionPersistence) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to modify.");
			types.selected().set(MOD_PLAYER_ACTION);
		} else if (action.get() instanceof SaveActionPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to save.");
			types.selected().set(SAVE_ACTION);
		} else if (action.get() instanceof ScriptedActionPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to script.");
			types.selected().set(SCRIPT_ACTION);
		} else if (action.get() instanceof FinishActionPersistenceObject) {
			LogRunner.logger().log(Level.INFO, "Setting selected type to finish.");
			types.selected().set(FINISH_ACTION);
		}
	}
	
	/**
	 * 
	 * @return A @see SelectionAwareObersavbleList of available action types.
	 */
	public SelectionAwareObservableList<String> types() {
		return types;
	}
	
	/**
	 * 
	 * @return The text for the append action.
	 */
	public String appendAction() {
		return APPEND_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the completion action.
	 */
	public String completionAction() {
		return COMPLETION_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the execution action.
	 */
	public String executionAction() {
		return EXECUTION_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the save action.
	 */
	public String saveAction() {
		return SAVE_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the mod player action.
	 */
	public String playerAction() {
		return MOD_PLAYER_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the script action.
	 */
	public String scriptAction() {
		return SCRIPT_ACTION;
	}
	
	/**
	 * 
	 * @return The text for the finish action.
	 */
	public String finishAction() {
		return FINISH_ACTION;
	}
	
	/**
	 * 
	 * @return The action associated with this model.
	 */
	public SimpleObjectProperty<ActionPersistenceObject> persistenceObject() {
		return action;
	}
	
	/**
	 * 
	 * @return A display string for action.
	 */
	public SimpleStringProperty actionText() {
		return actionText;
	}
}
