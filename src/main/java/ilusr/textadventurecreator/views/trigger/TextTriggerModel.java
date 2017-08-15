package ilusr.textadventurecreator.views.trigger;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.TextTriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextTriggerModel {

	private final String PRE_MATCH = "Prefix";
	private final String POST_MATCH = "Suffix";
	private final String EXACT_MATCH = "Exact";
	private final String CONTAINS_MATCH = "Contains";
	private final String NOT_CONTAINS_MATCH = "Does not Contain";
	
	private TextTriggerPersistenceObject trigger;
	private SelectionAwareObservableList<String> matchType;
	private SimpleStringProperty text;
	private SimpleBooleanProperty caseSensitive;
	private LanguageAwareString textText;
	private LanguageAwareString caseText;
	private LanguageAwareString typeText;
	private LanguageAwareString yesText;
	private LanguageAwareString noText;
	
	/**
	 * 
	 * @param trigger A @see TextTriggerPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public TextTriggerModel(TextTriggerPersistenceObject trigger, ILanguageService languageService) {
		this.trigger = trigger;
		text = new SimpleStringProperty(trigger.text());
		caseSensitive = new SimpleBooleanProperty(trigger.caseSensitive());
		textText = new LanguageAwareString(languageService, DisplayStrings.TEXT);
		caseText = new LanguageAwareString(languageService, DisplayStrings.CASE_SENSITIVE);
		typeText = new LanguageAwareString(languageService, DisplayStrings.TYPE);
		yesText = new LanguageAwareString(languageService, DisplayStrings.YES);
		noText = new LanguageAwareString(languageService, DisplayStrings.NO);
		
		matchType = new SelectionAwareObservableList<String>();
		matchType.list().add(PRE_MATCH);
		matchType.list().add(POST_MATCH);
		matchType.list().add(EXACT_MATCH);
		matchType.list().add(CONTAINS_MATCH);
		
		initialize();
		setupListeners();
	}
	
	private void initialize() {
		if (trigger.matchType() == null) {
			LogRunner.logger().info("Match type not yet set for trigger.");
			return;
		}
		
		switch (trigger.matchType()) {
			case Contains:
				matchType.selected().set(CONTAINS_MATCH);
				break;
			case Exact:
				matchType.selected().set(EXACT_MATCH);
				break;
			case NotContains:
				matchType.selected().set(NOT_CONTAINS_MATCH);
				break;
			case Postfix:
				matchType.selected().set(POST_MATCH);
				break;
			case Prefix:
				matchType.selected().set(PRE_MATCH);
				break;
		}
	}
	
	/**
	 * 
	 * @return The text to trigger off of.
	 */
	public SimpleStringProperty text() {
		return text;
	}
	
	/**
	 * 
	 * @return If the condition is case sensitive.
	 */
	public SimpleBooleanProperty caseSensitive() {
		return caseSensitive;
	}
	
	/**
	 * 
	 * @return What type of match is required for the trigger.
	 */
	public SelectionAwareObservableList<String> matchType() {
		return matchType;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public TextTriggerPersistenceObject persistenceObject() {
		return trigger;
	}
	
	/**
	 * 
	 * @return Display string for text.
	 */
	public SimpleStringProperty textText() {
		return textText;
	}
	
	/**
	 * 
	 * @return Display string for case.
	 */
	public SimpleStringProperty caseText() {
		return caseText;
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
	
	private void setupListeners() {
		matchType.selected().addListener((v, o, n) -> { 
			trigger.matchType(convertMatchType(n));
		});
		
		text.addListener((v, o, n) -> {
			trigger.text(n);
		});
		
		caseSensitive.addListener((v, o, n) -> {
			trigger.caseSensitive(n);
		});
	}
	
	private MatchType convertMatchType(String value) {
		switch (value) {
			case PRE_MATCH:
				return MatchType.Prefix;
			case POST_MATCH:
				return MatchType.Postfix;
			case EXACT_MATCH:
				return MatchType.Exact;
			case CONTAINS_MATCH:
				return MatchType.Contains;
			case NOT_CONTAINS_MATCH:
				return MatchType.NotContains;
			default:
				return MatchType.Contains;	
		}
	}
}
