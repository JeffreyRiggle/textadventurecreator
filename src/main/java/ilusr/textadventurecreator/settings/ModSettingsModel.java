package ilusr.textadventurecreator.settings;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.mod.IMod;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModSettingsModel {

	private final ISettingsManager settingsManager;
	private final ModManager modManager;
	
	private LanguageAwareString settingText;
	private LanguageAwareString enableModText;
	private LanguageAwareString modsText;
	private ObservableList<ModSetting> modSettings;
	private SimpleBooleanProperty enableMods;
	
	/**
	 * 
	 * @param settingsManager A @see SettingsManager to provide settings.
	 * @param modManager A @see ModManager to manage mods.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ModSettingsModel(ISettingsManager settingsManager, 
							ModManager modManager,
							ILanguageService languageService) {
		this.settingsManager = settingsManager;
		this.modManager = modManager;
		
		modSettings = FXCollections.observableArrayList();
		settingText = new LanguageAwareString(languageService, DisplayStrings.MOD_SETTING);
		enableModText = new LanguageAwareString(languageService, DisplayStrings.ENABLE_MODS);
		modsText = new LanguageAwareString(languageService, DisplayStrings.MODS);
		
		enableMods = new SimpleBooleanProperty(settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true));
		initialize();
	}
	
	private void initialize() {
		enableMods.addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Updating enable mods to %s", n));
			settingsManager.setBooleanValue(SettingNames.ALLOW_MODS, n);
		});
		
		for (IMod mod : modManager.mods()) {
			LogRunner.logger().info(String.format("Adding mod to list %s(%s)", mod.name(), mod.id()));
			ModSetting modSetting = new ModSetting(settingsManager, mod.name(), mod.id(), modManager.getActivation(mod.id()));
			modSettings.add(modSetting);
		}
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
	 * @return Display string for enable mod.
	 */
	public SimpleStringProperty enableModText() {
		return enableModText;
	}
	
	/**
	 * 
	 * @return Display string for mods.
	 */
	public SimpleStringProperty modsText() {
		return modsText;
	}
	
	/**
	 * 
	 * @return Display string for enable mods
	 */
	public SimpleBooleanProperty enableMods() {
		return enableMods;
	}
	
	/**
	 * 
	 * @return List of mods.
	 */
	public ObservableList<ModSetting> mods() {
		return modSettings;
	}
}
