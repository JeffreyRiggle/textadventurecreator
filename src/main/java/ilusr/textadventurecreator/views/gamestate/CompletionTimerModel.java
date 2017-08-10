package ilusr.textadventurecreator.views.gamestate;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.CompletionTimerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionTimerModel {
	
	private CompletionTimerPersistenceObject timer;
	private SimpleStringProperty completion;
	private SimpleObjectProperty<Long> duration;
	private LanguageAwareString stateText;
	private LanguageAwareString timeText;
	
	/**
	 * 
	 * @param timer A @see CompletionTimerPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CompletionTimerModel(CompletionTimerPersistenceObject timer, ILanguageService languageService) {
		this.timer = timer;
		
		completion = new SimpleStringProperty(timer.completionData());
		duration = new SimpleObjectProperty<Long>(timer.duration());
		stateText = new LanguageAwareString(languageService, DisplayStrings.STATE_TO_MOVE_TO);
		timeText = new LanguageAwareString(languageService, DisplayStrings.TIME_IN_MS);
		
		initialize();
	}
	
	private void initialize() {
		completion.addListener((v, o, n) -> {
			timer.completionData(n);
		});
		
		duration.addListener((v, o, n) -> {
			timer.duration(n);
		});
	}
	
	/**
	 * 
	 * @return The state to complete to.
	 */
	public SimpleStringProperty completion() {
		return completion;
	}
	
	/**
	 * 
	 * @return The amount of time before this action fires.
	 */
	public SimpleObjectProperty<Long> duration() {
		return duration;
	}
	
	/**
	 * 
	 * @return Display string for state.
	 */
	public SimpleStringProperty stateText() {
		return stateText;
	}
	
	/**
	 * 
	 * @return Display string for time.
	 */
	public SimpleStringProperty timeText() {
		return timeText;
	}
}
