package ilusr.textadventurecreator.views.gamestate;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.search.SearchListCellFactory;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionModel;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateInspector extends AnchorPane implements Initializable{

	@FXML
	private GridPane pane;
	
	@FXML
	private Label id;
	
	@FXML
	private TextArea textLog;
	
	@FXML
	private ComboBox<String> layout;
	
	@FXML
	private ListView<OptionPersistenceObject> options;
	
	@FXML
	private Label contentLabel;
	
	@FXML
	private Label content;
	
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
	private final IDialogService dialogService;
	private final ActionViewFactory actionViewFactory;
	private final TriggerViewFactory triggerViewFactory;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private GameStateModel model;
	
	/**
	 * 
	 * @param model A @see GameStateModel to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to create dialogs.
	 * @param actionViewFactory A @see ActionViewFactory to create action views.
	 * @param triggerViewFactory A @see TriggerViewFactory to create trigger views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateInspector(GameStateModel model, 
							  ILanguageService languageService, 
							  IDialogService dialogService,
							  ActionViewFactory actionViewFactory,
							  TriggerViewFactory triggerViewFactory,
							  IDialogProvider dialogProvider,
							  IStyleContainerService styleService,
							  InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.actionViewFactory = actionViewFactory;
		this.triggerViewFactory = triggerViewFactory;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameStateInspector.fxml"));
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
	 * @return The associated @see GameStateModel.
	 */
	public GameStateModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (model.hasContent()) {
			showContent();
		} else {
			hideContent();
		}
		
		id.textProperty().bind(model.stateId());
		textLog.textProperty().bind(model.textLog());
		textLog.setEditable(false);
		layout.itemsProperty().set(model.layouts().list());
		content.textProperty().bind(model.contentLocation());
		idLabel.textProperty().bind(model.idText());
		textLabel.textProperty().bind(model.textLogText());
		layoutLabel.textProperty().bind(model.layoutText());
		contentLabel.textProperty().bind(model.contentText());
		optionLabel.textProperty().bind(model.optionText());
		timerLabel.textProperty().bind(model.timerText());
		
		layout.setValue(model.layouts().selected().get());
		layout.setDisable(true);
		
		setupOptions();
		setupTimers();
	}
	
	private void setupOptions() {
		options.setCellFactory(new SearchListCellFactory<OptionPersistenceObject>(
											new OptionViewCreator(), 
											(i) -> { 
												OptionModel model = new OptionModel(dialogService, i, null, languageService, null, dialogProvider, styleService, urlProvider);
												ActionModel actionModel = new ActionModel(model.action().get(), languageService);												
												OptionInspector view = new OptionInspector(model, actionViewFactory.create(actionModel), triggerViewFactory, languageService, dialogService);
												dialogService.displayModal(view, languageService.getValue(DisplayStrings.OPTION));
											}));
		ObservableListBinder<OptionPersistenceObject> optionBinder = new ObservableListBinder<OptionPersistenceObject>(model.options(), options.getItems());
		optionBinder.bindSourceToTarget();
	}
	
	private void setupTimers() {
		timers.setCellFactory(new TimerCellFactory(new CompletionTimerViewCreator(languageService)));
		ObservableListBinder<CompletionTimerPersistenceObject> timerBinder = new ObservableListBinder<CompletionTimerPersistenceObject>(model.timers(), timers.getItems());
		timerBinder.bindSourceToTarget();
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
	}
	
	private class TimerCellFactory implements Callback<ListView<CompletionTimerPersistenceObject>, ListCell<CompletionTimerPersistenceObject>> {

		private CompletionTimerViewCreator viewCreator;

		public TimerCellFactory(CompletionTimerViewCreator viewCreator) {
			this.viewCreator = viewCreator;
		}
		
		@Override
		public ListCell<CompletionTimerPersistenceObject> call(ListView<CompletionTimerPersistenceObject> param) {
			return new TimerListCell(viewCreator);
		}
	}
	
	private class TimerListCell extends ListCell<CompletionTimerPersistenceObject> {
		private CompletionTimerViewCreator viewCreator;
		
		public TimerListCell(CompletionTimerViewCreator viewCreator) {
			this.viewCreator = viewCreator;
		}
		
		@Override
		public void updateItem(CompletionTimerPersistenceObject item, boolean empty) {
			super.updateItem(item, empty);
			
			if (empty || item == null) {
				setText("");
				setGraphic(null);
				return;
			}
			
			Node node = viewCreator.create(item);
			node.setDisable(true);
			setGraphic(node);
		}
	}
}
