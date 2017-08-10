package ilusr.textadventurecreator.views;

import ilusr.textadventurecreator.language.ILanguageService;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LanguageAwareString extends SimpleStringProperty {

	private final ILanguageService languageService;
	private final String key;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param key The key value to look up against the language service.
	 */
	public LanguageAwareString(ILanguageService languageService, String key) {
		super(languageService.getValue(key));
		
		this.languageService = languageService;
		this.key = key;
		initialize();
	}
	
	private void initialize() {
		languageService.addListener(() -> {
			super.set(languageService.getValue(key));
		});
	}
}
