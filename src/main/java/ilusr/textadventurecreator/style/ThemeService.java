package ilusr.textadventurecreator.style;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.RegistrationType;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.shell.IInitialize;
import ilusr.textadventurecreator.style.dark.DarkTheme;

public class ThemeService implements IInitialize, ILocalThemeListener {

	private List<Theme> themes;
	private Theme selectedTheme;
	private String deferredTheme;
	private List<IThemeListener> themeListeners;
	private IStyleContainerService styleService;
	private ISettingsManager settingsManager;
	
	public ThemeService(IStyleContainerService styleService, ISettingsManager settingManager) {
		this.styleService = styleService;
		this.settingsManager = settingManager;
		
		themes = new ArrayList<>();
		themeListeners = new ArrayList<IThemeListener>();
	}
	
	@Override
	public void initialize() {
		try {
			addTheme(new DarkTheme());
			
			String selected = settingsManager.getStringSetting(SettingNames.SELECTED_THEME, null);
			if (selected == null) {
				return;
			}
			
			if (!changeTheme(selected)) {
				deferredTheme = selected;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addTheme(Theme theme) {
		themes.add(theme);
		
		if (deferredTheme != null && theme.getName().equals(deferredTheme)) {
			changeTheme(deferredTheme);
		}
	}
	
	public void removeTheme(Theme theme) {
		if (selectedTheme == theme) {
			updateTheme(null);
		}
		
		themes.remove(theme);
	}
	
	public void addListener(IThemeListener listener) {
		themeListeners.add(listener);
	}
	
	public void removeListener(IThemeListener listener) {
		themeListeners.remove(listener);
	}
	
	public List<String> getRegisteredThemes() {
		List<String> retVal = new ArrayList<String>();
		
		for (Theme theme : themes) {
			retVal.add(theme.getName());
		}
		
		return retVal;
	}
	
	public boolean changeTheme(String themeName) {
		Theme theme = null;
		
		for (Theme t : themes) {
			if (t.getName().equals(themeName)) {
				theme = t;
				break;
			}
		}
		
		if (theme == null) {
			return false;
		}
		
		applyStyles(theme.styles());
		updateTheme(theme);
		
		for(IThemeListener listener : themeListeners) {
			listener.themeChanged(themeName);
		}
		
		return true;
	}
	
	private void updateTheme(Theme theme) {
		if (selectedTheme != null) {
			selectedTheme.removeListener(this);
		}
		
		selectedTheme = theme;
		
		if (selectedTheme != null) {
			selectedTheme.addListener(this);
		}
	}
	
	private void applyStyles(Map<String, File> styleMap) {
		for(Entry<String, File> entry : styleMap.entrySet()) {
			try {
				styleService.register(entry.getKey(), entry.getValue(), RegistrationType.Override);
			} catch (IllegalArgumentException | IOException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public void updated(String style, File file) {
		if (file == null) {
			styleService.register(style, "", RegistrationType.Override);
			return;
		}
		
		try {
			styleService.register(style, file, RegistrationType.Override);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
	}
}