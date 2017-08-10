package ilusr.textadventurecreator.menus;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OpenMenuItem extends MenuItem {
	
	private final TextAdventureProjectManager projectManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param projectManager A @see TextAdventureProjectManager to manage projects.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public OpenMenuItem(TextAdventureProjectManager projectManager, ILanguageService languageService) {
		super(languageService.getValue(DisplayStrings.OPEN));
		this.projectManager = projectManager;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "File -> Open Pressed.");
			open();
		});
		
		try {
			Image openIco = new Image(AssetLoader.getResourceURL("OpenIcon.png").toExternalForm(), 16, 16, true, true);
			super.setGraphic(new ImageView(openIco));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.OPEN));
		});
	}
	
	private void open() {
		projectManager.openProject(super.parentPopupProperty().get().getOwnerWindow());
	}
}
