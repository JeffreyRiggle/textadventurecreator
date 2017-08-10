package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryItemModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.action.ActionModel;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.AppendTextViewProvider;
import ilusr.textadventurecreator.views.action.CompletionActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutableActionViewProvider;
import ilusr.textadventurecreator.views.action.FinishActionViewProvider;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.action.SaveActionViewProvider;
import ilusr.textadventurecreator.views.action.ScriptedActionViewProvider;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class LibraryItemModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService;
	private LibraryItem item;
	private List<ItemPersistenceObject> libItems;
	private List<BodyPartPersistenceObject> libBparts;
	private List<TriggerPersistenceObject> libTriggers;
	private List<OptionPersistenceObject> libOptions;
	private List<PlayerPersistenceObject> libPlayers;
	private List<GameStatePersistenceObject> libGameStates;
	private TriggerViewFactory triggerViewFactory;
	private ActionViewFactory actionViewFactory;
	private PlayerModProviderFactory playerModFactory;
	private ILanguageService languageService;
	private InternalURLProvider urlProvider;
	private ServiceManager serviceManager;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	
	private LibraryItemModel model;
	
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		item = mock(LibraryItem.class);
		libItems = new ArrayList<>();
		when(item.items()).thenReturn(libItems);
		libBparts = new ArrayList<>();
		when(item.bodyParts()).thenReturn(libBparts);
		libTriggers = new ArrayList<>();
		when(item.triggers()).thenReturn(libTriggers);
		libOptions = new ArrayList<>();
		when(item.options()).thenReturn(libOptions);
		libPlayers = new ArrayList<>();
		when(item.players()).thenReturn(libPlayers);
		libGameStates = new ArrayList<>();
		when(item.gameStates()).thenReturn(libGameStates);
		
		triggerViewFactory = mock(TriggerViewFactory.class);
		actionViewFactory = mock(ActionViewFactory.class);
		playerModFactory = mock(PlayerModProviderFactory.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.BODY_PART)).thenReturn("Body Part");
		when(languageService.getValue(DisplayStrings.ITEM)).thenReturn("Item");
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.TRIGGER)).thenReturn("Trigger");
		when(languageService.getValue(DisplayStrings.OPTION)).thenReturn("Option");
		when(languageService.getValue(DisplayStrings.GAME_STATE)).thenReturn("Game State");
		when(languageService.getValue(DisplayStrings.ATTRIBUTES)).thenReturn("Attributes");
		when(languageService.getValue(DisplayStrings.CHARACTERISTICS)).thenReturn("Characteristics");
		when(languageService.getValue(DisplayStrings.BODY_PARTS)).thenReturn("Body Parts");
		when(languageService.getValue(DisplayStrings.PROPERTIES)).thenReturn("Properties");
		when(languageService.getValue(DisplayStrings.ITEMS)).thenReturn("Items");
		when(languageService.getValue(DisplayStrings.PLAYERS)).thenReturn("Players");
		when(languageService.getValue(DisplayStrings.TRIGGERS)).thenReturn("Triggers");
		when(languageService.getValue(DisplayStrings.ACTIONS)).thenReturn("Actions");
		when(languageService.getValue(DisplayStrings.OPTIONS)).thenReturn("Options");
		when(languageService.getValue(DisplayStrings.TIMERS)).thenReturn("Timers");
		when(languageService.getValue(DisplayStrings.GAME_STATES)).thenReturn("Game States");
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.AUTHOR)).thenReturn("Author");
		when(languageService.getValue(DisplayStrings.LAYOUT)).thenReturn("Layout");
		
		urlProvider = mock(InternalURLProvider.class);
		serviceManager = mock(ServiceManager.class);
		when(serviceManager.<MacroBuilderViewFactory>get("MacroBuilderViewFactory")).thenReturn(mock(MacroBuilderViewFactory.class));
		when(serviceManager.<MediaFinder>get("MediaFinder")).thenReturn(mock(MediaFinder.class));
		when(serviceManager.get("ScriptedActionViewProvider")).thenReturn(mock(ScriptedActionViewProvider.class));
		when(serviceManager.get("FinishActionViewProvider")).thenReturn(mock(FinishActionViewProvider.class));
		when(serviceManager.get("AppendTextViewProvider")).thenReturn(mock(AppendTextViewProvider.class));
		when(serviceManager.get("CompletionActionViewProvider")).thenReturn(mock(CompletionActionViewProvider.class));
		when(serviceManager.get("ExecutableActionViewProvider")).thenReturn(mock(ExecutableActionViewProvider.class));
		when(serviceManager.get("SaveActionViewProvider")).thenReturn(mock(SaveActionViewProvider.class));
		
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		
		model = new LibraryItemModel(dialogService, item, triggerViewFactory, actionViewFactory, playerModFactory,
				languageService, urlProvider, serviceManager, dialogProvider, styleService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}
	
	@Test
	public void testAttributeKey() {
		assertNotNull(model.addAttributeKey());
	}
	
	@Test
	public void testAddAttibute() {
		assertEquals(0, model.attributes().size());
		model.getAddAttributeAction().handle(mock(ActionEvent.class));
		assertEquals(1, model.attributes().size());
	}
	
	@Test
	public void testCharacteristicKey() {
		assertNotNull(model.addCharacteristicKey());
	}
	
	@Test
	public void testAddCharacteristic() {
		assertEquals(0, model.characteristics().size());
		model.getAddCharacteristicAction().handle(mock(ActionEvent.class));
		assertEquals(1, model.characteristics().size());
	}
	
	@Test
	public void testBodyPartKey() {
		assertNotNull(model.addBodyPartKey());
	}
	
	@Test
	public void testAddBodyPart() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		assertEquals(0, model.bodyParts().size());
		assertEquals(0, libBparts.size());
		model.getAddBodyPartAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Body Part"));
		
		captor.getValue().run();
		assertEquals(1, model.bodyParts().size());
		assertEquals(1, libBparts.size());
	}
	
	@Test
	public void testEditBodyPart() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		model.getEditBodyPartAction().execute(mock(BodyPartPersistenceObject.class));
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Body Part"));
	}
	
	@Test
	public void testPropertyKey() {
		assertNotNull(model.addPropertyKey());
	}
	
	@Test
	public void testAddProperty() {
		assertEquals(0, model.properties().size());
		model.getAddPropertyAction().handle(mock(ActionEvent.class));
		assertEquals(1, model.properties().size());
	}
	
	@Test
	public void testItemKey() {
		assertNotNull(model.addItemKey());
	}
	
	@Test
	public void testAddItem() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		assertEquals(0, model.items().size());
		assertEquals(0, libItems.size());
		model.getAddItemAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any(Node.class), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Item"));
		
		captor.getValue().run();
		assertEquals(1, model.items().size());
		assertEquals(1, libItems.size());
	}
	
	@Test
	public void testEditItem() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.getEditItemAction().execute(mock(ItemPersistenceObject.class));
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Item"));
	}
	
	@Test
	public void testPlayerKey() {
		assertNotNull(model.addPlayerKey());
	}
	
	@Test
	public void testAddPlayer() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		assertEquals(0, model.players().size());
		assertEquals(0, libPlayers.size());
		model.getAddPlayerAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Player"));
		
		captor.getValue().run();
		
		assertEquals(1, model.players().size());
		assertEquals(1, libPlayers.size());
	}
	
	@Test
	public void testEditPlayer() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		PlayerPersistenceObject player = mock(PlayerPersistenceObject.class);
		when(player.inventory()).thenReturn(mock(InventoryPersistenceObject.class));
		when(player.equipment()).thenReturn(mock(EquipmentPersistenceObject.class));
		
		model.getEditPlayerAction().execute(player);
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Player"));
	}
	
	@Test
	public void testTriggerKey() {
		assertNotNull(model.addTriggerKey());
	}
	
	@Test
	public void testAddTrigger() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		ArgumentCaptor<TriggerModel> triggerCaptor = ArgumentCaptor.forClass(TriggerModel.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		when(triggerViewFactory.create(any())).thenReturn(mock(TriggerView.class));
		
		assertEquals(0, model.triggers().size());
		assertEquals(0, item.triggers().size());
		model.getAddTriggerAction().handle(mock(ActionEvent.class));
		verify(triggerViewFactory, times(1)).create(triggerCaptor.capture(), any());
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Trigger"));
		
		TriggerPersistenceObject trig = mock(TriggerPersistenceObject.class);
		when(trig.type()).thenReturn("Text");
		triggerCaptor.getValue().persistenceObject().set(trig);
		captor.getValue().run();
		assertEquals(1, model.triggers().size());
		assertEquals(1, item.triggers().size());
	}
	
	@Test
	public void testEditTrigger() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		model.getEditTriggerAction().execute(mock(TriggerPersistenceObject.class));
		verify(triggerViewFactory, times(1)).create(any(TriggerModel.class), eq(new ArrayList<PlayerPersistenceObject>()));
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Trigger"));
	}
	
	@Test
	public void testActionKey() {
		assertNotNull(model.addActionKey());
	}
	
	@Test
	public void testAddAction() {
		assertEquals(0, model.actions().size());
		model.getAddActionAction().handle(mock(ActionEvent.class));
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testEditAction() {
		assertEquals(0, model.actions().size());
		model.getEditActionAction().execute(mock(ActionPersistenceObject.class));
		verify(actionViewFactory, times(1)).create(any(ActionModel.class), eq(new ArrayList<PlayerPersistenceObject>()));
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testOptionKey() {
		assertNotNull(model.addOptionKey());
	}
	
	@Test
	public void testAddOption() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		assertEquals(0, model.options().size());
		assertEquals(0, libOptions.size());
		model.getAddOptionAction().handle(mock(ActionEvent.class));
		verify(playerModFactory, times(1)).create(any(), eq(new ArrayList<PlayerPersistenceObject>()));
		verify(dialogProvider, times(1)).create(any());
		verify(dialog, times(1)).setOnComplete(captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Option"));
		
		captor.getValue().run();
		assertEquals(1, model.options().size());
		assertEquals(1, libOptions.size());
	}
	
	@Test
	public void testEditOption() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		assertEquals(0, model.options().size());
		model.getEditOptionAction().execute(mock(OptionPersistenceObject.class));
		verify(playerModFactory, times(1)).create(any(), eq(new ArrayList<PlayerPersistenceObject>()));
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Option"));
	}
	
	@Test
	public void testTimerKey() {
		assertNotNull(model.addTimerKey());
	}
	
	@Test
	public void testAddTimer() {
		assertEquals(0, model.timers().size());
		model.getAddTimerAction().handle(mock(ActionEvent.class));
		assertEquals(1, model.timers().size());
	}
	
	@Test
	public void testLayoutKey() {
		assertNotNull(model.addLayoutKey());		
	}
	
	@Test
	public void testAddLayout() {
		assertEquals(0, model.layouts().size());
		model.getAddLayoutAction().handle(mock(ActionEvent.class));
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testEditLayout() {
		LayoutPersistenceObject layout = mock(LayoutPersistenceObject.class);
		when(layout.getLayout()).thenReturn(mock(LayoutGridPersistenceObject.class));
		
		model.getEditLayoutAction().execute(layout);
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testGameStateKey() {
		assertNotNull(model.addGameStateKey());
	}
	
	@Test
	public void testAddGameState() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		assertEquals(0, model.gameStates().size());
		assertEquals(0, libGameStates.size());
		model.getAddGameStateAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Game State"));
		
		captor.getValue().run();
		
		assertEquals(1, model.gameStates().size());
		assertEquals(1, libGameStates.size());
	}
	
	@Test
	public void testEditGameState() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any(), any())).thenReturn(dialog);
		
		GameStatePersistenceObject gs = mock(GameStatePersistenceObject.class);
		when(gs.layout()).thenReturn(mock(LayoutInfoPersistenceObject.class));
		
		model.getEditGameStateAction().execute(gs);
		verify(dialogProvider, times(1)).create(any(), captor.capture());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Game State"));
		
		captor.getValue().run();
		//TODO What to verify.
	}
	
	@Test
	public void testName() {
		assertEquals(null, model.name().get());
		model.name().set("test");
		assertEquals("test", model.name().get());
	}
	
	@Test
	public void testAuthor() {
		assertEquals(null, model.author().get());
		model.author().set("tester");
		assertEquals("tester", model.author().get());
	}
	
	@Test
	public void testAttributeText() {
		assertEquals("Attributes", model.attributeText().get());
	}
	
	@Test
	public void testCharacteristicText() {
		assertEquals("Characteristics", model.characteristicText().get());
	}
	
	@Test
	public void testBodyPartText() {
		assertEquals("Body Parts", model.bodyPartText().get());
	}
	
	@Test
	public void testPropertyText() {
		assertEquals("Properties", model.propertyText().get());
	}
	
	@Test
	public void testItemText() {
		assertEquals("Items", model.itemText().get());
	}
	
	@Test
	public void testPlayerText() {
		assertEquals("Players", model.playerText().get());
	}
	
	@Test
	public void testTriggerText() {
		assertEquals("Triggers", model.triggerText().get());
	}
	
	@Test
	public void testActionText() {
		assertEquals("Actions", model.actionText().get());
	}
	
	@Test
	public void testOptionText() {
		assertEquals("Options", model.optionText().get());
	}
	
	@Test
	public void testTimerText() {
		assertEquals("Timers", model.timerText().get());
	}
	
	@Test
	public void testGameStateText() {
		assertEquals("Game States", model.gameStateText().get());
	}
	
	@Test
	public void testLayoutText() {
		assertEquals("Layout", model.layoutText().get());
	}
	
	@Test
	public void testNameText() {
		assertEquals("Library Name", model.nameText().get());
	}
	
	@Test
	public void testAuthorText() {
		assertEquals("Author", model.authorText().get());
	}
}
