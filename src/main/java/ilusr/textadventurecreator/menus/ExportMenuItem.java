package ilusr.textadventurecreator.menus;

import java.io.File;
import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.ItemSelector;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExportMenuItem extends MenuItem {

	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param libraryService A @see LibrarySerivce to provide library items.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param languageService A @see LangauageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public ExportMenuItem(LibraryService libraryService, 
						  IDialogService dialogService,
						  ILanguageService languageService,
						  IStyleContainerService styleService,
						  InternalURLProvider urlProvider) {
		super(languageService.getValue(DisplayStrings.EXPORT));
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("File -> Export Pressed.");
			exportLibrary();
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.EXPORT));
		});
	}
	
	private void exportLibrary() {
		List<LibraryItem> items = libraryService.getItems();
		ItemSelector<LibraryItem> selector = new ItemSelector<LibraryItem>(items, languageService, styleService, urlProvider);
		selector.setSelectedAction((ev) -> {
			LibraryItem selectedItem = selector.selectedItem();
			
			if (selectedItem == null) {
				LogRunner.logger().info("Not exporting item since none was selected.");
				return;
			}
			
			File path = getExportPath();
			
			if (path == null) {
				LogRunner.logger().info("Not exporting item since no path was selected.");
				return;
			}
			
			libraryService.exportLibrary(path.getAbsolutePath(), selectedItem, null);
		});
		
		dialogService.displayModal(selector);
	}
	
	private File getExportPath() {
		FileChooser chooser = new FileChooser();
		ExtensionFilter ext = new ExtensionFilter("Text Adventure Library Item", new String[] { "*.talim" });
		chooser.getExtensionFilters().add(ext);
		chooser.selectedExtensionFilterProperty().set(ext);
		
		return chooser.showSaveDialog(super.getParentPopup().getScene().getWindow());
	}
}
