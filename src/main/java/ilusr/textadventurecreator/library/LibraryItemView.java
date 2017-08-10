package ilusr.textadventurecreator.library;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.EditRemoveListCellFactory;
import ilusr.textadventurecreator.views.RemoveListCellFactory;
import ilusr.textadventurecreator.views.gamestate.CompletionTimerViewCreator;
import ilusr.textadventurecreator.views.player.AttributeViewCreator;
import ilusr.textadventurecreator.views.player.CharacteristicViewCreator;
import ilusr.textadventurecreator.views.player.PropertiesViewCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryItemView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ListView<AttributePersistenceObject> attributes;
	
	@FXML
	private ListView<CharacteristicPersistenceObject> characteristics;
	
	@FXML
	private ListView<BodyPartPersistenceObject> bodyParts;
	
	@FXML
	private ListView<ItemPersistenceObject> items;
	
	@FXML
	private ListView<PropertyPersistenceObject> properties;
	
	@FXML
	private ListView<PlayerPersistenceObject> players;
	
	@FXML
	private ListView<TriggerPersistenceObject> triggers;
	
	@FXML
	private ListView<ActionPersistenceObject> actions;
	
	@FXML
	private ListView<OptionPersistenceObject> options;
	
	@FXML
	private ListView<CompletionTimerPersistenceObject> timers;
	
	@FXML
	private ListView<GameStatePersistenceObject> gameStates;
	
	@FXML
	private ListView<LayoutPersistenceObject> layouts;
	
	@FXML
	private TitledPane attributePane;

	@FXML
	private TitledPane characteristicPane;
	
	@FXML
	private TitledPane bodyPartPane;
	
	@FXML
	private TitledPane propertyPane;
	
	@FXML
	private TitledPane itemPane;
	
	@FXML
	private TitledPane playerPane;
	
	@FXML
	private TitledPane triggerPane;
	
	@FXML
	private TitledPane actionPane;
	
	@FXML
	private TitledPane optionPane;
	
	@FXML
	private TitledPane timerPane;
	
	@FXML
	private TitledPane gameStatePane;
	
	@FXML
	private TitledPane layoutPane;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField author;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label authorLabel;
	
	private final ILanguageService languageService;
	private LibraryItemModel model;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param model A @see LibraryItemModel to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public LibraryItemView(LibraryItemModel model, 
						   ILanguageService languageService,
						   IStyleContainerService styleService,
						   InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LibraryItemView.fxml"));
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
	 * @return A @see LibraryItemModel associated with this view.
	 */
	public LibraryItemModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		attributes.setCellFactory(new RemoveListCellFactory<AttributePersistenceObject>(model.addAttributeKey(), 
				model.getAddAttributeAction(), 
				new AttributeViewCreator(languageService),
			    (m) -> {  model.attributes().remove(m); }));
		attributes.itemsProperty().get().add(model.addAttributeKey());
		ObservableListBinder<AttributePersistenceObject> attibuteBinder = new ObservableListBinder<AttributePersistenceObject>(model.attributes(), attributes.getItems());
		attibuteBinder.bindSourceToTarget();
		
		characteristics.setCellFactory(new RemoveListCellFactory<CharacteristicPersistenceObject>(model.addCharacteristicKey(),
				model.getAddCharacteristicAction(),
				new CharacteristicViewCreator(languageService),
				(m) -> { model.characteristics().remove(m); }));
		characteristics.itemsProperty().get().add(model.addCharacteristicKey());
		ObservableListBinder<CharacteristicPersistenceObject> charBinder = new ObservableListBinder<CharacteristicPersistenceObject>(model.characteristics(), characteristics.getItems());
		charBinder.bindSourceToTarget();
		
		bodyParts.setCellFactory(new EditRemoveListCellFactory<BodyPartPersistenceObject>(model.addBodyPartKey(), 
				  model.getEditBodyPartAction(), 
				  model.getAddBodyPartAction(),
				  null,
				  (m) -> { model.bodyParts().remove(m); }));
		bodyParts.itemsProperty().get().add(model.addBodyPartKey());
		ObservableListBinder<BodyPartPersistenceObject> bodyPartBinder = new ObservableListBinder<BodyPartPersistenceObject>(model.bodyParts(), bodyParts.getItems());
		bodyPartBinder.bindSourceToTarget();
		
		properties.setCellFactory(new RemoveListCellFactory<PropertyPersistenceObject>(model.addPropertyKey(), 
				model.getAddPropertyAction(), 
				new PropertiesViewCreator(languageService),
			    (m) -> {  model.properties().remove(m); }));
		properties.itemsProperty().get().add(model.addPropertyKey());
		ObservableListBinder<PropertyPersistenceObject> propBinder = new ObservableListBinder<PropertyPersistenceObject>(model.properties(), properties.getItems());
		propBinder.bindSourceToTarget();
		
		items.setCellFactory(new EditRemoveListCellFactory<ItemPersistenceObject>(model.addItemKey(), 
				  model.getEditItemAction(), 
				  model.getAddItemAction(),
				  null,
				  (m) -> { model.items().remove(m); }));
		items.itemsProperty().get().add(model.addItemKey());
		ObservableListBinder<ItemPersistenceObject> itemBinder = new ObservableListBinder<ItemPersistenceObject>(model.items(), items.getItems());
		itemBinder.bindSourceToTarget();
		
		players.setCellFactory(new EditRemoveListCellFactory<PlayerPersistenceObject>(model.addPlayerKey(), 
				  model.getEditPlayerAction(), 
				  model.getAddPlayerAction(),
				  null,
				  (m) -> { model.players().remove(m); }));
		players.itemsProperty().get().add(model.addPlayerKey());
		ObservableListBinder<PlayerPersistenceObject> playerBinder = new ObservableListBinder<PlayerPersistenceObject>(model.players(), players.getItems());
		playerBinder.bindSourceToTarget();
		
		triggers.setCellFactory(new EditRemoveListCellFactory<TriggerPersistenceObject>(model.addTriggerKey(), 
				  model.getEditTriggerAction(), 
				  model.getAddTriggerAction(),
				  null,
				  (m) -> { model.triggers().remove(m); }));
		triggers.itemsProperty().get().add(model.addTriggerKey());
		ObservableListBinder<TriggerPersistenceObject> triggerBinder = new ObservableListBinder<TriggerPersistenceObject>(model.triggers(), triggers.getItems());
		triggerBinder.bindSourceToTarget();
		
		actions.setCellFactory(new EditRemoveListCellFactory<ActionPersistenceObject>(model.addActionKey(), 
				  model.getEditActionAction(), 
				  model.getAddActionAction(),
				  null,
				  (m) -> { model.actions().remove(m); }));
		actions.itemsProperty().get().add(model.addActionKey());
		ObservableListBinder<ActionPersistenceObject> actionBinder = new ObservableListBinder<ActionPersistenceObject>(model.actions(), actions.getItems());
		actionBinder.bindSourceToTarget();
		
		options.setCellFactory(new EditRemoveListCellFactory<OptionPersistenceObject>(model.addOptionKey(), 
				  model.getEditOptionAction(), 
				  model.getAddOptionAction(),
				  null,
				  (m) -> { model.options().remove(m); }));
		options.itemsProperty().get().add(model.addOptionKey());
		ObservableListBinder<OptionPersistenceObject> optionBinder = new ObservableListBinder<OptionPersistenceObject>(model.options(), options.getItems());
		optionBinder.bindSourceToTarget();

		timers.setCellFactory(new RemoveListCellFactory<CompletionTimerPersistenceObject>(model.addTimerKey(), 
				  model.getAddTimerAction(), 
				  new CompletionTimerViewCreator(languageService),
				  (m) -> { model.timers().remove(m); }));
		
		timers.itemsProperty().get().add(model.addTimerKey());
		ObservableListBinder<CompletionTimerPersistenceObject> timerBinder = new ObservableListBinder<CompletionTimerPersistenceObject>(model.timers(), timers.getItems());
		timerBinder.bindSourceToTarget();
		
		gameStates.setCellFactory(new EditRemoveListCellFactory<GameStatePersistenceObject>(model.addGameStateKey(), 
				  model.getEditGameStateAction(), 
				  model.getAddGameStateAction(),
				  null,
				  (m) -> { model.gameStates().remove(m); }));
		gameStates.itemsProperty().get().add(model.addGameStateKey());
		ObservableListBinder<GameStatePersistenceObject> gameStateBinder = new ObservableListBinder<GameStatePersistenceObject>(model.gameStates(), gameStates.getItems());
		gameStateBinder.bindSourceToTarget();
		
		layouts.setCellFactory(new EditRemoveListCellFactory<LayoutPersistenceObject>(model.addLayoutKey(), 
				  model.getEditLayoutAction(), 
				  model.getAddLayoutAction(),
				  (p) -> { return new Label(p.id()); },
				  (m) -> { model.layouts().remove(m); }));
		layouts.itemsProperty().get().add(model.addLayoutKey());
		ObservableListBinder<LayoutPersistenceObject> layoutBinder = new ObservableListBinder<LayoutPersistenceObject>(model.layouts(), layouts.getItems());
		layoutBinder.bindSourceToTarget();
		
		name.textProperty().bindBidirectional(model.name());
		author.textProperty().bindBidirectional(model.author());
		
		bindPaneText();
		collapsePanes();
		setupStyles();
	}

	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "libraryitemstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.LIBRARY_ITEM), this, false);
		
		String style = styleService.get(StyledComponents.LIBRARY_ITEM);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	private void bindPaneText() {
		attributePane.textProperty().bind(model.attributeText());
		characteristicPane.textProperty().bind(model.characteristicText());
		bodyPartPane.textProperty().bind(model.bodyPartText());
		propertyPane.textProperty().bind(model.propertyText());
		itemPane.textProperty().bind(model.itemText());
		playerPane.textProperty().bind(model.playerText());
		triggerPane.textProperty().bind(model.triggerText());
		actionPane.textProperty().bind(model.actionText());
		optionPane.textProperty().bind(model.optionText());
		timerPane.textProperty().bind(model.timerText());
		gameStatePane.textProperty().bind(model.gameStateText());
		layoutPane.textProperty().bind(model.layoutText());
		nameLabel.textProperty().bind(model.nameText());
		authorLabel.textProperty().bind(model.authorText());
	}
	
	private void collapsePanes() {
		attributePane.setExpanded(false);
		characteristicPane.setExpanded(false);
		bodyPartPane.setExpanded(false);
		propertyPane.setExpanded(false);
		itemPane.setExpanded(false);
		playerPane.setExpanded(false);
		triggerPane.setExpanded(false);
		actionPane.setExpanded(false);
		optionPane.setExpanded(false);
		timerPane.setExpanded(false);
		gameStatePane.setExpanded(false);
		layoutPane.setExpanded(false);
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
