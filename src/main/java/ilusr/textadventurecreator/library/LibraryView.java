package ilusr.textadventurecreator.library;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.EditRemoveListCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryView extends AnchorPane implements Initializable, IStyleWatcher {
	
	@FXML
	private Button importBtn;
	
	@FXML
	private Button exportBtn;
	
	@FXML
	private ListView<LibraryItem> libraryItems;
	
	@FXML
	private Label libraryLabel;
	
	private LibraryViewModel model;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param model A @see LibraryViewModel to associate with this view.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public LibraryView(LibraryViewModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LibraryView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		libraryItems.setCellFactory(new EditRemoveListCellFactory<LibraryItem>(model.addItemKey(), 
				  model.getEditAction(), 
				  model.getAddAction(),
				  (i) -> { return new Label(i.getLibraryName() + ": " + i.getAuthor()); },
				  (m) -> { model.items().remove(m); }));
		libraryItems.itemsProperty().get().add(model.addItemKey());
		ObservableListBinder<LibraryItem> itemBinder = new ObservableListBinder<LibraryItem>(model.items(), libraryItems.getItems());
		itemBinder.bindSourceToTarget();
		
		importBtn.setOnAction((e) -> {
			importItem();
		});
		
		exportBtn.setOnAction((e) -> {
			exportItem();
		});
		
		bindText();
		setupStyle();
	}
	
	private void bindText() {
		libraryLabel.textProperty().bind(model.libraryText());
		importBtn.textProperty().bind(model.importText());
		exportBtn.textProperty().bind(model.exportText());
	}
	
	private void setupStyle() {
		styleUpdater = new StyleUpdater(urlProvider, "libraryviewstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.LIBRARY), this, false);
		
		String style = styleService.get(StyledComponents.LIBRARY);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	private void importItem() {
		FileChooser chooser = new FileChooser();
		
		ExtensionFilter ext = new ExtensionFilter("Text Adventure Library Item", "*.talim");
		chooser.getExtensionFilters().add(ext);
		
		File loadFile = chooser.showOpenDialog(super.getParent().getScene().getWindow());
		
		if (loadFile == null) {
			return;
		}
		
		model.importLibrary(loadFile.getAbsolutePath());
	}
	
	private void exportItem() {
		LibraryItem selectedItem = libraryItems.getSelectionModel().getSelectedItem();
		
		if (selectedItem == null) {
			//TODO: Warning?
			return;
		}
		
		FileChooser chooser = new FileChooser();
		ExtensionFilter ext = new ExtensionFilter("Text Adventure Library Item", new String[] { "*.talim" });
		chooser.getExtensionFilters().add(ext);
		chooser.selectedExtensionFilterProperty().set(ext);
		
		File saveFile = chooser.showSaveDialog(super.getParent().getScene().getWindow());
		
		if (saveFile == null) {
			return;
		}
		
		model.exportLibrary(saveFile.getAbsolutePath(), selectedItem);
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
