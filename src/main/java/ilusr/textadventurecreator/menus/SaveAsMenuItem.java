package ilusr.textadventurecreator.menus;

import java.io.File;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveAsMenuItem extends GameAwareMenuItem {

	private final ProjectPersistenceManager persistenceManager;
	private final TextAdventureProvider provider;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param persistenceManager A @see ProjectPersistenceManager to persist the project.
	 * @param provider A @see TextAdventureProvider to get the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public SaveAsMenuItem(ProjectPersistenceManager persistenceManager, 
						  TextAdventureProvider provider,
						  ILanguageService languageService) {
		super(provider, languageService.getValue(DisplayStrings.SAVE_AS));
		this.persistenceManager = persistenceManager;
		this.provider = provider;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "File -> Save As Pressed.");
			saveFile();
		});
		
		languageService.addListener(() -> {
			super.setText(languageService.getValue(DisplayStrings.SAVE_AS));
		});
	}
	
	private void saveFile() {
		FileChooser chooser = new FileChooser();
		ExtensionFilter ext = new ExtensionFilter("Text Adventure", new String[] { "*.txaml" });
		chooser.getExtensionFilters().add(ext);
		chooser.selectedExtensionFilterProperty().set(ext);
		
		File saveFile = chooser.showSaveDialog(super.parentPopupProperty().get().getOwnerWindow());
		
		if (saveFile == null) {
			LogRunner.logger().log(Level.INFO, "Not saving since no path was selected.");
			return;
		}
		
		persistenceManager.saveAsync(provider.getTextAdventureProject(), saveFile.getAbsolutePath());
	}
}
