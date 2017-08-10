package ilusr.textadventurecreator.views.action;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.ExecutionActionPersistence;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExecutionActionModel {

	private ExecutionActionPersistence action;
	private SimpleStringProperty executable;
	private SimpleBooleanProperty blocking;
	private LanguageAwareString exeText;
	private LanguageAwareString blockingText;
	private LanguageAwareString yesText;
	private LanguageAwareString noText;
	
	/**
	 * 
	 * @param action A @see ExecutionActionPersistence to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ExecutionActionModel(ExecutionActionPersistence action, ILanguageService languageService) {
		this.action = action;
		
		executable = new SimpleStringProperty(action.executable());
		blocking = new SimpleBooleanProperty(action.blocking());
		exeText = new LanguageAwareString(languageService, DisplayStrings.EXECUTABLE);
		blockingText = new LanguageAwareString(languageService, DisplayStrings.BLOCKING);
		yesText = new LanguageAwareString(languageService, DisplayStrings.YES);
		noText = new LanguageAwareString(languageService, DisplayStrings.NO);
		
		initialize();
	}
	
	private void initialize() {
		executable.addListener((v, o, n) -> {
			action.executable(n);
		});
		
		blocking.addListener((v, o, n) -> {
			action.blocking(n);
		});
	}
	
	/**
	 * 
	 * @return The executable to run.
	 */
	public SimpleStringProperty executable() {
		return executable;
	}
	
	/**
	 * 
	 * @return Display string for executable.
	 */
	public SimpleStringProperty exeText() {
		return exeText;
	}
	
	/**
	 * 
	 * @return Display string for blocking
	 */
	public SimpleStringProperty blockingText() {
		return blockingText;
	}
	
	/**
	 * 
	 * @return Display string for yes.
	 */
	public SimpleStringProperty yesText() {
		return yesText;
	}
	
	/**
	 * 
	 * @return Display string for no.
	 */
	public SimpleStringProperty noText() {
		return noText;
	}
	
	/**
	 * 
	 * @return If the executable is blocking.
	 */
	public SimpleBooleanProperty blocking() {
		return blocking;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public ExecutionActionPersistence getPersistenceObject() {
		return action;
	}
}
