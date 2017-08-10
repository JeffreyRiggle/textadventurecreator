package ilusr.textadventurecreator.views.action;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.CompletionActionPersistence;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionActionModel {

	private CompletionActionPersistence action;
	private SimpleStringProperty completionData;
	private LanguageAwareString stateText;
	
	/**
	 * 
	 * @param action A @see CompletionActionPersistence to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CompletionActionModel(CompletionActionPersistence action, ILanguageService languageService) {
		this.action = action;
		completionData = new SimpleStringProperty();
		stateText = new LanguageAwareString(languageService, DisplayStrings.STATE_TO_MOVE_TO);
		
		initialize();
	}
	
	private void initialize() {
		if (action.completionData() != null) {
			completionData.set(action.completionData());
		}
		
		completionData.addListener((v, o, n) -> {
			action.completionData(n);
		});
	}
	
	/**
	 * 
	 * @return The data to use for completion.
	 */
	public SimpleStringProperty completionData() {
		return completionData;
	}
	
	/**
	 * 
	 * @return A display string for state to move to.
	 */
	public SimpleStringProperty stateText() {
		return stateText;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public CompletionActionPersistence getPersistenceObject() {
		return action;
	}
}
