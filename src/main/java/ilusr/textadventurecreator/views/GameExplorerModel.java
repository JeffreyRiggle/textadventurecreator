package ilusr.textadventurecreator.views;

import java.util.List;
import java.util.logging.Level;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.GameStateFinder;
import ilusr.textadventurecreator.search.GameStateFinderModel;
import ilusr.textadventurecreator.search.LayoutFinder;
import ilusr.textadventurecreator.search.LayoutFinderModel;
import ilusr.textadventurecreator.search.PlayerFinder;
import ilusr.textadventurecreator.search.PlayerFinderModel;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameExplorerModel {

	private final ILayoutService layoutService;
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final MacroBuilderViewFactory macroFactory;
	private final MediaFinder mediaFinder;
	private final TriggerViewFactory triggerFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final ActionViewFactory actionViewFactory;
	private final InternalURLProvider urlProvider;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	
	private TextAdventurePersistenceObject tavPersistence;
	private ObservableList<PlayerModel> players;
	private ObservableList<GameStateModel> gameStates;
	private ObservableList<LayoutCreatorModel> layouts;
	private SimpleStringProperty gameText;
	private LanguageAwareString gameStateText;
	private LanguageAwareString playerText;
	private LanguageAwareString layoutText;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param macroFactory A @see MacroBuilderViewFactory to build macros.
	 * @param mediaFinder A @see MediaFinder to find and convert media.
	 * @param layoutService A @see LayoutService to modify tabs.
	 * @param textAdventureProvider A @see TextAdventureProvider to provide the current text adventure.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModeFactory to create player mods.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to provide action views.
	 * @param urlProvider A @see InternalURLProvider to provide internal resources.
	 * @param styleService service to manage styles.
	 */
	public GameExplorerModel(IDialogService dialogService,
							 LibraryService libraryService,
							 MacroBuilderViewFactory macroFactory,
							 MediaFinder mediaFinder,
							 ILayoutService layoutService, 
							 TextAdventureProvider textAdventureProvider,
							 TriggerViewFactory triggerFactory,
							 PlayerModProviderFactory playerModFactory,
							 ILanguageService languageService,
							 ActionViewFactory actionViewFactory,
							 InternalURLProvider urlProvider,
							 IDialogProvider dialogProvider,
							 IStyleContainerService styleService) {
		this.layoutService = layoutService;
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.macroFactory = macroFactory;
		this.mediaFinder = mediaFinder;
		this.triggerFactory = triggerFactory;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.actionViewFactory = actionViewFactory;
		this.urlProvider = urlProvider;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		
		players = FXCollections.observableArrayList();
		gameStates = FXCollections.observableArrayList();
		layouts = FXCollections.observableArrayList();
		tavPersistence = textAdventureProvider.getTextAdventureProject().getTextAdventure();
		gameText = new SimpleStringProperty(tavPersistence.gameName());
		gameStateText = new LanguageAwareString(languageService, DisplayStrings.GAME_STATES);
		playerText = new LanguageAwareString(languageService, DisplayStrings.PLAYERS);
		layoutText = new LanguageAwareString(languageService, DisplayStrings.LAYOUTS);
		
		initialize();
	}
	
	private void initialize() {
		for (PlayerPersistenceObject player : tavPersistence.players()) {
			LogRunner.logger().log(Level.INFO, String.format("Adding player %s to explorer.", player.playerName()));
			players.add(new PlayerModel(dialogService, libraryService, player, languageService, dialogProvider, styleService, urlProvider));
		}
		
		for (GameStatePersistenceObject gameState : tavPersistence.gameStates()) {
			LogRunner.logger().log(Level.INFO, String.format("Adding game state %s to explorer.", gameState.stateId()));
			gameStates.add(new GameStateModel(gameState, dialogService, macroFactory, mediaFinder, triggerFactory,
					playerModFactory, languageService, actionViewFactory, dialogProvider, styleService, urlProvider));
		}
		
		for (LayoutPersistenceObject pLayout : tavPersistence.getLayouts()) {
			LogRunner.logger().log(Level.INFO, String.format("Adding layout %s to explorer.", pLayout.id()));
			layouts.add(new LayoutCreatorModel(pLayout, languageService, dialogService, urlProvider, new SelectionManager()));
		}
		
		players.addListener((Change<? extends PlayerModel> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends PlayerModel> rPlayers = c.getRemoved();
			List<? extends PlayerModel> aPlayers = c.getList();
			
			for (PlayerModel model : rPlayers) {
				if (aPlayers.contains(model)) {
					return;
				}
				
				LogRunner.logger().log(Level.INFO, String.format("Removing player %s from game.", model.persistablePlayer().playerName()));
				tavPersistence.removePlayer(model.persistablePlayer());
			}
		});
		
		gameStates.addListener((Change<? extends GameStateModel> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends GameStateModel> rGame = c.getRemoved();
			List<? extends GameStateModel> aGame = c.getList();
			
			for (GameStateModel model : rGame) {
				if (aGame.contains(model)) {
					return;
				}
				
				LogRunner.logger().log(Level.INFO, String.format("Removing game state %s from game.", model.persistableGameState().stateId()));
				tavPersistence.removeGameState(model.persistableGameState());
			}
		});
		
		layouts.addListener((Change<? extends LayoutCreatorModel> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends LayoutCreatorModel> rLayout = c.getRemoved();
			List<? extends LayoutCreatorModel> aLayout = c.getList();
			
			for (LayoutCreatorModel model : rLayout) {
				if (aLayout.contains(model)) {
					return;
				}
				
				LogRunner.logger().log(Level.INFO, String.format("Removing layout %s from game.", model.persistableLayout().id()));
				tavPersistence.removeLayout(model.persistableLayout());
			}
		});
	}
	
	/**
	 * Creates a player tab and associates the tabs player to the current game.
	 */
	public void addPlayer() {
		String id = layoutService.addTab(BluePrintNames.Player);
		ITabContent tab = layoutService.getTabContent(id);
		PlayerView view = (PlayerView)tab.content().get();
		
		LogRunner.logger().log(Level.INFO, "Adding player to game.");
		tavPersistence.addPlayer(view.model().persistablePlayer());
		players.add(0, view.model());
	}
	
	/**
	 * Creates a player tab from a library item and associates that tabs player to the current game.
	 */
	public void addPlayerFromLibrary() {
		PlayerFinderModel finder = new PlayerFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		Dialog dialog = dialogProvider.create(new PlayerFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			LogRunner.logger().log(Level.INFO, String.format("Adding player %s to game.", finder.foundValue().playerName()));
			tavPersistence.addPlayer(finder.foundValue());
			players.add(0, new PlayerModel(dialogService, libraryService, finder.foundValue(), languageService, dialogProvider, styleService, urlProvider));
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return The players associated with the current game.
	 */
	public ObservableList<PlayerModel> players() {
		return players;
	}
	
	/**
	 * Creates a game state tab and associates the tabs game state with the current game.
	 */
	public void addGameState() {
		String id = layoutService.addTab(BluePrintNames.GameState);
		ITabContent tab = layoutService.getTabContent(id);
		GameStateView view = (GameStateView)tab.content().get();
		
		LogRunner.logger().log(Level.INFO, "Adding game state to game.");
		tavPersistence.addGameState(view.model().persistableGameState());
		gameStates.add(0, view.model());
	}
	
	/**
	 * Creates a game state tab from a library item and associates the tabs game state with the current game.
	 */
	public void addGameStateFromLibrary() {
		GameStateFinderModel finder = new GameStateFinderModel(libraryService, languageService, dialogService,
				actionViewFactory, triggerFactory, dialogProvider, urlProvider, styleService);
		Dialog dialog = dialogProvider.create(new GameStateFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			LogRunner.logger().log(Level.INFO, String.format("Adding game state %s to game.", finder.foundValue().stateId()));
			tavPersistence.addGameState(finder.foundValue());
			gameStates.add(0, new GameStateModel(finder.foundValue(), dialogService, macroFactory, mediaFinder, triggerFactory,
					playerModFactory, languageService, actionViewFactory, dialogProvider, styleService, urlProvider));
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return The game states associated with the current game.
	 */
	public ObservableList<GameStateModel> gameStates() {
		return gameStates;
	}
	
	/**
	 * Creates a layout creator tab and associates the tabs layout to the current game.
	 */
	public void addLayout() {
		String id = layoutService.addTab(BluePrintNames.LayoutCreator);
		ITabContent tab = layoutService.getTabContent(id);
		LayoutCreatorView view = (LayoutCreatorView)tab.content().get();
		
		LogRunner.logger().log(Level.INFO, "Adding layout to game.");
		tavPersistence.addLayout(view.model().persistableLayout());
		layouts.add(0, view.model());
	}
	
	/**
	 * Creates a layout creator tab from a library item and associates the tabs layout to the current game.
	 */
	public void addLayoutFromLibrary() {
		LayoutFinderModel finder = new LayoutFinderModel(libraryService, languageService, dialogService, urlProvider);
		Dialog dialog = dialogProvider.create(new LayoutFinder(finder, styleService, urlProvider));
		
		dialog.setOnComplete(() -> {
			LogRunner.logger().log(Level.INFO, String.format("Adding layout %s to game.", finder.foundValue().id()));
			tavPersistence.addLayout(finder.foundValue());
			layouts.add(0, new LayoutCreatorModel(finder.foundValue(), languageService, dialogService, urlProvider, new SelectionManager()));
		});
		
		dialogService.displayModal(dialog);
	}
	
	/**
	 * 
	 * @return The layouts associated with the current game.
	 */
	public ObservableList<LayoutCreatorModel> layouts() {
		return layouts;
	}
	
	/**
	 * 
	 * @return The name of the current game.
	 */
	public SimpleStringProperty gameName() {
		return gameText;
	}
	
	/**
	 * 
	 * @return A display string for game states.
	 */
	public SimpleStringProperty gameStateText() {
		return gameStateText;
	}
	
	/**
	 * 
	 * @return A display string for players.
	 */
	public SimpleStringProperty playerText() {
		return playerText;
	}
	
	/**
	 * 
	 * @return A display string for layout.
	 */
	public SimpleStringProperty layoutText() {
		return layoutText;
	}
}
