package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.RemoveListCellFactory;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartViewer extends PlayerDataView implements Initializable, IStyleWatcher {

	@FXML
	private TextField name;
	
	@FXML
	private TextField description;
	
	@FXML
	private ListView<CharacteristicPersistenceObject> characteristics;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private Label characteristicLabel;
	
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private BodyPartModel model;
	
	/**
	 * 
	 * @param model The @see BodyPartModel to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BodyPartViewer(BodyPartModel model, 
						  ILanguageService languageService,
						  IStyleContainerService styleService,
						  InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BodyPartViewer.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		name.textProperty().bindBidirectional(model.name());
		description.textProperty().bindBidirectional(model.description());
		nameLabel.textProperty().bind(model.bodyNameText());
		descriptionLabel.textProperty().bind(model.bodyDescriptionText());
		characteristicLabel.textProperty().bind(model.characteristicText());
		
		if (model.allowLibraryAdd()) {
			characteristics.setCellFactory(new RemoveListCellFactory<CharacteristicPersistenceObject>(model.addKey(), 
					  addTemplate(), 
					  new CharacteristicViewCreator(languageService),
					  (m) -> { model.characteristics().remove(m); }));
		} else {
			characteristics.setCellFactory(new RemoveListCellFactory<CharacteristicPersistenceObject>(model.addKey(), 
					  (e) -> { model.addCharacteristic(); }, 
					  new CharacteristicViewCreator(languageService),
					  (m) -> { model.characteristics().remove(m); }));
		}
		
		
		characteristics.itemsProperty().get().add(model.addKey());
		ObservableListBinder<CharacteristicPersistenceObject> characteristicBinder = new ObservableListBinder<CharacteristicPersistenceObject>(model.characteristics(), characteristics.getItems());
		characteristicBinder.bindSourceToTarget();
		setupStyles();
	}

	private Node addTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.setId("listCellAdd");
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addCharacteristic(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return model.data();
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "bodystylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.BODY_PART), this, false);
		
		String style = styleService.get(StyledComponents.BODY_PART);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
