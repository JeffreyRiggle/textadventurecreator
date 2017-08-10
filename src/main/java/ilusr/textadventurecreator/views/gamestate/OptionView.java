package ilusr.textadventurecreator.views.gamestate;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.core.ViewSwitcher;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.EditRemoveListCellFactory;
import ilusr.textadventurecreator.views.action.AppendTextModel;
import ilusr.textadventurecreator.views.action.AppendTextViewProvider;
import ilusr.textadventurecreator.views.action.CompletionActionModel;
import ilusr.textadventurecreator.views.action.CompletionActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutableActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutionActionModel;
import ilusr.textadventurecreator.views.action.FinishActionModel;
import ilusr.textadventurecreator.views.action.FinishActionViewProvider;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.action.PlayerModificationActionViewProvider;
import ilusr.textadventurecreator.views.action.SaveActionModel;
import ilusr.textadventurecreator.views.action.SaveActionViewProvider;
import ilusr.textadventurecreator.views.action.ScriptedActionModel;
import ilusr.textadventurecreator.views.action.ScriptedActionViewProvider;
import ilusr.textadventurecreator.views.trigger.TriggerViewCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ListView<TriggerPersistenceObject> triggers;
	
	@FXML
	private ComboBox<String> actionType;
	
	@FXML
	private ViewSwitcher<String> action;
	
	@FXML
	private Button importAction;
	
	@FXML
	private Label triggerLabel;
	
	@FXML
	private Label actionLabel;
	
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private OptionModel model;
	private AppendTextViewProvider appendTextProvider;
	private CompletionActionViewProvider completionProvider;
	private ExecutableActionViewProvider exeProvider;
	private SaveActionViewProvider saveProvider;
	private PlayerModificationActionViewProvider playerProvider;
	private ScriptedActionViewProvider scriptProvider;
	private FinishActionViewProvider finishProvider;
	private PlayerModProviderFactory playerModFactory;
	
	/**
	 * 
	 * @param model The @see OptionModel to bind to.
	 * @param appendTextProvider A @see AppendTextProvider to provide append text views.
	 * @param completionProvider A @see CompletionActionViewProvider to provide completion views.
	 * @param exeProvider A @see ExecutableActionViewProvider to provide execution views.
	 * @param saveProvider A @see SaveActionViewProvider to provide save views.
	 * @param playerProvider A @see PlayerModificationActionViewProvider to provide player modification views.
	 * @param scriptProvider A @see ScriptedActionViewProvider to provide script views.
	 * @param finishProvider A @see FinishActionViewProvider to provide finish views.
	 * @param playerModFactory A @see PlayerModProviderFactory to provide player mod views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionView(OptionModel model, 
					  AppendTextViewProvider appendTextProvider, 
					  CompletionActionViewProvider completionProvider, 
					  ExecutableActionViewProvider exeProvider,
					  SaveActionViewProvider saveProvider,
					  PlayerModificationActionViewProvider playerProvider,
					  ScriptedActionViewProvider scriptProvider,
					  FinishActionViewProvider finishProvider,
					  PlayerModProviderFactory playerModFactory,
					  ILanguageService languageService,
					  IStyleContainerService styleService,
					  InternalURLProvider urlProvider) {
		this.model = model;
		this.appendTextProvider = appendTextProvider;
		this.completionProvider = completionProvider;
		this.exeProvider = exeProvider;
		this.saveProvider = saveProvider;
		this.playerProvider = playerProvider;
		this.scriptProvider = scriptProvider;
		this.finishProvider = finishProvider;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.urlProvider = urlProvider;
		this.styleService = styleService;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OptionView.fxml"));
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
		actionLabel.textProperty().bind(model.actionText());
		
		if (model.allowLibraryAdd()) {
			setupLibrary();
		} else {
			hideLibrary();
		}

		setupViewSwitcher();
		setupTriggers();
		
		actionType.setItems(model.types().list());
		actionType.valueProperty().addListener((v, o, n) -> {
			action.switchView(n);
			model.types().selected().set(n);
			updateModel(n);
		});
		
		if (model.types().selected().get() != null) {
			actionType.valueProperty().set(model.types().selected().get());
		}
		
		setupStyles();
	}
	
	private void setupLibrary() {
		importAction.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		
		importAction.setOnAction((e) -> {
			model.addActionFromLibrary((action) -> {
				importAction(action);
			});
		});
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "optionstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.OPTION), this, false);
		
		String style = styleService.get(StyledComponents.OPTION);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	private void hideLibrary() {
		importAction.setVisible(false);
	}
	
	private void setupTriggers() {
		if (model.allowLibraryAdd()) {
			triggers.setCellFactory(new EditRemoveListCellFactory<TriggerPersistenceObject>(model.addTriggerKey(), 
					  model.getEditTriggerAction(), 
					  addTemplate(),
					  new TriggerViewCreator(),
					  (m) -> { model.triggers().remove(m); }));
		} else {
			triggers.setCellFactory(new EditRemoveListCellFactory<TriggerPersistenceObject>(model.addTriggerKey(), 
					  model.getEditTriggerAction(), 
					  (e) -> { model.addTrigger(); },
					  new TriggerViewCreator(),
					  (m) -> { model.triggers().remove(m); }));
		}
		
		
		triggers.itemsProperty().get().add(model.addTriggerKey());
		ObservableListBinder<TriggerPersistenceObject> binder = new ObservableListBinder<TriggerPersistenceObject>(model.triggers(), triggers.getItems());
		binder.bindSourceToTarget();
	}
	
	private Node addTemplate() {
		HBox root = new HBox(5);
		Button add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setOnAction((e) -> { model.addTrigger(); });
		
		Button addL = new Button();
		addL.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addL.setOnAction((e) -> { model.addTriggerFromLibrary(); });
		
		root.getChildren().add(add);
		root.getChildren().add(addL);
		return root;
	}
	
	private void importAction(ActionPersistenceObject action) {
		String type = null;
		
		if (action instanceof AppendTextActionPersistence) {
			appendTextProvider = new AppendTextViewProvider(new AppendTextModel((AppendTextActionPersistence)action, languageService));
			type = model.appendAction();
		} else if (action instanceof CompletionActionPersistence) {
			completionProvider = new CompletionActionViewProvider(new CompletionActionModel((CompletionActionPersistence)action, languageService));
			type = model.completionAction();
		} else if (action instanceof ExecutionActionPersistence) {
			exeProvider = new ExecutableActionViewProvider(new ExecutionActionModel((ExecutionActionPersistence)action, languageService));
			type = model.executionAction();
		} else if (action instanceof SaveActionPersistenceObject) {
			saveProvider = new SaveActionViewProvider(new SaveActionModel((SaveActionPersistenceObject)action, languageService));
			type = model.saveAction();
		} else if (action instanceof ModifyPlayerActionPersistence) {
			if (model.players() != null) {
				playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action, model.players());
			} else {
				playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action);
			}
			
			type = model.playerAction();
		} else if (action instanceof ScriptedActionPersistenceObject) {
			scriptProvider = new ScriptedActionViewProvider(new ScriptedActionModel((ScriptedActionPersistenceObject)action, languageService));
			type = model.scriptAction();
		} else if (action instanceof FinishActionPersistenceObject) {
			finishProvider = new FinishActionViewProvider(new FinishActionModel((FinishActionPersistenceObject)action, languageService));
			type = model.finishAction();
		}
		
		if (type == null) {
			return;
		}
		
		this.action.clearViews();
		setupViewSwitcher();
		actionType.setValue(type);
	}
	
	private void setupViewSwitcher() {
		action.addView(model.appendAction(), appendTextProvider);
		action.addView(model.completionAction(), completionProvider);
		action.addView(model.executionAction(), exeProvider);
		action.addView(model.saveAction(), saveProvider);
		action.addView(model.playerAction(), playerProvider);
		action.addView(model.scriptAction(), scriptProvider);
		action.addView(model.finishAction(), finishProvider);
	}
	
	private void updateModel(String type) {
		if (type.equals(model.appendAction())) {
			model.action().set(appendTextProvider.getView().getPersistenceObject());
		} else if (type.equals(model.completionAction())) {
			model.action().set(completionProvider.getView().getPersistenceObject());
		} else if (type.equals(model.executionAction())) {
			model.action().set(exeProvider.getView().getPersistenceObject());
		} else if (type.equals(model.saveAction())) {
			model.action().set(saveProvider.getView().getPersistenceObject());
		} else if (type.equals(model.playerAction())) {
			model.action().set(playerProvider.getView().getPersistenceObject());
		} else if (type.equals(model.scriptAction())) {
			model.action().set(scriptProvider.getView().getPersistenceObject());
		} else if (type.equals(model.finishAction())) {
			model.action().set(finishProvider.getView().getPersistenceObject());
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
