package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.GameExplorerModel;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.gamestate.GameStateView;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import ilusr.textadventurecreator.views.layout.LayoutCreatorView;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.player.PlayerModel;
import ilusr.textadventurecreator.views.player.PlayerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class GameExplorerModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService;
	private LibraryService libraryService;
	private MacroBuilderViewFactory macroFactory;
	private MediaFinder mediaFinder;
	private ILayoutService layoutService; 
	private TextAdventureProvider textAdventureProvider;
	private TriggerViewFactory triggerFactory;
	private PlayerModProviderFactory playerModFactory;
	private ILanguageService languageService;
	private ActionViewFactory actionViewFactory;
	private InternalURLProvider urlProvider;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	
	private TextAdventurePersistenceObject tav;
	private PlayerPersistenceObject player1;
	private GameStatePersistenceObject gameState1;
	private LayoutPersistenceObject layout1;
	
	private GameExplorerModel model;
	
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		libraryService = mock(LibraryService.class);
		macroFactory = mock(MacroBuilderViewFactory.class);
		mediaFinder = mock(MediaFinder.class);
		layoutService = mock(ILayoutService.class);
		
		textAdventureProvider = mock(TextAdventureProvider.class);
		tav = mock(TextAdventurePersistenceObject.class);
		player1 = mock(PlayerPersistenceObject.class);
		when(player1.inventory()).thenReturn(mock(InventoryPersistenceObject.class));
		when(player1.equipment()).thenReturn(mock(EquipmentPersistenceObject.class));
		ArrayList<PlayerPersistenceObject> players = new ArrayList<>();
		players.add(player1);
		when(tav.players()).thenReturn(players);
		
		gameState1 = mock(GameStatePersistenceObject.class);
		when(gameState1.layout()).thenReturn(mock(LayoutInfoPersistenceObject.class));
		ArrayList<GameStatePersistenceObject> gameStates = new ArrayList<>();
		gameStates.add(gameState1);
		when(tav.gameStates()).thenReturn(gameStates);
		
		layout1 = mock(LayoutPersistenceObject.class);
		when(layout1.getLayout()).thenReturn(mock(LayoutGridPersistenceObject.class));
		ArrayList<LayoutPersistenceObject> layouts = new ArrayList<>();
		layouts.add(layout1);
		when(tav.getLayouts()).thenReturn(layouts);
		when(tav.gameName()).thenReturn("Test Game");
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		when(proj.getTextAdventure()).thenReturn(tav);
		when(textAdventureProvider.getTextAdventureProject()).thenReturn(proj);
		
		triggerFactory = mock(TriggerViewFactory.class);
		playerModFactory = mock(PlayerModProviderFactory.class);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.GAME_STATES)).thenReturn("Game States");
		when(languageService.getValue(DisplayStrings.PLAYERS)).thenReturn("Players");
		when(languageService.getValue(DisplayStrings.LAYOUTS)).thenReturn("Layouts");
		
		actionViewFactory = mock(ActionViewFactory.class);
		urlProvider = mock(InternalURLProvider.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		
		model = new GameExplorerModel(dialogService, libraryService, macroFactory, mediaFinder, layoutService, 
									  textAdventureProvider, triggerFactory, playerModFactory, languageService, 
									  actionViewFactory, urlProvider, dialogProvider, styleService);
	}
	
	@Test
	public void testPlayers() {
		assertEquals(1, model.players().size());
	}

	@Test
	public void testGameStates() {
		assertEquals(1, model.gameStates().size());
	}
	
	@Test
	public void testLayouts() {
		assertEquals(1, model.layouts().size());
	}
	
	@Test
	public void testAddPlayer() {
		PlayerPersistenceObject player = mock(PlayerPersistenceObject.class);
		when(player.playerName()).thenReturn("Test player");
		PlayerModel pmodel = mock(PlayerModel.class);
		when(pmodel.persistablePlayer()).thenReturn(player);
		PlayerView view = mock(PlayerView.class);
		when(view.model()).thenReturn(pmodel);
		ITabContent tab = mock(ITabContent.class);
		when(tab.content()).thenReturn(new SimpleObjectProperty<Node>(view));
		when(layoutService.addTab(BluePrintNames.Player)).thenReturn("Player1");
		when(layoutService.getTabContent("Player1")).thenReturn(tab);
		
		model.addPlayer();
		verify(layoutService, times(1)).addTab(BluePrintNames.Player);
		verify(layoutService, times(1)).getTabContent("Player1");
		verify(tav, times(1)).addPlayer(player);
		assertEquals(2, model.players().size());
	}
	
	@Test
	public void testAddPlayerFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addPlayerFromLibrary();
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testRemovePlayer() {
		model.players().remove(0);
		verify(tav, times(1)).removePlayer(player1);
	}
	
	@Test
	public void testAddGameState() {
		GameStatePersistenceObject gameState = mock(GameStatePersistenceObject.class);
		when(gameState.stateId()).thenReturn("Test state");
		GameStateModel gmodel = mock(GameStateModel.class);
		when(gmodel.persistableGameState()).thenReturn(gameState);
		GameStateView view = mock(GameStateView.class);
		when(view.model()).thenReturn(gmodel);
		ITabContent tab = mock(ITabContent.class);
		when(tab.content()).thenReturn(new SimpleObjectProperty<Node>(view));
		when(layoutService.addTab(BluePrintNames.GameState)).thenReturn("GameState1");
		when(layoutService.getTabContent("GameState1")).thenReturn(tab);
		
		model.addGameState();
		verify(layoutService, times(1)).addTab(BluePrintNames.GameState);
		verify(layoutService, times(1)).getTabContent("GameState1");
		verify(tav, times(1)).addGameState(gameState);
		assertEquals(2, model.gameStates().size());
	}
	
	@Test
	public void testAddGameStateFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addGameStateFromLibrary();
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testRemoveGameState() {
		model.gameStates().remove(0);
		verify(tav, times(1)).removeGameState(gameState1);
	}
	
	@Test
	public void testAddLayout() {
		LayoutPersistenceObject layout = mock(LayoutPersistenceObject.class);
		when(layout.id()).thenReturn("Test layout");
		LayoutCreatorModel lmodel = mock(LayoutCreatorModel.class);
		when(lmodel.persistableLayout()).thenReturn(layout);
		LayoutCreatorView view = mock(LayoutCreatorView.class);
		when(view.model()).thenReturn(lmodel);
		ITabContent tab = mock(ITabContent.class);
		when(tab.content()).thenReturn(new SimpleObjectProperty<Node>(view));
		when(layoutService.addTab(BluePrintNames.LayoutCreator)).thenReturn("Layout1");
		when(layoutService.getTabContent("Layout1")).thenReturn(tab);
		
		model.addLayout();
		verify(layoutService, times(1)).addTab(BluePrintNames.LayoutCreator);
		verify(layoutService, times(1)).getTabContent("Layout1");
		verify(tav, times(1)).addLayout(layout);
		assertEquals(2, model.layouts().size());
	}
	
	@Test
	public void testAddLayoutFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addLayoutFromLibrary();
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testRemoveLayout() {
		model.layouts().remove(0);
		verify(tav, times(1)).removeLayout(layout1);
	}
	
	@Test
	public void testGameName() {
		assertEquals("Test Game", model.gameName().get());
	}
	
	@Test
	public void testGameStateText() {
		assertEquals("Game States", model.gameStateText().get());
	}
	
	@Test
	public void testPlayerText() {
		assertEquals("Players", model.playerText().get());
	}
	
	@Test
	public void testLayoutText() {
		assertEquals("Layouts", model.layoutText().get());
	}
}
