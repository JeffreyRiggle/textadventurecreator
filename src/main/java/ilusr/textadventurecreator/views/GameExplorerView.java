package ilusr.textadventurecreator.views;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.MultiValueObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.iroshell.services.LayoutService;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.converters.GameStateToTreeItemConverter;
import ilusr.textadventurecreator.views.converters.LayoutCreatorToTreeItemConverter;
import ilusr.textadventurecreator.views.converters.PlayerToTreeItemConverter;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import ilusr.textadventurecreator.views.player.PlayerModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;

@SuppressWarnings("rawtypes")
public class GameExplorerView extends AnchorPane implements Initializable, IStyleWatcher{

	@FXML
	private Button addPlayer;

	@FXML
	private Button addPlayerFromLibrary;
	
	@FXML
	private Button addGameState;

	@FXML
	private Button addGameStateFromLibrary;
	
	@FXML
	private Button addLayout;
	
	@FXML
	private Button addLayoutFromLibrary;
	
	@FXML
	private TreeItem players;

	@FXML
	private TreeItem gameStates;
	
	@FXML
	private TreeItem layouts;
	
	@FXML
	private TreeItem root;
	
	private final LayoutService layoutService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private GameExplorerModel model;
	
	/**
	 * 
	 * @param model A @see GameExplorerModel to bind to.
	 * @param layoutService A @see LayoutService to provide layouts.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameExplorerView(GameExplorerModel model, 
							LayoutService layoutService,
							IStyleContainerService styleService,
							InternalURLProvider urlProvider) {
		this.model = model;
		this.layoutService = layoutService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader explorerLoader = new FXMLLoader(getClass().getResource("GameExplorerView.fxml"));
		explorerLoader.setRoot(this);
		explorerLoader.setController(this);
		
		try {
			explorerLoader.load();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addPlayer.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		addPlayer.setOnAction((e) -> {
			model.addPlayer();
		});
		
		addPlayerFromLibrary.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addPlayerFromLibrary.setOnAction((e) -> {
			model.addPlayerFromLibrary();
		});
		
		addGameState.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		addGameState.setOnAction((e) -> {
			model.addGameState();
		});
		
		addGameStateFromLibrary.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addGameStateFromLibrary.setOnAction((e) -> {
			model.addGameStateFromLibrary();
		});
		
		addLayout.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		addLayout.setOnAction((e) -> {
			model.addLayout();
		});
		
		addLayoutFromLibrary.getStylesheets().add(getClass().getResource("LibraryAdd.css").toExternalForm());
		addLayoutFromLibrary.setOnAction((e) -> {
			model.addLayoutFromLibrary();
		});
		
		MultiValueObservableListBinder<PlayerModel, TreeItem> pbinder = 
				new MultiValueObservableListBinder<PlayerModel, TreeItem>(model.players(), players.getChildren(), new PlayerToTreeItemConverter(model, layoutService));
		
		MultiValueObservableListBinder<GameStateModel, TreeItem> gbinder = 
				new MultiValueObservableListBinder<GameStateModel, TreeItem>(model.gameStates(), gameStates.getChildren(), new GameStateToTreeItemConverter(model, layoutService));
		
		MultiValueObservableListBinder<LayoutCreatorModel, TreeItem> lbinder =
				new MultiValueObservableListBinder<LayoutCreatorModel, TreeItem>(model.layouts(), layouts.getChildren(), new LayoutCreatorToTreeItemConverter(model, layoutService));
		
		players.valueProperty().bind(model.playerText());
		gameStates.valueProperty().bind(model.gameStateText());
		layouts.valueProperty().bind(model.layoutText());
		root.valueProperty().bind(model.gameName());
		setupStyles();
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "explorerstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.EXPLORER), this, false);
		
		String style = styleService.get(StyledComponents.EXPLORER);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
