package ilusr.textadventurecreator.menus;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.about.AboutView;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AboutMenuItem extends MenuItem {

	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public AboutMenuItem(ILanguageService languageService, 
						 IDialogService dialogService,
						 IStyleContainerService styleService,
						 InternalURLProvider urlProvider) {
		super(languageService.getValue(DisplayStrings.ABOUT));
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		initialize();
	}
	
	private void initialize() {
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.ABOUT));
		});
		
		super.setOnAction((e) -> {
			LogRunner.logger().info("Help -> About pressed.");
			dialogService.displayModal(new AboutView(languageService, styleService, urlProvider), languageService.getValue(DisplayStrings.ABOUT));
		});
	}
}
