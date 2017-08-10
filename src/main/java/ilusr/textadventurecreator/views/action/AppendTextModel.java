package ilusr.textadventurecreator.views.action;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.AppendTextActionPersistence;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AppendTextModel {
	
	private AppendTextActionPersistence action;
	private SimpleStringProperty appendText;
	private LanguageAwareString labelText;
	
	/**
	 * 
	 * @param action A @see AppendTextActionPersistence to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public AppendTextModel(AppendTextActionPersistence action, ILanguageService languageService) {
		this.action = action;
		
		appendText = new SimpleStringProperty(action.appendText());
		labelText = new LanguageAwareString(languageService, DisplayStrings.APPEND_TEXT);
		
		initialize();
	}

	private void initialize() {
		appendText.addListener((v, o, n) -> {
			this.action.appendText(n);
		});
	}
	
	/**
	 * 
	 * @return The text to append in this action.
	 */
	public SimpleStringProperty appendText() {
		return appendText;
	}
	
	/**
	 * 
	 * @return A display string for append text.
	 */
	public SimpleStringProperty labelText() {
		return labelText;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public AppendTextActionPersistence getPersistenceObject() {
		return action;
	}
}
