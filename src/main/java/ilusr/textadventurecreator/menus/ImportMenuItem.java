package ilusr.textadventurecreator.menus;

import java.io.File;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ImportMenuItem extends MenuItem {

	private final LibraryService libraryService;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ImportMenuItem(LibraryService libraryService, ILanguageService languageService) {
		super(languageService.getValue(DisplayStrings.IMPORT));
		this.libraryService = libraryService;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("File -> Import Pressed.");
			importLibrary();
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.IMPORT));
		});
	}
	
	private void importLibrary() {
		FileChooser chooser = new FileChooser();
		
		ExtensionFilter ext = new ExtensionFilter("Text Adventure Library Item", "*.talim");
		chooser.getExtensionFilters().add(ext);
		
		File loadFile = chooser.showOpenDialog(super.parentPopupProperty().get().getOwnerWindow());
		
		if (loadFile == null) {
			LogRunner.logger().info("Not importing library since no path was selected.");
			return;
		}
		
		libraryService.importLibrary(loadFile.getAbsolutePath(), null);
	}
}
