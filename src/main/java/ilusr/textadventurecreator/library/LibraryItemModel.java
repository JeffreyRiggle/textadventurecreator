package ilusr.textadventurecreator.library;

import java.util.UUID;
import java.util.logging.Level;

import ilusr.core.interfaces.Callback;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.action.ActionModel;
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
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.gamestate.GameStateView;
import ilusr.textadventurecreator.views.gamestate.OptionModel;
import ilusr.textadventurecreator.views.gamestate.OptionView;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import ilusr.textadventurecreator.views.layout.LayoutCreatorView;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.player.BodyPartModel;
import ilusr.textadventurecreator.views.player.BodyPartViewer;
import ilusr.textadventurecreator.views.player.ItemModel;
import ilusr.textadventurecreator.views.player.ItemViewer;
import ilusr.textadventurecreator.views.player.PlayerModel;
import ilusr.textadventurecreator.views.player.PlayerView;
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TimerPersistenceObject;
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
public class LibraryItemModel {

	private final IDialogService dialogService;
	private final TriggerViewFactory triggerViewFactory;
	private final ActionViewFactory actionViewFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final InternalURLProvider urlProvider;
	private final ServiceManager serviceManager;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	
	private LanguageAwareString attributeText;
	private LanguageAwareString characteristicText;
	private LanguageAwareString bodyPartText;
	private LanguageAwareString propertyText;
	private LanguageAwareString itemText;
	private LanguageAwareString playerText;
	private LanguageAwareString triggerText;
	private LanguageAwareString actionText;
	private LanguageAwareString optionText;
	private LanguageAwareString timerText;
	private LanguageAwareString gameStateText;
	private LanguageAwareString layoutText;
	private LanguageAwareString nameText;
	private LanguageAwareString authorText;
	
	private ObservableList<PlayerPersistenceObject> players;
	private PlayerPersistenceObject addPlayerKey;
	private AttributePersistenceObject addAttributeKey;
	private ObservableList<AttributePersistenceObject> attributes;
	private CharacteristicPersistenceObject addCharacteristicKey;
	private ObservableList<CharacteristicPersistenceObject> characteristics;
	private BodyPartPersistenceObject addBodyPartKey;
	private ObservableList<BodyPartPersistenceObject> bodyParts;
	private ItemPersistenceObject addItemKey;
	private ObservableList<ItemPersistenceObject> items;
	private PropertyPersistenceObject addPropertyKey;
	private ObservableList<PropertyPersistenceObject> properties;
	private TriggerPersistenceObject addTriggerKey;
	private ObservableList<TriggerPersistenceObject> triggers;
	private ActionPersistenceObject addActionKey;
	private ObservableList<ActionPersistenceObject> actions;
	private OptionPersistenceObject addOptionKey;
	private ObservableList<OptionPersistenceObject> options;
	private CompletionTimerPersistenceObject addTimerKey;
	private ObservableList<CompletionTimerPersistenceObject> timers;
	private GameStatePersistenceObject addGameStateKey;
	private ObservableList<GameStatePersistenceObject> gameStates;
	private ObservableList<LayoutPersistenceObject> layouts;
	private LayoutPersistenceObject addLayoutKey;
	private SimpleStringProperty author;
	private SimpleStringProperty name;
	private LibraryItem item;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display stages.
	 * @param item A @see LibraryItem to associate with this model.
	 * @param triggerViewFactory A @see TriggerViewFactory to create triggers.
	 * @param actionViewFactory A @see ActionViewFactory to create actions.
	 * @param playerModFactory A @see PlayerModFactory to create player mod views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 * @param serviceManager A @see ServiceManager to provide access to services.
	 * @param dialogProvider A @see IDialogProvider to provide dialogs.
	 * @param styleService service to manage styles.
	 */
	public LibraryItemModel(IDialogService dialogService, 
							LibraryItem item, 
							TriggerViewFactory triggerViewFactory,
							ActionViewFactory actionViewFactory,
							PlayerModProviderFactory playerModFactory,
							ILanguageService languageService,
							InternalURLProvider urlProvider,
							ServiceManager serviceManager,
							IDialogProvider dialogProvider,
							IStyleContainerService styleService) {
		this.dialogService = dialogService;
		this.item = item;
		this.triggerViewFactory = triggerViewFactory;
		this.actionViewFactory = actionViewFactory;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.urlProvider = urlProvider;
		this.serviceManager = serviceManager;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		
		attributes = FXCollections.observableArrayList();
		characteristics = FXCollections.observableArrayList();
		bodyParts = FXCollections.observableArrayList();
		properties = FXCollections.observableArrayList();
		items = FXCollections.observableArrayList();
		players = FXCollections.observableArrayList();
		triggers = FXCollections.observableArrayList();
		actions = FXCollections.observableArrayList();
		options = FXCollections.observableArrayList();
		timers = FXCollections.observableArrayList();
		gameStates = FXCollections.observableArrayList();
		layouts = FXCollections.observableArrayList();
		name = new SimpleStringProperty(item.getLibraryName());
		author = new SimpleStringProperty(item.getAuthor());

		attributeText = new LanguageAwareString(languageService, DisplayStrings.ATTRIBUTES);
		characteristicText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTICS);
		bodyPartText = new LanguageAwareString(languageService, DisplayStrings.BODY_PARTS);
		propertyText = new LanguageAwareString(languageService, DisplayStrings.PROPERTIES);
		itemText = new LanguageAwareString(languageService, DisplayStrings.ITEMS);
		playerText = new LanguageAwareString(languageService, DisplayStrings.PLAYERS);
		triggerText = new LanguageAwareString(languageService, DisplayStrings.TRIGGERS);
		actionText = new LanguageAwareString(languageService, DisplayStrings.ACTIONS);
		optionText = new LanguageAwareString(languageService, DisplayStrings.OPTIONS);
		timerText = new LanguageAwareString(languageService, DisplayStrings.TIMERS);
		gameStateText = new LanguageAwareString(languageService, DisplayStrings.GAME_STATES);
		nameText = new LanguageAwareString(languageService, DisplayStrings.LIBRARY_NAME);
		authorText = new LanguageAwareString(languageService, DisplayStrings.AUTHOR);
		layoutText = new LanguageAwareString(languageService, DisplayStrings.LAYOUT);
		
		setupLibrary();
		setupKeys();
		bind();
	}
	
	private void setupLibrary() {
		LogRunner.logger().log(Level.INFO, "Setting up library item");
		attributes.addAll(item.attributes());
		characteristics.addAll(item.characteristics());
		bodyParts.addAll(item.bodyParts());
		properties.addAll(item.properties());
		items.addAll(item.items());
		players.addAll(item.players());
		triggers.addAll(item.triggers());
		actions.addAll(item.actions());
		options.addAll(item.options());
		gameStates.addAll(item.gameStates());
		layouts.addAll(item.layouts());
		
		for (TimerPersistenceObject timer : item.timers()) {
			if (timer instanceof CompletionTimerPersistenceObject) {
				timers.add((CompletionTimerPersistenceObject)timer);
			}
		}
	}
	
	private void setupKeys() {
		try {
			LogRunner.logger().log(Level.INFO, "Creating library item keys");
			addAttributeKey = new AttributePersistenceObject();
			addAttributeKey.objectName(UUID.randomUUID().toString());
			
			addCharacteristicKey = new CharacteristicPersistenceObject();
			addCharacteristicKey.objectName(UUID.randomUUID().toString());
			
			addBodyPartKey = new BodyPartPersistenceObject();
			addBodyPartKey.objectName(UUID.randomUUID().toString());
			
			addPropertyKey = new PropertyPersistenceObject();
			addPropertyKey.name(UUID.randomUUID().toString());
			
			addItemKey = new ItemPersistenceObject();
			addItemKey.name(UUID.randomUUID().toString());
			
			addPlayerKey = new PlayerPersistenceObject();
			addPlayerKey.playerName(UUID.randomUUID().toString());
			
			addTriggerKey = new TextTriggerPersistenceObject();
			addActionKey = new AppendTextActionPersistence();
			addOptionKey = new OptionPersistenceObject();
			addTimerKey = new CompletionTimerPersistenceObject();
			addGameStateKey = new GameStatePersistenceObject(UUID.randomUUID().toString());
			addLayoutKey = new LayoutPersistenceObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bind() {
		LogRunner.logger().log(Level.INFO, "Binding library item");
		name.addListener((v, o, n) -> {
			item.setLibraryName(n);
		});
		
		author.addListener((v, o, n) -> {
			item.setAuthor(n);
		});
	}
	
	/**
	 * 
	 * @return The key to use for the add attribute item.
	 */
	public AttributePersistenceObject addAttributeKey() {
		return addAttributeKey;
	}
	
	/**
	 * 
	 * @return A list of @see AttributePersistenceObject associated with this library item.
	 */
	public ObservableList<AttributePersistenceObject> attributes() {
		return attributes;
	}
	
	private void addAttribute() {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Adding new attribute to %s", name.get()));
			AttributePersistenceObject attribute = new AttributePersistenceObject();
			item.attributes().add(attribute);
			attributes.add(attribute);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call on add attribute.
	 */
	public EventHandler<ActionEvent> getAddAttributeAction() {
		return e -> { addAttribute(); };
	}
	
	/**
	 * 
	 * @return The key to use for the add characteristic item.
	 */
	public CharacteristicPersistenceObject addCharacteristicKey() {
		return addCharacteristicKey;
	}
	
	/**
	 * 
	 * @return A list of @see CharacteristicPersistenceObject associated with this library item.
	 */
	public ObservableList<CharacteristicPersistenceObject> characteristics() {
		return characteristics;
	}
	
	private void addCharacteristic() {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Adding new characteristic to %s", name.get()));
			CharacteristicPersistenceObject character = new CharacteristicPersistenceObject();
			item.characteristics().add(character);
			characteristics.add(character);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call on add characteristic.
	 */
	public EventHandler<ActionEvent> getAddCharacteristicAction() {
		return e -> { addCharacteristic(); };
	}
	
	/**
	 * 
	 * @return The key to use for the add body part item.
	 */
	public BodyPartPersistenceObject addBodyPartKey() {
		return addBodyPartKey;
	}
	
	/**
	 * 
	 * @return A list of @see BodyPartPersistenceObject associated with this library item.
	 */
	public ObservableList<BodyPartPersistenceObject> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * 
	 * @return A @see Callback to call on edit body part.
	 */
	public Callback<BodyPartPersistenceObject> getEditBodyPartAction() {
		return bodyPart -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing body part %s for %s", bodyPart.objectName(), name.get()));
			BodyPartModel model = new BodyPartModel(bodyPart, null, dialogService, languageService, dialogProvider, styleService, urlProvider);
			Dialog dialog = dialogProvider.create(new BodyPartViewer(model, languageService, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.BODY_PART));
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call on add body part.
	 */
	public EventHandler<ActionEvent> getAddBodyPartAction() {
		return e -> { addBodyPart(); };
	}
	
	private void addBodyPart() {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Creating new body part for %s", name.get()));
			BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
			BodyPartModel model = new BodyPartModel(bodyPart, null, dialogService, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new BodyPartViewer(model, languageService, styleService, urlProvider), () -> {
													  LogRunner.logger().log(Level.INFO, String.format("Adding body part %s to %s", bodyPart.objectName(), name.get()));
													  item.bodyParts().add(bodyPart);
													  bodyParts.add(bodyPart);
												  });
			dialog.isValid().bind(model.valid());
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.BODY_PART));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The key to use for the add property item.
	 */
	public PropertyPersistenceObject addPropertyKey() {
		return addPropertyKey;
	}
	
	/**
	 * 
	 * @return A list of @see PropertyPersistenceObject associated with this library item.
	 */
	public ObservableList<PropertyPersistenceObject> properties() {
		return properties;
	}
	
	private void addProperty() {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Adding property to %s", name.get()));
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			item.properties().add(prop);
			properties.add(prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The @see ActionEvent to run to add a property to this library item.
	 */
	public EventHandler<ActionEvent> getAddPropertyAction() {
		return e -> { addProperty(); };
	}
	
	/**
	 * 
	 * @return The key to use for the add item.
	 */
	public ItemPersistenceObject addItemKey() {
		return addItemKey;
	}
	
	/**
	 * 
	 * @return A list of @see ItemPersistenceObject associated with this library item.
	 */
	public ObservableList<ItemPersistenceObject> items() {
		return items;
	}
	
	/**
	 * 
	 * @return A @see CallBack to run to edit a @see ItemPersistenceObject associated with this library item.
	 */
	public Callback<ItemPersistenceObject> getEditItemAction() {
		return item -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing item %s for %s", item.itemName(), name.get()));
			ItemModel model = new ItemModel(item, null, dialogService, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new ItemViewer(model, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to run to add an item to this library item.
	 */
	public EventHandler<ActionEvent> getAddItemAction() {
		return e -> { addItem(); };
	}
	
	private void addItem() {
		try {
			ItemPersistenceObject item = new ItemPersistenceObject();
			ItemModel model = new ItemModel(item, null, dialogService, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new ItemViewer(model, styleService, urlProvider), () -> {
				LogRunner.logger().log(Level.INFO, String.format("Adding item %s to %s", item.itemName(), name.get()));
				this.item.items().add(item);
				items.add(item);
			});
			
			dialog.isValid().bind(model.valid());
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The key to use for the add player item.
	 */
	public PlayerPersistenceObject addPlayerKey() {
		return addPlayerKey;
	}
	
	/**
	 * 
	 * @return A list of @see PlayerPersistenceObject associated with this library item.
	 */
	public ObservableList<PlayerPersistenceObject> players() {
		return players;
	}
	
	/**
	 * 
	 * @return A @see Callback to use to edit a @see PlayerPersistenceObject associated with this library item.
	 */
	public Callback<PlayerPersistenceObject> getEditPlayerAction() {
		return (player) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing player %s for %s", player.playerName(), name.get()));
			PlayerModel model = new PlayerModel(dialogService, null, player, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new PlayerView(model, languageService, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.PLAYER));
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a player to this library item.
	 */
	public EventHandler<ActionEvent> getAddPlayerAction() {
		return (e) -> { addPlayer(); };
	}
	
	private void addPlayer() {
		try {
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			PlayerModel model = new PlayerModel(dialogService, null, player, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new PlayerView(model, languageService, styleService, urlProvider), () -> {
				LogRunner.logger().log(Level.INFO, String.format("Adding player %s to %s", player.playerName(), name.get()));
				item.players().add(player);
				players.add(player);
			});
			
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.PLAYER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The key to use for the add trigger item.
	 */
	public TriggerPersistenceObject addTriggerKey() {
		return addTriggerKey;
	}
	
	/**
	 * 
	 * @return A list of @see TriggerPersistenceObject associated with this library item.
	 */
	public ObservableList<TriggerPersistenceObject> triggers() {
		return triggers;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit a @see TriggerPersistenceObject associated with this library item.
	 */
	public Callback<TriggerPersistenceObject> getEditTriggerAction() {
		return (trigger) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing trigger %s for %s", trigger.type(), name.get()));
			TriggerModel model = new TriggerModel(trigger, languageService);
			
			Dialog dialog = dialogProvider.create(triggerViewFactory.create(model, item.players()));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.TRIGGER));
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a trigger to this library item.
	 */
	public EventHandler<ActionEvent> getAddTriggerAction() {
		return (e) -> { addTrigger(); };
	}
	
	private void addTrigger() {
		TriggerModel model = new TriggerModel(languageService);
		
		Dialog dialog = dialogProvider.create(triggerViewFactory.create(model, item.players()), () -> {
			TriggerPersistenceObject trigger = model.persistenceObject().get();
			if (trigger == null) {
				LogRunner.logger().log(Level.INFO, "Not adding trigger since trigger is null");
			}
			
			LogRunner.logger().log(Level.INFO, String.format("Adding trigger %s to %s", trigger.type(), name.get()));
			item.triggers().add(model.persistenceObject().get());
			triggers.add(model.persistenceObject().get());
		});
		
		dialog.isValid().bind(model.valid());
		
		dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.TRIGGER));
	}
	
	/**
	 * 
	 * @return The key to use for the add action item.
	 */
	public ActionPersistenceObject addActionKey() {
		return addActionKey;
	}
	
	/**
	 * 
	 * @return A list of @see ActionPersistenceObject associated with this library item.
	 */
	public ObservableList<ActionPersistenceObject> actions() {
		return actions;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit a @see ActionPersistenceObject associated with this library item.
	 */
	public Callback<ActionPersistenceObject> getEditActionAction() {
		return (action) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing action for %s", name.get()));
			ActionModel model = new ActionModel(action, languageService);
			
			Dialog dialog = dialogProvider.create(actionViewFactory.create(model, item.players()), () -> {
				LogRunner.logger().log(Level.INFO, String.format("Finished editing action for %s", name.get()));
				item.actions().add(model.persistenceObject().get());
				actions.add(model.persistenceObject().get());
			});
			
			dialogService.displayModal(dialog);
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a action to this library item.
	 */
	public EventHandler<ActionEvent> getAddActionAction() {
		return (e) -> { addAction(); };
	}
	
	private void addAction() {
		ActionModel model = new ActionModel(languageService);
		
		Dialog dialog = dialogProvider.create(actionViewFactory.create(model, item.players()), () -> {
			LogRunner.logger().log(Level.INFO, String.format("Adding action to %s", name.get()));
			item.actions().add(model.persistenceObject().get());
			actions.add(model.persistenceObject().get());
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return The key to use for the add option item.
	 */
	public OptionPersistenceObject addOptionKey() {
		return addOptionKey;
	}
	
	/**
	 * 
	 * @return A list of @see OptionPersistenceObject associated with this library item.
	 */
	public ObservableList<OptionPersistenceObject> options() {
		return options;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit a @see OptionPersistenceObject associated with this library item.
	 */
	public Callback<OptionPersistenceObject> getEditOptionAction() {
		return (option) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing option for %s", name.get()));
			OptionModel model = new OptionModel(dialogService, option, triggerViewFactory, item.players(), languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(buildEditOptionView(model));
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.OPTION));
		};
	}
	
	private OptionView buildEditOptionView(OptionModel model) {
		// Then create the related provider and use that instead of the IoC one.
		AppendTextViewProvider appendProvider = serviceManager.get("AppendTextViewProvider");
		CompletionActionViewProvider completeProvider = serviceManager.get("CompletionActionViewProvider");
		ExecutableActionViewProvider exeProvider = serviceManager.get("ExecutableActionViewProvider");
		SaveActionViewProvider saveProvider = serviceManager.get("SaveActionViewProvider");
		PlayerModificationActionViewProvider playerProvider = null;
		try {
			playerProvider = playerModFactory.create(new ModifyPlayerActionPersistence(), item.players());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ScriptedActionViewProvider scriptProvider = serviceManager.get("ScriptedActionViewProvider");
		FinishActionViewProvider finishProvider = serviceManager.get("FinishActionViewProvider");
		
		if (model.persistenceObject().action() != null) {
			ActionPersistenceObject action = model.persistenceObject().action();
			if (action instanceof AppendTextActionPersistence) {
				appendProvider = new AppendTextViewProvider(new AppendTextModel((AppendTextActionPersistence)action, languageService));
			} else if (action instanceof CompletionActionPersistence) {
				completeProvider = new CompletionActionViewProvider(new CompletionActionModel((CompletionActionPersistence)action, languageService));
			} else if (action instanceof ExecutionActionPersistence) {
				exeProvider = new ExecutableActionViewProvider(new ExecutionActionModel((ExecutionActionPersistence)action, languageService));
			} else if (action instanceof ModifyPlayerActionPersistence) {
				playerProvider = playerModFactory.create((ModifyPlayerActionPersistence)action, item.players());
			} else if (action instanceof SaveActionPersistenceObject) {
				saveProvider = new SaveActionViewProvider(new SaveActionModel((SaveActionPersistenceObject)action, languageService));
			} else if (action instanceof ScriptedActionPersistenceObject) {
				scriptProvider = new ScriptedActionViewProvider(new ScriptedActionModel((ScriptedActionPersistenceObject)action, languageService));
			}
		}
		
		return new OptionView(model, appendProvider, completeProvider, exeProvider,
				saveProvider, playerProvider, scriptProvider, finishProvider, playerModFactory, languageService,
				styleService, urlProvider);
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a option to this library item.
	 */
	public EventHandler<ActionEvent> getAddOptionAction() {
		return (e) -> { addOption(); };
	}
	
	private void addOption() {
		try {
			OptionPersistenceObject opt = new OptionPersistenceObject();
			OptionModel model = new OptionModel(dialogService, opt, triggerViewFactory, item.players(), languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
			AppendTextViewProvider appendProvider = serviceManager.get("AppendTextViewProvider");
			CompletionActionViewProvider completeProvider = serviceManager.get("CompletionActionViewProvider");
			ExecutableActionViewProvider exeProvider = serviceManager.get("ExecutableActionViewProvider");
			SaveActionViewProvider saveProvider = serviceManager.get("SaveActionViewProvider");
			PlayerModificationActionViewProvider playerProvider = playerModFactory.create(new ModifyPlayerActionPersistence(), item.players());
			ScriptedActionViewProvider scriptProvider = serviceManager.get("ScriptedActionViewProvider");
			FinishActionViewProvider finishProvider = serviceManager.get("FinishActionViewProvider");
			
			Dialog dialog = dialogProvider.create(new OptionView(model, appendProvider, completeProvider, exeProvider, saveProvider,
					playerProvider, scriptProvider, finishProvider, playerModFactory, languageService, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			dialog.setOnComplete(() -> {
				LogRunner.logger().log(Level.INFO, String.format("Adding option to %s", name.get()));
				item.options().add(opt);
				options.add(opt);
			});
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.OPTION));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The key to use for the add timer item.
	 */
	public CompletionTimerPersistenceObject addTimerKey() {
		return addTimerKey;
	}
	
	/**
	 * 
	 * @return A list of @see CompletionTimerPersistenceObject associated with this library item.
	 */
	public ObservableList<CompletionTimerPersistenceObject> timers() {
		return timers;
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a timer to this library item.
	 */
	public EventHandler<ActionEvent> getAddTimerAction() {
		return (e) -> {
			try {
				CompletionTimerPersistenceObject timer = new CompletionTimerPersistenceObject();
				LogRunner.logger().log(Level.INFO, String.format("Adding timer to %s", name.get()));
				
				item.timers().add(timer);
				timers.add(timer);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};
	}
	
	/**
	 * 
	 * @return The key to use for the add layout item.
	 */
	public LayoutPersistenceObject addLayoutKey() {
		return addLayoutKey;
	}
	
	/**
	 * 
	 * @return A list of @see LayoutPersistenceObject associated with this library item.
	 */
	public ObservableList<LayoutPersistenceObject> layouts() {
		return layouts;
	}
	
	public Callback<LayoutPersistenceObject> getEditLayoutAction() {
		return (layout) -> {
			LayoutCreatorModel model = new LayoutCreatorModel(layout, languageService, dialogService, urlProvider, new SelectionManager());
				
			Dialog dialog = dialogProvider.create(new LayoutCreatorView(model, styleService, urlProvider), () -> {
				LogRunner.logger().log(Level.INFO, String.format("Editing layout %s for %s", layout.id(), name.get()));
				item.layouts().add(model.persistableLayout());
				layouts.add(model.persistableLayout());
			});
				
			dialogService.displayModal(dialog);
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to call to add a layout to this library item.
	 */
	public EventHandler<ActionEvent> getAddLayoutAction() {
		return e -> { addLayout(); };
	}
	
	private void addLayout() {
		try {
			LayoutCreatorModel model = new LayoutCreatorModel(new LayoutPersistenceObject(), languageService, dialogService, urlProvider, new SelectionManager());
			
			Dialog dialog = dialogProvider.create(new LayoutCreatorView(model, styleService, urlProvider), () -> {
				LogRunner.logger().log(Level.INFO, String.format("Adding layout %s to %s", model.persistableLayout().id(), name.get()));
				item.layouts().add(model.persistableLayout());
				layouts.add(model.persistableLayout());
			});
			
			dialogService.displayModal(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The key to use for the add game state item.
	 */
	public GameStatePersistenceObject addGameStateKey() {
		return addGameStateKey;
	}
	
	/**
	 * 
	 * @return A list of @see GameStatePersistenceObject associated with this library item.
	 */
	public ObservableList<GameStatePersistenceObject> gameStates() {
		return gameStates;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit a @see GameStatePersistenceObject associated with this library item.
	 */
	public Callback<GameStatePersistenceObject> getEditGameStateAction() {
		return (gameState) -> {
			try {
				LogRunner.logger().log(Level.INFO, String.format("Editing game state %s for %s", gameState.stateId(), name.get()));
				MacroBuilderViewFactory macroBuilder = serviceManager.<MacroBuilderViewFactory>get("MacroBuilderViewFactory");
				MediaFinder finder = serviceManager.<MediaFinder>get("MediaFinder");
				
				GameStateModel model = new GameStateModel(gameState, dialogService, macroBuilder, finder, triggerViewFactory,
						playerModFactory, players, layouts, languageService, actionViewFactory, dialogProvider,
						styleService, urlProvider);
				GameStateView view = new GameStateView(model, languageService, styleService, urlProvider);
				
				Dialog dialog = dialogProvider.create(view, () -> {
					LogRunner.logger().log(Level.INFO, String.format("Finished editing game state %s for %s", gameState.stateId(), name.get()));
					item.gameStates().add(model.persistableGameState());
					gameStates.add(model.persistableGameState());
				});
				
				dialog.isValid().bind(model.valid());
				
				dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.GAME_STATE));
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to run to add a game state to this library item.
	 */
	public EventHandler<ActionEvent> getAddGameStateAction() {
		return (e) -> { addGameState(); };
	}
	
	private void addGameState() {
		try {
			MacroBuilderViewFactory macroBuilder = serviceManager.<MacroBuilderViewFactory>get("MacroBuilderViewFactory");
			MediaFinder finder = serviceManager.<MediaFinder>get("MediaFinder");
			
			GameStateModel model = new GameStateModel(new GameStatePersistenceObject(new String()), dialogService, macroBuilder,
					finder, triggerViewFactory, playerModFactory, players, layouts, languageService, actionViewFactory,
					dialogProvider, styleService, urlProvider);
			GameStateView view = new GameStateView(model, languageService, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(view, () -> {
				LogRunner.logger().log(Level.INFO, String.format("Adding game state %s to %s", model.persistableGameState().stateId(), name.get()));
				item.gameStates().add(model.persistableGameState());
				gameStates.add(model.persistableGameState());
			});
			
			dialog.isValid().bind(model.valid());
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.GAME_STATE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The name of this library item.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return The author of this library item.
	 */
	public SimpleStringProperty author() {
		return author;
	}
	
	/**
	 * 
	 * @return The display text for attribute.
	 */
	public SimpleStringProperty attributeText() {
		return attributeText;
	}
	
	/**
	 * 
	 * @return The display text for characteristic
	 */
	public SimpleStringProperty characteristicText() {
		return characteristicText;
	}
	
	/**
	 * 
	 * @return The display text for body part.
	 */
	public SimpleStringProperty bodyPartText() {
		return bodyPartText;
	}
	
	/**
	 * 
	 * @return The display text for property.
	 */
	public SimpleStringProperty propertyText() {
		return propertyText;
	}
	
	/**
	 * 
	 * @return The display text for item.
	 */
	public SimpleStringProperty itemText() {
		return itemText;
	}
	
	/**
	 * 
	 * @return The display text for player.
	 */
	public SimpleStringProperty playerText() {
		return playerText;
	}
	
	/**
	 * 
	 * @return The display text for trigger.
	 */
	public SimpleStringProperty triggerText() {
		return triggerText;
	}
	
	/**
	 * 
	 * @return The display text for action.
	 */
	public SimpleStringProperty actionText() {
		return actionText;
	}
	
	/**
	 * 
	 * @return The display text for option.
	 */
	public SimpleStringProperty optionText() {
		return optionText;
	}
	
	/**
	 * 
	 * @return The display text for timer.
	 */
	public SimpleStringProperty timerText() {
		return timerText;
	}
	
	/**
	 * 
	 * @return The display text for game state.
	 */
	public SimpleStringProperty gameStateText() {
		return gameStateText;
	}
	
	/**
	 * 
	 * @return The display text for layout.
	 */
	public SimpleStringProperty layoutText() {
		return layoutText;
	}
	
	/**
	 * 
	 * @return The display text for name.
	 */
	public SimpleStringProperty nameText() {
		return nameText;
	}
	
	/**
	 * 
	 * @return The display text for author.
	 */
	public SimpleStringProperty authorText() {
		return authorText;
	}
}
