package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.RemoveListCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ItemViewer extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private TextField name;
	
	@FXML
	private TextField description;
	
	@FXML
	private ListView<PropertyPersistenceObject> properties;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private Label propertyLabel;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private ItemModel model;
	
	/**
	 * 
	 * @param model A @see ItemModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public ItemViewer(ItemModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemViewer.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		name.textProperty().bindBidirectional(model.name());
		description.textProperty().bindBidirectional(model.description());
		nameLabel.textProperty().bind(model.nameText());
		descriptionLabel.textProperty().bind(model.descriptionText());
		propertyLabel.textProperty().bind(model.propertyText());
		
		if (model.allowLibraryAdd()) {
			properties.setCellFactory(new RemoveListCellFactory<PropertyPersistenceObject>(model.addKey(), 
					  addTemplate(), 
					  new PropertiesViewCreator(model.getLanguageService()),
					  (m) -> { model.properties().remove(m); }));
		} else {
			properties.setCellFactory(new RemoveListCellFactory<PropertyPersistenceObject>(model.addKey(), 
					  (e) -> { model.addProperty(); }, 
					  new PropertiesViewCreator(model.getLanguageService()),
					  (m) -> { model.properties().remove(m); }));
		}
		
		
		properties.itemsProperty().get().add(model.addKey());
		ObservableListBinder<PropertyPersistenceObject> binder = new ObservableListBinder<PropertyPersistenceObject>(model.properties(), properties.getItems());
		binder.bindSourceToTarget();
		setupStyles();
	}
	
	private Node addTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addProperty(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addPropertyFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "itemstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.ITEM), this, false);
		
		String style = styleService.get(StyledComponents.ITEM);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
