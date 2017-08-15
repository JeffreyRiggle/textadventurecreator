package ilusr.textadventurecreator.menus;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsViewRepository;
import ilusr.textadventurecreator.settings.SettingsView;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
/**
 * 
 * @author Jeff Riggle
 *
 */
public class SettingsMenuItem extends MenuItem {

	private final ILanguageService languageService;
	private final ISettingsViewRepository repository;
	private final IDialogService dialogService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param manager A @see SettingsManager to manage settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param provider A @see TextAdventureProvider to get the current text adventure.
	 * @param modManager A @see ModManager to manage mods.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public SettingsMenuItem(ILanguageService languageService,
							ISettingsViewRepository repository,
							IDialogService dialogService,
							IStyleContainerService styleService,
							InternalURLProvider urlProvider) {
		super(languageService.getValue(DisplayStrings.SETTINGS));
		this.languageService = languageService;
		this.repository = repository;
		this.dialogService = dialogService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("Options -> Settings Pressed.");
			showSettings();
		});
		
		languageService.addListener(() -> {
			super.setText(languageService.getValue(DisplayStrings.SETTINGS));
		});
	}
	
	private void showSettings() {
		Scene view = new Scene(new SettingsView(repository, languageService, styleService, urlProvider), 800, 600);
		dialogService.displayModal(view, languageService.getValue(DisplayStrings.SETTINGS));
	}
}
