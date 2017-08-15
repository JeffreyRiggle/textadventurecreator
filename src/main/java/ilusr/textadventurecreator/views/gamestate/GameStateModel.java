package ilusr.textadventurecreator.views.gamestate;

import java.util.ArrayList;
import java.util.List;

import ilusr.core.interfaces.Callback;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.OptionFinder;
import ilusr.textadventurecreator.search.OptionFinderModel;
import ilusr.textadventurecreator.search.TimerFinder;
import ilusr.textadventurecreator.search.TimerFinderModel;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.AppendTextModel;
import ilusr.textadventurecreator.views.action.AppendTextViewProvider;
import ilusr.textadventurecreator.views.action.CompletionActionModel;
import ilusr.textadventurecreator.views.action.CompletionActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutableActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutionActionModel;
import ilusr.textadventurecreator.views.action.FinishActionViewProvider;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.action.PlayerModificationActionViewProvider;
import ilusr.textadventurecreator.views.action.SaveActionModel;
import ilusr.textadventurecreator.views.action.SaveActionViewProvider;
import ilusr.textadventurecreator.views.action.ScriptedActionModel;
import ilusr.textadventurecreator.views.action.ScriptedActionViewProvider;
import ilusr.textadventurecreator.views.macro.MacroBuilderView;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.stage.Window;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;
import textadventurelib.persistence.TimerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateModel {

	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final MacroBuilderViewFactory macroFactory;
	private final MediaFinder mediaFinder;
	private final TriggerViewFactory triggerFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final ActionViewFactory actionViewFactory;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private GameStatePersistenceObject gameState;
	private SimpleStringProperty gameStateId;
	private SimpleStringProperty textLog;
	private SimpleIntegerProperty textIndex;
	private SelectionAwareObservableList<String> layouts;
	private OptionPersistenceObject addOptionKey;
	private ObservableList<OptionPersistenceObject> options;
	private CompletionTimerPersistenceObject addTimerKey;
	private ObservableList<CompletionTimerPersistenceObject> timers;
	private SimpleStringProperty contentLocation;
	private List<PlayerPersistenceObject> players;
	private List<LayoutPersistenceObject> gameLayouts;
	private LanguageAwareString idText;
	private LanguageAwareString textLogText;
	private LanguageAwareString macroText;
	private LanguageAwareString layoutText;
	private LanguageAwareString contentText;
	private LanguageAwareString browseText;
	private LanguageAwareString optionText;
	private LanguageAwareString timerText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param gameState The @see GameStatePersistenceObject to bind to.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param macroFactory A @see MacroBuilderViewFactory to create macro views.
	 * @param mediaFinder A @see MediaFinder to find media.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mods.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateModel(GameStatePersistenceObject gameState,
						  IDialogService dialogService, 
						  MacroBuilderViewFactory macroFactory, 
						  MediaFinder mediaFinder,
						  TriggerViewFactory triggerFactory,
						  PlayerModProviderFactory playerModFactory,
						  ILanguageService languageService,
						  ActionViewFactory actionViewFactory,
						  IDialogProvider dialogProvider,
						  IStyleContainerService styleService,
						  InternalURLProvider urlProvider) {
		this(gameState, dialogService, macroFactory, mediaFinder, triggerFactory, playerModFactory,
				null, new ArrayList<LayoutPersistenceObject>(), languageService, actionViewFactory, dialogProvider,
				styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param gameState The @see GameStatePersistenceObject to bind to.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param macroFactory A @see MacroBuilderViewFactory to create macro views.
	 * @param mediaFinder A @see MediaFinder to find media.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mods.
	 * @param players A list of players to use.
	 * @param gameLayouts A list of layouts to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateModel(GameStatePersistenceObject gameState,
			  IDialogService dialogService, 
			  MacroBuilderViewFactory macroFactory, 
			  MediaFinder mediaFinder,
			  TriggerViewFactory triggerFactory,
			  PlayerModProviderFactory playerModFactory,
			  List<PlayerPersistenceObject> players,
			  List<LayoutPersistenceObject> gameLayouts,
			  ILanguageService languageService,
			  ActionViewFactory actionViewFactory,
			  IDialogProvider dialogProvider,
			  IStyleContainerService styleService,
			  InternalURLProvider urlProvider) {
		this(gameState, dialogService, null, macroFactory, mediaFinder, triggerFactory, playerModFactory,
				players, gameLayouts, languageService, actionViewFactory, dialogProvider,
				styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param gameState The @see GameStatePersistenceObject to bind to.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param macroFactory A @see MacroBuilderViewFactory to create macro views.
	 * @param mediaFinder A @see MediaFinder to find media.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mods.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateModel(GameStatePersistenceObject gameState,
			  IDialogService dialogService,
			  LibraryService libraryService,
			  MacroBuilderViewFactory macroFactory, 
			  MediaFinder mediaFinder,
			  TriggerViewFactory triggerFactory,
			  PlayerModProviderFactory playerModFactory,
			  TextAdventureProvider provider,
			  ILanguageService languageService,
			  ActionViewFactory actionViewFactory,
			  IDialogProvider dialogProvider,
			  IStyleContainerService styleService,
			  InternalURLProvider urlProvider) {
		this(gameState, dialogService, libraryService, macroFactory, mediaFinder, triggerFactory, playerModFactory,
				provider.getTextAdventureProject().getTextAdventure().players(),
				provider.getTextAdventureProject().getTextAdventure().getLayouts(), languageService, actionViewFactory,
				dialogProvider, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param gameState The @see GameStatePersistenceObject to bind to.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param macroFactory A @see MacroBuilderViewFactory to create macro views.
	 * @param mediaFinder A @see MediaFinder to find media.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mods.
	 * @param players A list of players to use.
	 * @param gameLayouts A list of layouts to use.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateModel(GameStatePersistenceObject gameState,
			  IDialogService dialogService,
			  LibraryService libraryService,
			  MacroBuilderViewFactory macroFactory, 
			  MediaFinder mediaFinder,
			  TriggerViewFactory triggerFactory,
			  PlayerModProviderFactory playerModFactory,
			  List<PlayerPersistenceObject> players,
			  List<LayoutPersistenceObject> gameLayouts,
			  ILanguageService languageService,
			  ActionViewFactory actionViewFactory,
			  IDialogProvider dialogProvider,
			  IStyleContainerService styleService,
			  InternalURLProvider urlProvider) {
		this.gameState = gameState;
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.macroFactory = macroFactory;
		this.mediaFinder = mediaFinder;
		this.triggerFactory = triggerFactory;
		this.playerModFactory = playerModFactory;
		this.players = players;
		this.gameLayouts = gameLayouts;
		this.languageService = languageService;
		this.actionViewFactory = actionViewFactory;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		gameStateId = new SimpleStringProperty(gameState.stateId());
		textLog = new SimpleStringProperty(gameState.textLog());
		textIndex = new SimpleIntegerProperty();
		layouts = new SelectionAwareObservableList<String>();
		options = FXCollections.observableArrayList();
		timers = FXCollections.observableArrayList();
		contentLocation = new SimpleStringProperty();
		idText = new LanguageAwareString(languageService, DisplayStrings.STATE_ID);
		textLogText = new LanguageAwareString(languageService, DisplayStrings.TEXT_LOG);
		macroText = new LanguageAwareString(languageService, DisplayStrings.MACRO);
		layoutText = new LanguageAwareString(languageService, DisplayStrings.LAYOUT);
		contentText = new LanguageAwareString(languageService, DisplayStrings.CONTENT);
		browseText = new LanguageAwareString(languageService, DisplayStrings.BROWSE);
		optionText = new LanguageAwareString(languageService, DisplayStrings.OPTIONS);
		timerText = new LanguageAwareString(languageService, DisplayStrings.TIMERS);
		valid = new SimpleBooleanProperty(gameState.stateId() != null && !gameState.stateId().isEmpty());
		
		initialize();
		setupBindings();
	}
	
	private void initialize() {
		for (OptionPersistenceObject option : gameState.options()) {
			LogRunner.logger().info(String.format("Adding Option with action type %s.", option.action().type()));
			options.add(option);
		}
		
		for (TimerPersistenceObject timer : gameState.timers()) {
			if (!(timer instanceof CompletionTimerPersistenceObject)) {
				continue;
			}
			
			LogRunner.logger().info("Adding Timer.");
			timers.add((CompletionTimerPersistenceObject)timer);
		}
		
		if (gameState.layout() != null) {
			LogRunner.logger().info(String.format("Setting content location to %s.", gameState.layout().getLayoutContent()));
			contentLocation.set(gameState.layout().getLayoutContent());
		}
		
		LayoutType type = gameState.layout().getLayoutType();
		if (type != null && type != LayoutType.Custom) {
			LogRunner.logger().info(String.format("Setting layout type to %s.", gameState.layout().getLayoutType().toString()));
			layouts.selected().set(gameState.layout().getLayoutType().toString());
		} else if (type != null && type == LayoutType.Custom) {
			LogRunner.logger().info(String.format("Setting layout type to %s.", gameState.layout().getLayoutId()));
			layouts.selected().set(gameState.layout().getLayoutId());
		}
	}
	
	private void setupBindings() {
		gameStateId.addListener((v, o, n) -> {
			valid.set(n != null && !n.isEmpty());
			gameState.stateId(n);
		});
		
		textLog.addListener((v, o, n) -> {
			gameState.textLog(n);
		});
		
		for (LayoutType t : LayoutType.values()) {
			layouts.list().add(t.toString());
		}
		
		for (LayoutPersistenceObject layout : gameLayouts) {
			layouts.list().add(layout.id());
		}
		
		layouts.selected().addListener((v, o, n) -> {
			if (n == null || n.isEmpty()) {
				return;
			}
			
			try {
				gameState.layout().setLayoutType(LayoutType.valueOf(n));
			} catch (Exception e) {
				gameState.layout().setLayoutType(LayoutType.Custom);
				gameState.layout().setLayoutId(n);
			}
		});
		
		contentLocation.addListener((v, o, n) -> {
			gameState.layout().setLayoutContent(n);
		});
		
		options.addListener((Change<? extends OptionPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends OptionPersistenceObject> rOption = c.getRemoved();
			List<? extends OptionPersistenceObject> aOption = c.getList();
			
			for (OptionPersistenceObject model : rOption) {
				if (aOption.contains(model)) {
					continue;
				}
				
				gameState.removeOption(model);
			}
		});
		
		try {
			addOptionKey = new OptionPersistenceObject();
			addTimerKey = new CompletionTimerPersistenceObject();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	/**
	 * 
	 * @return The id of the game state.
	 */
	public SimpleStringProperty stateId() {
		return gameStateId;
	}
	
	/**
	 * 
	 * @return If the current game state is valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return The text log of the game state.
	 */
	public SimpleStringProperty textLog() {
		return textLog;
	}
	
	/**
	 * 
	 * @return The current position of the cursor in the text log.
	 */
	public SimpleIntegerProperty textIndex() {
		return textIndex;
	}
	
	/**
	 * 
	 * @return The associated game state object.
	 */
	public GameStatePersistenceObject persistableGameState() {
		return gameState;
	}
	
	/**
	 * 
	 * @return The layout type to use.
	 */
	public SelectionAwareObservableList<String> layouts() {
		return layouts;
	}
	
	/**
	 * 
	 * @return The content to show in the game state.
	 */
	public SimpleStringProperty contentLocation() {
		return contentLocation;
	}
	
	/**
	 * 
	 * @return A key to use as the add button for the options list.
	 */
	public OptionPersistenceObject addOptionKey() {
		return addOptionKey;
	}
	
	/**
	 * 
	 * @return Display string for id.
	 */
	public SimpleStringProperty idText() {
		return idText;
	}
	
	/**
	 * 
	 * @return Display string for text log.
	 */
	public SimpleStringProperty textLogText() {
		return textLogText;
	}
	
	/**
	 * 
	 * @return Display string for macro.
	 */
	public SimpleStringProperty macroText() {
		return macroText;
	}
	
	/**
	 * 
	 * @return Display string for layout.
	 */
	public SimpleStringProperty layoutText() {
		return layoutText;
	}
	
	/**
	 * 
	 * @return Display string for content.
	 */
	public SimpleStringProperty contentText() {
		return contentText;
	}
	
	/**
	 * 
	 * @return Display string for browse.
	 */
	public SimpleStringProperty browseText() {
		return browseText;
	}
	
	/**
	 * 
	 * @return Display string for option.
	 */
	public SimpleStringProperty optionText() {
		return optionText;
	}
	
	/**
	 * 
	 * @return Display string for timer.
	 */
	public SimpleStringProperty timerText() {
		return timerText;
	}
	
	/**
	 * 
	 * @return A @see Callback to run when editing an option.
	 */
	public Callback<OptionPersistenceObject> getEditOptionAction() {
		return (opt) -> {
			LogRunner.logger().info(String.format("Editing option with action type %s.", opt.action().type()));
			OptionModel model = new OptionModel(dialogService, libraryService, opt, triggerFactory, players,
					languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
			Dialog dialog = dialogProvider.create(buildEditOptionView(model));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.OPTION));
		};
	}
	
	private OptionView buildEditOptionView(OptionModel model) {
		// Then create the related provider and use that instead of the IoC one.
		AppendTextViewProvider appendProvider = ServiceManager.getInstance().get("AppendTextViewProvider");
		CompletionActionViewProvider completeProvider = ServiceManager.getInstance().get("CompletionActionViewProvider");
		ExecutableActionViewProvider exeProvider = ServiceManager.getInstance().get("ExecutableActionViewProvider");
		SaveActionViewProvider saveProvider = ServiceManager.getInstance().get("SaveActionViewProvider");
		PlayerModificationActionViewProvider playerProvider = ServiceManager.getInstance().get("PlayerModificationActionViewProvider");
		ScriptedActionViewProvider scriptProvider = ServiceManager.getInstance().get("ScriptedActionViewProvider");
		FinishActionViewProvider finishProvider = ServiceManager.getInstance().get("FinishActionViewProvider");
		
		if (model.persistenceObject().action() != null) {
			ActionPersistenceObject action = model.persistenceObject().action();
			if (action instanceof AppendTextActionPersistence) {
				appendProvider = new AppendTextViewProvider(new AppendTextModel((AppendTextActionPersistence)action, languageService));
			} else if (action instanceof CompletionActionPersistence) {
				completeProvider = new CompletionActionViewProvider(new CompletionActionModel((CompletionActionPersistence)action, languageService));
			} else if (action instanceof ExecutionActionPersistence) {
				exeProvider = new ExecutableActionViewProvider(new ExecutionActionModel((ExecutionActionPersistence)action, languageService));
			} else if (action instanceof ModifyPlayerActionPersistence) {
				if (players != null) {
					playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action, players);
				} else {
					playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action);
				}
			} else if (action instanceof SaveActionPersistenceObject) {
				saveProvider = new SaveActionViewProvider(new SaveActionModel((SaveActionPersistenceObject)action, languageService));
			} else if (action instanceof ScriptedActionPersistenceObject) {
				scriptProvider = new ScriptedActionViewProvider(new ScriptedActionModel((ScriptedActionPersistenceObject)action, languageService));
			}
		}
		
		return new OptionView(model, appendProvider, completeProvider, exeProvider, saveProvider, playerProvider, scriptProvider,
				finishProvider, playerModFactory, languageService, styleService, urlProvider);
	}
	
	/**
	 * Creates an option and adds it to the game state.
	 */
	public void addOption() {
		try {
			OptionPersistenceObject opt = new OptionPersistenceObject();
			OptionModel model = new OptionModel(dialogService, libraryService, opt, triggerFactory, players,
					languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
			AppendTextViewProvider appendProvider = ServiceManager.getInstance().get("AppendTextViewProvider");
			CompletionActionViewProvider completeProvider = ServiceManager.getInstance().get("CompletionActionViewProvider");
			ExecutableActionViewProvider exeProvider = ServiceManager.getInstance().get("ExecutableActionViewProvider");
			SaveActionViewProvider saveProvider = ServiceManager.getInstance().get("SaveActionViewProvider");
			PlayerModificationActionViewProvider playerProvider = null;
				
			if (players == null) {
				playerProvider = playerModFactory.create(new ModifyPlayerActionPersistence());
			} else {
				playerProvider = playerModFactory.create(new ModifyPlayerActionPersistence(), players);
			}
				
			ScriptedActionViewProvider scriptProvider = ServiceManager.getInstance().get("ScriptedActionViewProvider");
			FinishActionViewProvider finishProvider = ServiceManager.getInstance().get("FinishActionViewProvider");
				
			Dialog dialog = dialogProvider.create(new OptionView(model, appendProvider, completeProvider, exeProvider, saveProvider,
					playerProvider, scriptProvider, finishProvider, playerModFactory, languageService,
					styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			dialog.setOnComplete(() -> {
				LogRunner.logger().info(String.format("Adding option with action type %s.", opt.action().type()));
				gameState.addOption(opt);
				options.add(opt);
			});
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.OPTION));
		} catch (Exception ex) {
			LogRunner.logger().severe(ex);
		}
	}
	
	/**
	 * Adds an option to this game state from a library item.
	 */
	public void addOptionFromLibrary() {
		OptionFinderModel finder = new OptionFinderModel(libraryService, languageService, dialogService,
				actionViewFactory, triggerFactory, dialogProvider, styleService, urlProvider);
		Dialog dialog = dialogProvider.create(new OptionFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			LogRunner.logger().info(String.format("Adding option with action type %s.", finder.foundValue().action().type()));
			gameState.addOption(finder.foundValue());
			options.add(finder.foundValue());
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return A key to use as the add timer button.
	 */
	public CompletionTimerPersistenceObject addTimerKey() {
		return addTimerKey;
	}
	
	/**
	 * Adds a timer to the game state.
	 */
	public void addTimer() {
		try {
			LogRunner.logger().info("Adding timer to game state.");
			CompletionTimerPersistenceObject timer = new CompletionTimerPersistenceObject();
			timers.add(timer);
			gameState.addTimer(timer);
		} catch (Exception ex) {
			LogRunner.logger().severe(ex);
		}
	}
	
	/**
	 * Adds a timer to the game state from a library item.
	 */
	public void addTimerFromLibrary() {
		TimerFinderModel finder = new TimerFinderModel(libraryService, languageService, dialogService);
		Dialog dialog = dialogProvider.create(new TimerFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			if (!(finder.foundValue() instanceof CompletionTimerPersistenceObject)) {
				return;
			}
			
			LogRunner.logger().info("Adding timer to game state from library.");
			CompletionTimerPersistenceObject timer = (CompletionTimerPersistenceObject)finder.foundValue();
			timers.add(timer);
			gameState.addTimer(timer);
		});
		
		dialogService.displayModal(dialog);
	}
	
	public ObservableList<CompletionTimerPersistenceObject> timers() {
		return timers;
	}
	
	/**
	 * 
	 * @return If this game state should have content.
	 */
	public boolean hasContent() {
		String layout = layouts.selected().get();
		if (layout == null || layout.isEmpty()) {
			return false;
		}
		
		LayoutType type = gameState.layout().getLayoutType();
		
		if (type == LayoutType.ContentOnly || type == LayoutType.TextAndContentWithButtonInput 
		    || type == LayoutType.TextAndContentWithTextInput) {
			return true;
		}
		
		if (type != LayoutType.Custom) {
			return false;
		}
		
		boolean hasContent = false;
		for (LayoutPersistenceObject pLayout : gameLayouts) {
			if (!pLayout.id().equals(layout)) {
				continue;
			}
			
			hasContent = pLayout.hasContentArea();
			break;
		}
		
		return hasContent;
	}
	
	/**
	 * 
	 * @return If the game state allows the use of libraries.
	 */
	public boolean allowLibraryAdd() {
		return libraryService != null;
	}
	
	/**
	 * 
	 * @return The options associated with this game state.
	 */
	public ObservableList<OptionPersistenceObject> options() {
		return options;
	}
	
	/**
	 * Creates a macro an inserts it into the text log.
	 */
	public void buildMacro() {
		final MacroBuilderView view = macroFactory.build();
		Dialog dialog = dialogProvider.create(view);
		dialog.isValid().bind(view.model().valid());
		dialog.setOnComplete(() -> {
			String log = textLog.get();
			LogRunner.logger().info(String.format("Inserting macro %s into text log.", view.model().currentMacro()));
			if (log == null || log.isEmpty()) {
				textLog.set(view.model().currentMacro());
				return;
			}
			
			StringBuilder builder = new StringBuilder();
			builder.append(log.substring(0, Math.max(textIndex.get(), 0)));
			builder.append(view.model().currentMacro());
			builder.append(log.substring(textIndex.get()));
			textLog.set(builder.toString());
		});
		
		dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.MACRO));
	}
	
	/**
	 * 
	 * @param window The window to use to display the browser.
	 */
	public void browseContent(Window window) {
		mediaFinder.setWindow(window);
		mediaFinder.getMediaLocation(contentLocation);
	}
}
