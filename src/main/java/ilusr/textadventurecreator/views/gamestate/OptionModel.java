package ilusr.textadventurecreator.views.gamestate;

import java.util.List;

import ilusr.core.interfaces.Callback;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.ActionFinder;
import ilusr.textadventurecreator.search.ActionFinderModel;
import ilusr.textadventurecreator.search.TriggerFinder;
import ilusr.textadventurecreator.search.TriggerFinderModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionModel {

	private final String APPEND_ACTION = "Append Text";
	private final String COMPLETION_ACTION = "Complete";
	private final String EXECUTION_ACTION = "Execute";
	private final String MOD_PLAYER_ACTION = "Modify Player";
	private final String SAVE_ACTION = "Save action";
	private final String SCRIPT_ACTION = "Script Action";
	private final String FINISH_ACTION = "Finish Action";
	
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final TriggerViewFactory factory;
	private final ILanguageService languageService;
	private final List<PlayerPersistenceObject> players;
	private final ActionViewFactory actionViewFactory;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private OptionPersistenceObject option;
	private ObservableList<TriggerPersistenceObject> triggers;
	private SimpleObjectProperty<ActionPersistenceObject> action;
	private TriggerPersistenceObject addTriggerKey;
	private SelectionAwareObservableList<String> types;
	private LanguageAwareString triggerText;
	private LanguageAwareString actionText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param option A @see OptionPersistenceObject to bind to.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionModel(IDialogService dialogService, 
					   OptionPersistenceObject option, 
					   TriggerViewFactory factory, 
					   ILanguageService languageService,
					   ActionViewFactory actionViewFactory,
					   IDialogProvider dialogProvider,
					   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this(dialogService, option, factory, null, languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param option A @see OptionPersistenceObject to bind to.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param players The players to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionModel(IDialogService dialogService, 
					   OptionPersistenceObject option, 
					   TriggerViewFactory factory,
					   List<PlayerPersistenceObject> players,
					   ILanguageService languageService,
					   ActionViewFactory actionViewFactory,
					   IDialogProvider dialogProvider,
					   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this(dialogService, null, option, factory, players, languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param option A @see OptionPersistenceObject to bind to.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param players The players to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionModel(IDialogService dialogService,
					   LibraryService libraryService,
			   		   OptionPersistenceObject option, 
			   		   TriggerViewFactory factory,
			   		   List<PlayerPersistenceObject> players,
			   		   ILanguageService languageService,
			   		   ActionViewFactory actionViewFactory,
			   		   IDialogProvider dialogProvider,
			   		   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.option = option;
		this.factory = factory;
		this.players = players;
		this.languageService = languageService;
		this.actionViewFactory = actionViewFactory;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		triggers = FXCollections.observableArrayList();
		types = new SelectionAwareObservableList<String>();
		triggerText = new LanguageAwareString(languageService, DisplayStrings.TRIGGERS);
		actionText = new LanguageAwareString(languageService, DisplayStrings.ACTION);
		action = new SimpleObjectProperty<ActionPersistenceObject>(option.action());
		valid = new SimpleBooleanProperty(option.action() != null && option.triggers().size() > 0);
		
		addTypes();
		selectType();
		
		try {
			addTriggerKey = new TextTriggerPersistenceObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		bind();
	}
	
	private void addTypes() {
		types.list().add(APPEND_ACTION);
		types.list().add(COMPLETION_ACTION);
		types.list().add(EXECUTION_ACTION);
		types.list().add(MOD_PLAYER_ACTION);
		types.list().add(SAVE_ACTION);
		types.list().add(SCRIPT_ACTION);
		types.list().add(FINISH_ACTION);
	}
	
	private void selectType() {
		if (option.action() == null) {
			LogRunner.logger().info("Not setting type since there is no action yet.");
			return;
		}
		
		if (option.action() instanceof AppendTextActionPersistence) {
			LogRunner.logger().info("Setting type to append.");
			types.selected().set(APPEND_ACTION);
		} else if (option.action() instanceof CompletionActionPersistence) {
			LogRunner.logger().info("Setting type to completion.");
			types.selected().set(COMPLETION_ACTION);
		} else if (option.action() instanceof ExecutionActionPersistence) {
			LogRunner.logger().info("Setting type to execution.");
			types.selected().set(EXECUTION_ACTION);
		} else if (option.action() instanceof ModifyPlayerActionPersistence) {
			LogRunner.logger().info("Setting type to player mod.");
			types.selected().set(MOD_PLAYER_ACTION);
		} else if (option.action() instanceof SaveActionPersistenceObject) {
			LogRunner.logger().info("Setting type to save.");
			types.selected().set(SAVE_ACTION);
		} else if (option.action() instanceof ScriptedActionPersistenceObject) {
			LogRunner.logger().info("Setting type to script.");
			types.selected().set(SCRIPT_ACTION);
		} else if (option.action() instanceof FinishActionPersistenceObject) {
			LogRunner.logger().info("Setting type to finish.");
			types.selected().set(FINISH_ACTION);
		}
	}
	
	private void bind() {
		for (TriggerPersistenceObject trig : option.triggers()) {
			LogRunner.logger().info(String.format("Adding trigger with type %s", trig.type()));
			triggers.add(trig);
		}
		
		action.addListener((v, o, n) -> {
			valid.set(n != null && option.triggers().size() > 0);
			option.action(n);
		});
		
		triggers.addListener((Change<? extends TriggerPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends TriggerPersistenceObject> rTrigger = c.getRemoved();
			List<? extends TriggerPersistenceObject> aTrigger = c.getList();
			
			for (TriggerPersistenceObject model : rTrigger) {
				if (aTrigger.contains(model)) {
					continue;
				}
				
				option.removeTrigger(model);
			}
		});
	}
	
	/**
	 * 
	 * @return If library actions are allowed on this option.
	 */
	public boolean allowLibraryAdd() {
		return libraryService != null;
	}
	
	/**
	 * 
	 * @return The triggers associated with this option.
	 */
	public ObservableList<TriggerPersistenceObject> triggers() {
		return triggers;
	}
	
	/**
	 * 
	 * @return The key to use as the add button for triggers.
	 */
	public TriggerPersistenceObject addTriggerKey() {
		return addTriggerKey;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit triggers.
	 */
	public Callback<TriggerPersistenceObject> getEditTriggerAction() {
		return (trig) -> {
			LogRunner.logger().info(String.format("Editing trigger with type %s", trig.type()));
			TriggerModel model = new TriggerModel(trig, languageService);
			
			Dialog dialog = dialogProvider.create(createView(model));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog);
		};
	}
	
	/**
	 * Adds a trigger to this option.
	 */
	public void addTrigger() {
		TriggerModel model = new TriggerModel(languageService);
		
		Dialog dialog = dialogProvider.create(createView(model));
		dialog.isValid().bind(model.valid());
		dialog.setOnComplete(() -> {
			TriggerPersistenceObject trig = model.persistenceObject().get();
			LogRunner.logger().info(String.format("Adding trigger with type %s", trig.type()));
			option.addTrigger(trig);
			triggers.add(trig);
			valid.set(option.action() != null && option.triggers().size() > 0);
		});
			
		dialogService.displayModal(dialog);
	}
	
	/**
	 * Adds trigger to this option from a library item.
	 */
	public void addTriggerFromLibrary() {
		TriggerFinderModel finder = new TriggerFinderModel(libraryService, languageService, dialogService, factory);
		Dialog dialog = dialogProvider.create(new TriggerFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			option.addTrigger(finder.foundValue());
			LogRunner.logger().info(String.format("Adding trigger with type %s from library", finder.foundValue().type()));
			triggers.add(finder.foundValue());
			valid.set(option.action() != null && option.triggers().size() > 0);
		});
		
		dialogService.displayModal(dialog);
	}
	
	private TriggerView createView(TriggerModel model) {
		if (players == null) {
			return factory.create(model);
		}
		
		return factory.create(model, players);
	}
	
	/**
	 * 
	 * @return The players available to this option.
	 */
	public List<PlayerPersistenceObject> players() {
		return players;
	}
	
	/**
	 * 
	 * @param callback A @see Callback to run after adding an action from a library.
	 */
	public void addActionFromLibrary(Callback<ActionPersistenceObject> callback) {
		ActionFinderModel finder = new ActionFinderModel(libraryService, languageService, dialogService, actionViewFactory);
		Dialog dialog = dialogProvider.create(new ActionFinder(finder, styleService, urlProvider));
		dialog.setOnComplete(() -> {
			LogRunner.logger().info(String.format("Adding action with type %s from library", finder.foundValue().type()));
			callback.execute(finder.foundValue());
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public OptionPersistenceObject persistenceObject() {
		return option;
	}
	
	/**
	 * 
	 * @return The associated action.
	 */
	public SimpleObjectProperty<ActionPersistenceObject> action() {
		return action;
	}
	
	/**
	 * 
	 * @return The type of action.
	 */
	public SelectionAwareObservableList<String> types() {
		return types;
	}
	
	/**
	 * 
	 * @return Display string for trigger.
	 */
	public SimpleStringProperty triggerText() {
		return triggerText;
	}
	
	/**
	 * 
	 * @return Display string for action.
	 */
	public SimpleStringProperty actionText() {
		return actionText;
	}
	
	/**
	 * 
	 * @return If the option is currently valid
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return The append action.
	 */
	public String appendAction() {
		return APPEND_ACTION;
	}
	
	/**
	 * 
	 * @return The completion action.
	 */
	public String completionAction() {
		return COMPLETION_ACTION;
	}
	
	/**
	 * 
	 * @return The execution action.
	 */
	public String executionAction() {
		return EXECUTION_ACTION;
	}
	
	/**
	 * 
	 * @return The save action.
	 */
	public String saveAction() {
		return SAVE_ACTION;
	}
	
	/**
	 * 
	 * @return The mod player action.
	 */
	public String playerAction() {
		return MOD_PLAYER_ACTION;
	}
	
	/**
	 * 
	 * @return The script action.
	 */
	public String scriptAction() {
		return SCRIPT_ACTION;
	}
	
	/**
	 * 
	 * @return The finish action.
	 */
	public String finishAction() {
		return FINISH_ACTION;
	}
}
