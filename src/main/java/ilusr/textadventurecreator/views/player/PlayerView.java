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
import ilusr.textadventurecreator.views.EditRemoveListCellFactory;
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
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.EquipPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ListView<AttributePersistenceObject> attributes;
	
	@FXML
	private ListView<CharacteristicPersistenceObject> characteristics;
	
	@FXML
	private ListView<BodyPartPersistenceObject> bodyParts;
	
	@FXML
	private ListView<EquipPersistenceObject> equipment;
	
	@FXML
	private ListView<InventoryItem> inventory;
	
	@FXML
	private TextField id;
	
	@FXML
	private Label playerLabel;
	
	@FXML
	private Label attributeLabel;
	
	@FXML
	private Label characteristicLabel;
	
	@FXML
	private Label bodyPartLabel;
	
	@FXML
	private Label inventoryLabel;
	
	@FXML
	private Label equipmentLabel;
	
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private PlayerModel model;
	
	/**
	 * 
	 * @param model A @see PlayerModel to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public PlayerView(PlayerModel model, 
					  ILanguageService languageService,
					  IStyleContainerService styleService,
					  InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader playerLoader = new FXMLLoader(getClass().getResource("PlayerView.fxml"));
		playerLoader.setRoot(this);
		playerLoader.setController(this);
		
		try {
			playerLoader.load();
		} catch (Exception exception) {
			LogRunner.logger().severe(exception);
		}
	}
	
	/**
	 * 
	 * @return The bound model.
	 */
	public PlayerModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id.textProperty().bindBidirectional(model.playerID());
		playerLabel.textProperty().bind(model.playerText());
		attributeLabel.textProperty().bind(model.attributeText());
		characteristicLabel.textProperty().bind(model.characteristicText());
		bodyPartLabel.textProperty().bind(model.bodyPartText());
		inventoryLabel.textProperty().bind(model.inventoryText());
		equipmentLabel.textProperty().bind(model.equipmentText());
		
		initializeAttributes();
		initializeCharacteristics();
		initializeBodyParts();
		
		equipment.setCellFactory(new EditRemoveListCellFactory<EquipPersistenceObject>(model.addEquipKey(), 
																					   model.getEditEquipmentAction(), 
																					   model.getAddEquipmentAction(),
																					   null,
																					   (m) -> { model.equipment().remove(m); }));
		equipment.itemsProperty().get().add(model.addEquipKey());
		ObservableListBinder<EquipPersistenceObject> equipBinder = new ObservableListBinder<EquipPersistenceObject>(model.equipment(), equipment.getItems());
		equipBinder.bindSourceToTarget();
		
		inventory.setCellFactory(new EditRemoveListCellFactory<InventoryItem>(model.addItemKey(), 
																			  model.getEditInventoryAction(), 
																			  (e) -> model.addItem(),
																			  null,
																			  (m) -> { model.items().remove(m); }));
		inventory.itemsProperty().get().add(model.addItemKey());
		ObservableListBinder<InventoryItem> itemBinder = new ObservableListBinder<InventoryItem>(model.items(), inventory.getItems());
		itemBinder.bindSourceToTarget();
		setupStyles();
	}
	
	private void initializeAttributes() {
		if (model.allowLibraryAdd()) {
			attributes.setCellFactory(new RemoveListCellFactory<AttributePersistenceObject>(model.addAttributeKey(),
					attributeAddTemplate(),
					new AttributeViewCreator(languageService),
				    (m) -> {  model.attributes().remove(m); }));
		} else {
			attributes.setCellFactory(new RemoveListCellFactory<AttributePersistenceObject>(model.addAttributeKey(),
					(e) -> { model.addAttribute(); },
					new AttributeViewCreator(languageService),
				    (m) -> {  model.attributes().remove(m); }));
		}
		
		attributes.itemsProperty().get().add(model.addAttributeKey());
		ObservableListBinder<AttributePersistenceObject> attibuteBinder = new ObservableListBinder<AttributePersistenceObject>(model.attributes(), attributes.getItems());
		attibuteBinder.bindSourceToTarget();
	}
	
	private void initializeCharacteristics() {
		if (model.allowLibraryAdd()) {
			characteristics.setCellFactory(new RemoveListCellFactory<CharacteristicPersistenceObject>(model.addCharacteristicKey(), 
					  characteristicAddTemplate(), 
					  new CharacteristicViewCreator(languageService),
					  (m) -> { model.characteristics().remove(m); }));
		} else {
			characteristics.setCellFactory(new RemoveListCellFactory<CharacteristicPersistenceObject>(model.addCharacteristicKey(), 
					  (e) -> { model.addCharacteristic(); }, 
					  new CharacteristicViewCreator(languageService),
					  (m) -> { model.characteristics().remove(m); }));
		}
		
		
		characteristics.itemsProperty().get().add(model.addCharacteristicKey());
		ObservableListBinder<CharacteristicPersistenceObject> characteristicBinder = new ObservableListBinder<CharacteristicPersistenceObject>(model.characteristics(), characteristics.getItems());
		characteristicBinder.bindSourceToTarget();
	}
	
	private void initializeBodyParts() {
		if (model.allowLibraryAdd()) {
			bodyParts.setCellFactory(new EditRemoveListCellFactory<BodyPartPersistenceObject>(model.addBodyPartKey(), 
					  model.getEditBodyPartAction(), 
					  bodyPartAddTemplate(),
					  null,
					  (m) -> { model.bodyParts().remove(m); }));
		} else {
			bodyParts.setCellFactory(new EditRemoveListCellFactory<BodyPartPersistenceObject>(model.addBodyPartKey(), 
					  model.getEditBodyPartAction(), 
					  (e) -> { model.addBodyPart(); },
					  null,
					  (m) -> { model.bodyParts().remove(m); }));
		}
		
		
		bodyParts.itemsProperty().get().add(model.addBodyPartKey());
		ObservableListBinder<BodyPartPersistenceObject> bodyPartBinder = new ObservableListBinder<BodyPartPersistenceObject>(model.bodyParts(), bodyParts.getItems());
		bodyPartBinder.bindSourceToTarget();
	}
	
	private Node attributeAddTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addAttribute(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addAttributeFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private Node characteristicAddTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addCharacteristic(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addCharacteristicFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private Node bodyPartAddTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addBodyPart(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addBodyPartFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "playerstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.PLAYER), this, false);
		
		String style = styleService.get(StyledComponents.PLAYER);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
