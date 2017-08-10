package ilusr.textadventurecreator.toolbars;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OpenCommand extends Button {

	private final TextAdventureProjectManager projectManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param projectManager A @see TextAdventureProjectManager to manage text adventure projects
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public OpenCommand(TextAdventureProjectManager projectManager, ILanguageService languageService) {
		this.projectManager = projectManager;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("OpenCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.OPEN)));
		
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "Open Toolbar item pressed.");
			open();
		});
		
		try {
			Image openIco = new Image(AssetLoader.getResourceURL("OpenIcon.png").toExternalForm(), 16, 16, true, true);
			super.setGraphic(new ImageView(openIco));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.OPEN));
		});
	}
	
	private void open() {
		projectManager.openProject(super.getScene().getWindow());
	}
}
