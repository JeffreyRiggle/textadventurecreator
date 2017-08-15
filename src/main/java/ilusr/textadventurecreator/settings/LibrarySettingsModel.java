package ilusr.textadventurecreator.settings;

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
public class LibrarySettingsModel {

	private final ISettingsManager manager;
	
	private SimpleBooleanProperty globalLibrary;
	private SimpleBooleanProperty gameLibrary;
	private LanguageAwareString settingText;
	private LanguageAwareString globalText;
	private LanguageAwareString individualText;
	
	/**
	 * 
	 * @param manager A @see SettingsManager to provide settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LibrarySettingsModel(ISettingsManager manager, ILanguageService languageService) {
		this.manager = manager;
		globalLibrary = new SimpleBooleanProperty(manager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true));
		gameLibrary = new SimpleBooleanProperty(manager.getBooleanSetting(SettingNames.GAME_LIBRARY, false));
		settingText = new LanguageAwareString(languageService, DisplayStrings.LIBRARY_SETTINGS);
		globalText = new LanguageAwareString(languageService, DisplayStrings.USE_GLOBAL_LIBRARY);
		individualText = new LanguageAwareString(languageService, DisplayStrings.USE_INDIVIDUAL_LIBRARY);
		
		initialize();
	}
	
	private void initialize() {
		globalLibrary.addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating global library setting to %s", n));
			manager.setBooleanValue(SettingNames.GLOBAL_LIBRARY, n);
		});
		
		gameLibrary.addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating game library setting to %s", n));
			manager.setBooleanValue(SettingNames.GAME_LIBRARY, n);
		});
	}
	
	/**
	 * 
	 * @return If the global library is being used.
	 */
	public SimpleBooleanProperty globalLibrary() {
		return globalLibrary;
	}
	
	/**
	 * 
	 * @return If the game library is being used.
	 */
	public SimpleBooleanProperty gameLibrary() {
		return gameLibrary;
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
	 * @return Display string for global library.
	 */
	public SimpleStringProperty globalText() {
		return globalText;
	}
	
	/**
	 * 
	 * @return Display string for individual library.
	 */
	public SimpleStringProperty individualText() {
		return individualText;
	}
}
