package ilusr.textadventurecreator.toolbars;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class NewProjectCommand extends Button {

	private final TextAdventureProjectManager projectManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param projectManager A @see TextAdventureProjectManager to manage the text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public NewProjectCommand(TextAdventureProjectManager projectManager, ILanguageService languageService) {
		this.projectManager = projectManager;
		this.languageService = languageService;
		
		initialize();
	}

	private void initialize() {
		super.getStylesheets().add(getClass().getResource("NewCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.NEW_PROJECT)));
		
		super.setOnAction((e) -> {
			try {
				LogRunner.logger().info("New project toolbar item pressed.");
				projectManager.createNewProject();
			} catch (Exception ex) {
				LogRunner.logger().severe(ex);
			}
		});
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.NEW_PROJECT));
		});
	}
}
