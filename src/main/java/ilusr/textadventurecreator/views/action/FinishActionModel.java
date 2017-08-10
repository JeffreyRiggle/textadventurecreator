package ilusr.textadventurecreator.views.action;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.FinishActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class FinishActionModel {
	
	private FinishActionPersistenceObject action;
	private LanguageAwareString finishText;
	
	/**
	 * 
	 * @param action A @see FinishActionPersistnceObject to bind to.
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public FinishActionModel(FinishActionPersistenceObject action, ILanguageService languageService) {
		this.action = action;
		finishText = new LanguageAwareString(languageService, DisplayStrings.FINISH_ACTION_TEXT);
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public FinishActionPersistenceObject getPersistenceObject() {
		return action;
	}
	
	/**
	 * 
	 * @return The display string for finish.
	 */
	public SimpleStringProperty finishText() {
		return finishText;
	}
}
