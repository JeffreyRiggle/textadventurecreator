package ilusr.textadventurecreator.settings;

import java.util.HashMap;
import java.util.Map;

import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.style.ThemeService;
import ilusr.textadventurecreator.style.ThemeSettings;
import ilusr.textadventurecreator.style.ThemeSettingsModel;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SettingsViewRepository implements ISettingsViewRepository {

	private final ISettingsManager settingsManager;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final TextAdventureProvider provider;
	private final ModManager modManager;
	private final ThemeService themeService;
	private final IDialogProvider dialogProvider;
	
	private Map<String, Node> settingsViews;
	
	/**
	 * 
	 * @param settingsManager A @see SettingsManager to provide settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param modManager A @see ModManager to manage mods.
	 * @param themeService A @see ThemeService to manage themes.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 */
	public SettingsViewRepository(ISettingsManager settingsManager, 
			ILanguageService languageService, 
			IDialogService dialogService,
			TextAdventureProvider provider,
			ModManager modManager,
			ThemeService themeService,
			IDialogProvider dialogProvider) {
		this.settingsManager = settingsManager;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.provider = provider;
		this.modManager = modManager;
		this.themeService = themeService;
		this.dialogProvider = dialogProvider;
		
		settingsViews = new HashMap<String, Node>();
	}
	
	@Override
	public void initialize() {
		settingsViews.put("General", new GeneralSettings(new GeneralSettingsModel(settingsManager, languageService)));
		settingsViews.put("Library", new LibrarySettings(new LibrarySettingsModel(settingsManager, languageService)));
		settingsViews.put("Language", new LanguageSettings(new LanguageSettingsModel(languageService, dialogService, dialogProvider)));
		settingsViews.put("Game", new GameSettings(new GameSettingsModel(provider, languageService)));
		settingsViews.put("Mods", new ModSettings(new ModSettingsModel(settingsManager, modManager, languageService)));
		settingsViews.put("Theme", new ThemeSettings(new ThemeSettingsModel(languageService, themeService, settingsManager)));
	}
	
	@Override
	public void register(String name, Node view) {
		settingsViews.put(name, view);
	}

	@Override
	public void unRegister(String name) {
		settingsViews.remove(name);
	}

	@Override
	public Map<String, Node> getRegisteredViews() {
		return settingsViews;
	}

}
