package ilusr.textadventurecreator.views.gamestate;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.search.SearchListCellFactory;
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionInspector extends AnchorPane implements Initializable {

	@FXML
	private ListView<TriggerPersistenceObject> triggers;
	
	@FXML
	private AnchorPane action;
	
	@FXML
	private Label triggerLabel;
	
	private final TriggerViewFactory viewFactory;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	
	private OptionModel model;
	private Node actionNode;
	
	/**
	 * 
	 * @param model A @see OptionModel to bind to.
	 * @param actionNode A @see Node to display for the action.
	 * @param viewFactory A @see TriggerViewFactory to create trigger views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 */
	public OptionInspector(OptionModel model, 
						   Node actionNode, 
						   TriggerViewFactory viewFactory,
						   ILanguageService languageService,
						   IDialogService dialogService) {
		this.model = model;
		this.actionNode = actionNode;
		this.viewFactory = viewFactory;
		this.languageService = languageService;
		this.dialogService = dialogService;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OptionInspector.fxml"));
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
		triggerLabel.textProperty().bind(model.triggerText());

		action.getChildren().add(actionNode);
		AnchorPane.setBottomAnchor(actionNode, 0.0);
		AnchorPane.setTopAnchor(actionNode, 0.0);
		AnchorPane.setLeftAnchor(actionNode, 0.0);
		AnchorPane.setRightAnchor(actionNode, 0.0);
		actionNode.setDisable(true);
		
		setupTriggers();
	}
	
	private void setupTriggers() {		
		triggers.setCellFactory(new SearchListCellFactory<TriggerPersistenceObject>(
				(t) -> { return new Label(t.type()); }, 
				(i) -> { 
					TriggerModel model = new TriggerModel(i, languageService);
					TriggerView view = viewFactory.create(model);
					view.setDisable(true);
					dialogService.displayModal(view, languageService.getValue(DisplayStrings.TRIGGER));
				}));
		triggers.itemsProperty().get().add(model.addTriggerKey());
		ObservableListBinder<TriggerPersistenceObject> binder = new ObservableListBinder<TriggerPersistenceObject>(model.triggers(), triggers.getItems());
		binder.bindSourceToTarget();
	}
}
