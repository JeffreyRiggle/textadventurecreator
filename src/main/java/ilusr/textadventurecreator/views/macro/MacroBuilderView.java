package ilusr.textadventurecreator.views.macro;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.action.PropertyType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MacroBuilderView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private HBox buildArea;
	
	@FXML
	private TextField macro;
	
	@FXML
	private Button build;
	
	@FXML
	private Label macroLabel;
	
	private ComboBox<String> players;
	private ComboBox<String> selector;
	private ComboBox<String> attributes;
	private ComboBox<String> characteristics;
	private ComboBox<String> bodyParts;
	private ComboBox<String> bodyPartOptions;
	private ComboBox<String> items;
	private ComboBox<String> itemOptions;
	private ComboBox<String> properties;
	private ComboBox<String> propertyNames;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private MacroBuilderModel model;
	
	/**
	 * 
	 * @param model The @see MacroBuilderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public MacroBuilderView(MacroBuilderModel model, 
						    IStyleContainerService styleService,
						    InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MacroBuilderView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The @see MacroBuilderModel associated with this view.
	 */
	public MacroBuilderModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		macro.editableProperty().set(false);
		macroLabel.textProperty().bind(model.macroText());
		
		createComboBoxes();
		setupPlayers();
		buildArea.getChildren().add(players);
		build.setOnAction((e) -> {
			macro.setText(model.createMacro());
		});
		build.textProperty().bind(model.buildText());
	}
	
	private void createComboBoxes() {
		players = new ComboBox<String>();
		
		players.valueProperty().addListener((v, o, n) -> {
			buildArea.getChildren().clear();
			buildArea.getChildren().add(players);
			
			if (!n.isEmpty()) {
				buildArea.getChildren().add(selector);
			}
		});
		
		selector = new ComboBox<String>();
		selector.getItems().addAll(model.propertyTypes());
		
		selector.valueProperty().addListener((v, o, n) -> {
			buildArea.getChildren().clear();
			buildArea.getChildren().add(players);
			buildArea.getChildren().add(selector);
			
			addPropertyType(n);
		});
		model.selectedPropertyType().bindBidirectional(selector.valueProperty());
		
		attributes = new ComboBox<String>();
		attributes.itemsProperty().set(model.attributes().list());
		attributes.valueProperty().addListener((v, o, n) -> {
			addPropertyNames();
			model.attributes().selected().set(n);
		});
		
		characteristics = new ComboBox<String>();
		characteristics.itemsProperty().set(model.characteristics().list());
		characteristics.valueProperty().addListener((v, o, n) -> { 
			addPropertyNames(); 
			model.characteristics().selected().set(n);
		});
		
		bodyPartOptions = new ComboBox<String>();
		bodyPartOptions.itemsProperty().set(model.bodyPartOptions().list());
		bodyPartOptions.valueProperty().addListener((v, o, n) -> {
			boolean hasChars = buildArea.getChildren().contains(characteristics);
			boolean isChar = model.convertPropertyType(n) == PropertyType.Characteristic;
			if (!hasChars && isChar) {
				buildArea.getChildren().add(characteristics);
			} else if (hasChars && !isChar) {
				buildArea.getChildren().remove(characteristics);
				removePropertyNames();
			}
			
			model.bodyPartOptions().selected().set(n);
		});
		
		bodyParts = new ComboBox<String>();
		bodyParts.itemsProperty().set(model.bodyParts().list());
		bodyParts.valueProperty().addListener((v, o, n) -> { 
			if (!buildArea.getChildren().contains(bodyPartOptions)) {
				buildArea.getChildren().add(bodyPartOptions);
			}
			
			model.bodyParts().selected().set(n);
		});
		
		itemOptions = new ComboBox<String>();
		itemOptions.itemsProperty().set(model.itemOptions().list());
		itemOptions.valueProperty().addListener((v, o, n) -> {
			boolean hasProp = buildArea.getChildren().contains(properties);
			boolean isProp = model.convertPropertyType(n) == PropertyType.Property;
			if (!hasProp && isProp) {
				buildArea.getChildren().add(properties);
			} else if (hasProp && !isProp) {
				buildArea.getChildren().remove(properties);
				removePropertyNames();
			}
			
			model.itemOptions().selected().set(n);
		});
		
		items = new ComboBox<String>();
		items.itemsProperty().set(model.items().list());
		items.valueProperty().addListener((v, o, n) -> { 
			if (!buildArea.getChildren().contains(itemOptions)) {
				buildArea.getChildren().add(itemOptions);
			}
			
			model.items().selected().set(n);
		});
		
		properties = new ComboBox<String>();
		properties.itemsProperty().set(model.properties().list());
		properties.valueProperty().addListener((v, o, n) -> { 
			addPropertyNames();
			model.properties().selected().set(n);
		});
		
		propertyNames = new ComboBox<String>();
		propertyNames.itemsProperty().get().addAll(model.namedPropertyTypes());
		model.selectedNamedProperty().bindBidirectional(propertyNames.valueProperty());
		setupStyles();
	}
	
	private void addPropertyNames() {
		if (!buildArea.getChildren().contains(propertyNames)) {
			buildArea.getChildren().add(propertyNames);
		}
	}
	
	private void removePropertyNames() {
		if (buildArea.getChildren().contains(propertyNames)) {
			buildArea.getChildren().remove(propertyNames);
		}
	}
	
	private void setupPlayers() {
		players.getItems().clear();
		
		for (PlayerPersistenceObject player : model.players()) {
			players.getItems().add(player.playerName());
		}
		
		model.selectedPlayer().bindBidirectional(players.valueProperty());
	}
	
	private void addPropertyType(String value) {
		switch (model.convertPropertyType(value)) {
			case Attribute:
				buildArea.getChildren().add(attributes);
				break;
			case BodyPart:
				buildArea.getChildren().add(bodyParts);
				break;
			case Characteristic:
				buildArea.getChildren().add(characteristics);
				break;
			case Item:
				buildArea.getChildren().add(items);
				break;
			default:
				break;
		}
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "macrostylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.MACRO), this, false);
		
		String style = styleService.get(StyledComponents.MACRO);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
