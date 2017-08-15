package ilusr.textadventurecreator.menus;

import ilusr.iroshell.services.ILayoutService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.BluePrintNames;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibrariesMenuItem extends MenuItem {

	private final ILayoutService layoutService;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param layoutService A @see ILayoutService to add tabs.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LibrariesMenuItem(ILayoutService layoutService, ILanguageService languageService) {
		super(languageService.getValue(DisplayStrings.LIBRARIES));
		this.layoutService = layoutService;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("View -> Libraries Pressed.");
			layoutService.addTab(BluePrintNames.Library);
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.LIBRARIES));
		});
	}
}
