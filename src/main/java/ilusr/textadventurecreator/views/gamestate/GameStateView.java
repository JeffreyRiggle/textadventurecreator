package ilusr.textadventurecreator.views.gamestate;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateView extends AnchorPane implements Initializable, IStyleWatcher{

	@FXML
	private GridPane pane;
	
	@FXML
	private TextField gameStateId;
	
	@FXML
	private TextArea textLog;
	
	@FXML
	private Button macro;
	
	@FXML
	private ComboBox<String> layout;
	
	@FXML
	private ListView<OptionPersistenceObject> options;
	
	@FXML
	private Label contentLabel;
	
	@FXML
	private TextField content;
	
	@FXML
	private Button contentButton;
	
	@FXML
	private ListView<CompletionTimerPersistenceObject> timers;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private Label textLabel;
	
	@FXML
	private Label layoutLabel;
	
	@FXML
	private Label optionLabel;
	
	@FXML
	private Label timerLabel;
	
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private GameStateModel model;
	
	/**
	 * 
	 * @param model The @see GameStateModel to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateView(GameStateModel model, 
						 ILanguageService languageService,
						 IStyleContainerService styleService,
						 InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameStateView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	public GameStateModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		hideContent();
		gameStateId.textProperty().bindBidirectional(model.stateId());
		textLog.textProperty().bindBidirectional(model.textLog());
		model.textIndex().bind(textLog.caretPositionProperty());
		layout.itemsProperty().set(model.layouts().list());
		content.textProperty().bindBidirectional(model.contentLocation());
		idLabel.textProperty().bind(model.idText());
		textLabel.textProperty().bind(model.textLogText());
		layoutLabel.textProperty().bind(model.layoutText());
		contentLabel.textProperty().bind(model.contentText());
		optionLabel.textProperty().bind(model.optionText());
		timerLabel.textProperty().bind(model.timerText());
		
		layout.setValue(model.layouts().selected().get());
		layout.valueProperty().addListener((v, o, n) -> {
			model.layouts().selected().set(n);
			
			if (model.hasContent()) {
				showContent();
			} else {
				hideContent();
			}
		});
		
		macro.setOnAction((e) -> {
			model.buildMacro();
		});
		macro.textProperty().bind(model.macroText());
		
		contentButton.setOnAction((e) -> {
			model.browseContent(super.getScene().getWindow());
		});
		contentButton.textProperty().bind(model.browseText());
		
		setupOptions();
		setupTimers();
		setupStyles();
	}
	
	private void setupOptions() {
		if (model.allowLibraryAdd()) {
			options.setCellFactory(new EditRemoveListCellFactory<OptionPersistenceObject>(model.addOptionKey(), 
					  model.getEditOptionAction(), 
					  optionAddTemplate(),
					  new OptionViewCreator(),
					  (m) -> { model.options().remove(m); }));
		} else {
			options.setCellFactory(new EditRemoveListCellFactory<OptionPersistenceObject>(model.addOptionKey(), 
					  model.getEditOptionAction(), 
					  (e) -> { model.addOption(); }, 
					  new OptionViewCreator(),
					  (m) -> { model.options().remove(m); }));
		}
		
		
		options.itemsProperty().get().add(model.addOptionKey());
		ObservableListBinder<OptionPersistenceObject> optionBinder = new ObservableListBinder<OptionPersistenceObject>(model.options(), options.getItems());
		optionBinder.bindSourceToTarget();
	}
	
	private Node optionAddTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.setId("listCellAdd");
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addOption(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addOptionFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private void setupTimers() {
		if (model.allowLibraryAdd()) {
			timers.setCellFactory(new RemoveListCellFactory<CompletionTimerPersistenceObject>(model.addTimerKey(), 
					  timerAddTemplate(), 
					  new CompletionTimerViewCreator(languageService),
					  (m) -> { model.timers().remove(m); }));
		} else {
			timers.setCellFactory(new RemoveListCellFactory<CompletionTimerPersistenceObject>(model.addTimerKey(), 
					  (e) -> { model.addTimer(); }, 
					  new CompletionTimerViewCreator(languageService),
					  (m) -> { model.timers().remove(m); }));
		}
		
		timers.itemsProperty().get().add(model.addTimerKey());
		ObservableListBinder<CompletionTimerPersistenceObject> timerBinder = new ObservableListBinder<CompletionTimerPersistenceObject>(model.timers(), timers.getItems());
		timerBinder.bindSourceToTarget();
	}
	
	private Node timerAddTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addTimer(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addTimerFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private void showContent() {
		RowConstraints row5 = pane.getRowConstraints().get(5);
		RowConstraints row6 = pane.getRowConstraints().get(6);
		
		row5.setMaxHeight(30);
		row5.setPrefHeight(30);
		row6.setMaxHeight(100);
		row6.setPrefHeight(100);
		contentLabel.setVisible(true);
		content.setVisible(true);
		contentButton.setVisible(true);
	}
	
	private void hideContent() {
		RowConstraints row5 = pane.getRowConstraints().get(5);
		RowConstraints row6 = pane.getRowConstraints().get(6);
		
		row5.setMaxHeight(0);
		row5.setPrefHeight(0);
		row6.setMaxHeight(0);
		row6.setPrefHeight(0);
		contentLabel.setVisible(false);
		content.setVisible(false);
		contentButton.setVisible(false);
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "gamestatestylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.GAME_STATE), this, false);
		
		String style = styleService.get(StyledComponents.GAME_STATE);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
