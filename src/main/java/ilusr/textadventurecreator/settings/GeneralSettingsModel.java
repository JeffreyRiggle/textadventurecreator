package ilusr.textadventurecreator.settings;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GeneralSettingsModel {

	private final ISettingsManager manager;
	
	private SimpleBooleanProperty hideLanding;
	private LanguageAwareString showGreetingText;
	private LanguageAwareString settingText;
	private SimpleBooleanProperty includeLayout;
	private LanguageAwareString layoutText;
	
	/**
	 * 
	 * @param manager A @see SettingsManager to provide settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public GeneralSettingsModel(ISettingsManager manager, ILanguageService languageService) {
		this.manager = manager;
		hideLanding = new SimpleBooleanProperty(manager.getBooleanSetting(SettingNames.LANDING, true));
		includeLayout = new SimpleBooleanProperty(manager.getBooleanSetting(SettingNames.PERSIST_LAYOUT, true));
		showGreetingText = new LanguageAwareString(languageService, DisplayStrings.SHOW_GREETINGS);
		settingText = new LanguageAwareString(languageService, DisplayStrings.GENERAL_SETTINGS);
		layoutText = new LanguageAwareString(languageService, DisplayStrings.PERSIST_LAYOUT);
		
		initialize();
	}
	
	private void initialize() {
		hideLanding.addListener((v, o, n) -> {
			LogRunner.logger().log(Level.INFO, String.format("Updating show landing to %s", n));
			manager.setBooleanValue(SettingNames.LANDING, n);
		});
		
		includeLayout.addListener((v, o, n) -> {
			LogRunner.logger().log(Level.INFO, String.format("Updating persist layout to %s", n));
			manager.setBooleanValue(SettingNames.PERSIST_LAYOUT, n);
		});
	}
	
	/**
	 * 
	 * @return If the landing page should be hidden.
	 */
	public SimpleBooleanProperty hideLanding() {
		return hideLanding;
	}
	
	/**
	 * 
	 * @return Display string for show greeting.
	 */
	public SimpleStringProperty showGreetingText() {
		return showGreetingText;
	}
	
	/**
	 * 
	 * @return Display string for setting.
	 */
	public SimpleStringProperty settingText() {
		return settingText;
	}
	
	/**
	 * 
	 * @return Display string for layout.
	 */
	public SimpleStringProperty layoutText() {
		return layoutText;
	}
	
	/**
	 * 
	 * @return If layout should be persisted.
	 */
	public SimpleBooleanProperty includeLayout() {
		return includeLayout;
	}
}
