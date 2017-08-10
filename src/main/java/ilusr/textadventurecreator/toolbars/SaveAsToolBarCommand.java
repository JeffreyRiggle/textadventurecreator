package ilusr.textadventurecreator.toolbars;

import java.io.File;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveAsToolBarCommand extends GameStateAwareButton {

	private final ProjectPersistenceManager persistenceManager;
	private final TextAdventureProvider provider;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param persistenceManager A @see ProjectPersistenceManager to save the project
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public SaveAsToolBarCommand(ProjectPersistenceManager persistenceManager, 
								TextAdventureProvider provider,
								ILanguageService languageService) {
		super(provider, null);
		this.persistenceManager = persistenceManager;
		this.provider = provider;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("SaveAsCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.SAVE_AS)));
		super.setOnAction((e) -> {
			saveFile();
		});
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.SAVE_AS));
		});
	}
	
	private void saveFile() {
		LogRunner.logger().log(Level.INFO, "Save As toolbar item pressed.");
		FileChooser chooser = new FileChooser();
		ExtensionFilter ext = new ExtensionFilter("Text Adventure", new String[] { "*.txaml" });
		chooser.getExtensionFilters().add(ext);
		chooser.selectedExtensionFilterProperty().set(ext);
		
		File saveFile = chooser.showSaveDialog(super.getScene().getWindow());
		
		if (saveFile == null) {
			LogRunner.logger().log(Level.INFO, "No file selected not saving project");
			return;
		}
		
		persistenceManager.saveAsync(provider.getTextAdventureProject(), saveFile.getAbsolutePath());
	}
}
