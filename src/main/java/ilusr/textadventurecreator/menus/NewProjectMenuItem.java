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
public class NewProjectMenuItem extends MenuItem {

	private final TextAdventureProjectManager projectManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param projectManager A @see TextAdventureProjectManager to manage projects.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public NewProjectMenuItem(TextAdventureProjectManager projectManager, ILanguageService languageService) {
		super(languageService.getValue(DisplayStrings.NEW));
		this.projectManager = projectManager;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		setupIcon();

		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "File -> New Pressed.");
			try {
				projectManager.createNewProject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.NEW));
		});
	}
	
	private void setupIcon() {
		try {
			Image newIco = new Image(AssetLoader.getStream("newFile.png"));
			super.setGraphic(new ImageView(newIco));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
