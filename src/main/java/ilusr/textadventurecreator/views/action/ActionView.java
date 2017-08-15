package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.iroshell.core.ViewSwitcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionView extends AnchorPane implements Initializable {

	@FXML
	private ComboBox<String> actionType;
	
	@FXML
	private ViewSwitcher<String> action;
	
	@FXML
	private Label actionLabel;
	
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	
	private ActionModel model;
	private AppendTextViewProvider appendTextProvider;
	private CompletionActionViewProvider completionProvider;
	private ExecutableActionViewProvider exeProvider;
	private SaveActionViewProvider saveProvider;
	private PlayerModificationActionViewProvider playerProvider;
	private ScriptedActionViewProvider scriptProvider;
	private FinishActionViewProvider finishProvider;
	private List<PlayerPersistenceObject> players;
	
	/**
	 * 
	 * @param model A @see ActionModel to bind to.
	 * @param appendTextProvider A view provider for the append text action.
	 * @param completionProvider A view provider for the completion action.
	 * @param exeProvider A view provider for the executable action.
	 * @param saveProvider A view provider for the save action.
	 * @param playerProvider A view provider for the player action.
	 * @param scriptProvider A view provider for the script action.
	 * @param finishProvider A view provider for the finish action.
	 * @param playerModFactory A view provider for the player mod action.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ActionView(ActionModel model, 
			  AppendTextViewProvider appendTextProvider, 
	          CompletionActionViewProvider completionProvider, 
	          ExecutableActionViewProvider exeProvider,
	          SaveActionViewProvider saveProvider,
	          PlayerModificationActionViewProvider playerProvider,
	          ScriptedActionViewProvider scriptProvider,
	          FinishActionViewProvider finishProvider,
	          PlayerModProviderFactory playerModFactory,
	          ILanguageService languageService) {
		this(model, appendTextProvider, completionProvider, exeProvider, saveProvider, playerProvider,
				scriptProvider, finishProvider, playerModFactory, null, languageService);
		
	}
	
	/**
	 * 
	 * @param model A @see ActionModel to bind to.
	 * @param appendTextProvider A view provider for the append text action.
	 * @param completionProvider A view provider for the completion action.
	 * @param exeProvider A view provider for the executable action.
	 * @param saveProvider A view provider for the save action.
	 * @param playerProvider A view provider for the player action.
	 * @param scriptProvider A view provider for the script action.
	 * @param finishProvider A view provider for the finish action.
	 * @param playerModFactory A view provider for the player mod action.
	 * @param players A list of players to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ActionView(ActionModel model, 
					  AppendTextViewProvider appendTextProvider, 
			          CompletionActionViewProvider completionProvider, 
			          ExecutableActionViewProvider exeProvider,
			          SaveActionViewProvider saveProvider,
			          PlayerModificationActionViewProvider playerProvider,
			          ScriptedActionViewProvider scriptProvider,
			          FinishActionViewProvider finishProvider,
			          PlayerModProviderFactory playerModFactory,
			          List<PlayerPersistenceObject> players,
			          ILanguageService languageService) {
		this.model = model;
		this.appendTextProvider = appendTextProvider;
		this.completionProvider = completionProvider;
		this.exeProvider = exeProvider;
		this.saveProvider = saveProvider;
		this.playerProvider = playerProvider;
		this.scriptProvider = scriptProvider;
		this.finishProvider = finishProvider;
		this.playerModFactory = playerModFactory;
		this.players = players;
		this.languageService = languageService;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ActionView.fxml"));
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
		setupViewSwitcher();
		
		actionType.setItems(model.types().list());
		actionType.valueProperty().addListener((v, o, n) -> {
			LogRunner.logger().info(String.format("Switching action view to %s", n));
			action.switchView(n);
			model.types().selected().set(n);
			updateModel(n);
		});
		
		if (model.types().selected().get() != null) {
			actionType.valueProperty().set(model.types().selected().get());
		}
		
		actionLabel.textProperty().bind(model.actionText());
	}

	private void setupViewSwitcher() {
		if (model.persistenceObject().get() != null) {
			bindPersistence();
		}
		
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
			model.persistenceObject().set(appendTextProvider.getView().getPersistenceObject());
		} else if (type.equals(model.completionAction())) {
			model.persistenceObject().set(completionProvider.getView().getPersistenceObject());
		} else if (type.equals(model.executionAction())) {
			model.persistenceObject().set(exeProvider.getView().getPersistenceObject());
		} else if (type.equals(model.saveAction())) {
			model.persistenceObject().set(saveProvider.getView().getPersistenceObject());
		} else if (type.equals(model.playerAction())) {
			model.persistenceObject().set(playerProvider.getView().getPersistenceObject());
		} else if (type.equals(model.scriptAction())) {
			model.persistenceObject().set(scriptProvider.getView().getPersistenceObject());
		} else if (type.equals(model.finishAction())) {
			model.persistenceObject().set(finishProvider.getView().getPersistenceObject());
		}
	}
	
	private void bindPersistence() {
		ActionPersistenceObject action = model.persistenceObject().get();
		
		if (action instanceof AppendTextActionPersistence) {
			appendTextProvider = new AppendTextViewProvider(new AppendTextModel((AppendTextActionPersistence)action, languageService));
		} else if (action instanceof CompletionActionPersistence) {
			completionProvider = new CompletionActionViewProvider(new CompletionActionModel((CompletionActionPersistence)action, languageService));
		} else if (action instanceof ExecutionActionPersistence) {
			exeProvider = new ExecutableActionViewProvider(new ExecutionActionModel((ExecutionActionPersistence)action, languageService));
		} else if (action instanceof SaveActionPersistenceObject) {
			saveProvider = new SaveActionViewProvider(new SaveActionModel((SaveActionPersistenceObject)action, languageService));
		} else if (action instanceof ModifyPlayerActionPersistence) {
			if (players == null) {
				playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action);
			} else {
				playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action, players);
			}
		} else if (action instanceof ScriptedActionPersistenceObject) {
			scriptProvider = new ScriptedActionViewProvider(new ScriptedActionModel((ScriptedActionPersistenceObject)action, languageService));
		} else if (action instanceof FinishActionPersistenceObject) {
			finishProvider = new FinishActionViewProvider(new FinishActionModel((FinishActionPersistenceObject)action, languageService));
		}
	}
}
