package ilusr.textadventurecreator.style;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleStringProperty;

public class ThemeSettingsModel {

	private final ThemeService themeService;
	private final ISettingsManager settingsManager;
	
	private LanguageAwareString themeSettingsText;
	private LanguageAwareString selectedText;
	private LanguageAwareString applyText;
	
	private SelectionAwareObservableList<String> selectedTheme;
	
	public ThemeSettingsModel(ILanguageService languageService, 
							  ThemeService themeService,
							  ISettingsManager settingsManager) {
		this.themeService = themeService;
		this.settingsManager = settingsManager;
		
		selectedTheme = new SelectionAwareObservableList<>();
		themeSettingsText = new LanguageAwareString(languageService, DisplayStrings.THEME_SETTINGS);
		selectedText = new LanguageAwareString(languageService, DisplayStrings.THEMES);
		applyText = new LanguageAwareString(languageService, DisplayStrings.APPLY);
		
		initialize();
	}
	
	private void initialize() {
		for (String theme : themeService.getRegisteredThemes()) {
			selectedTheme.list().add(theme);
		}
		
		String firstTheme = selectedTheme.list().size() > 0 ? selectedTheme.list().get(0) : null;
		String select = settingsManager.getStringSetting(SettingNames.SELECTED_THEME, firstTheme);
		selectedTheme.selected().set(select);
	}
	
	public void apply() {
		String theme = selectedTheme.selected().get();
		themeService.changeTheme(theme);
		settingsManager.setStringSetting(SettingNames.SELECTED_THEME, theme);
	}
	
	public SelectionAwareObservableList<String> selectedTheme() {
		return selectedTheme;
	}
	
	public SimpleStringProperty themeSettingsText() {
		return themeSettingsText;
	}
	
	public SimpleStringProperty selectedText() {
		return selectedText;
	}
	
	public SimpleStringProperty applyText() {
		return applyText;
	}
}
