package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameDebuggerView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private Label currentGameState;
	
	@FXML
	private AnchorPane gameArea;
	
	@FXML
	private PlayersDebugView debugLocals;
	
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private GameDebuggerModel model;
	
	/**
	 * 
	 * @param model The @see GameDebuggerModel to associate with this view.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public GameDebuggerView(GameDebuggerModel model, 
							ILanguageService languageService,
							IStyleContainerService styleService,
							InternalURLProvider urlProvider) {
		this.model = model;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameDebuggerView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	/**
	 * 
	 * @return A @see AnchorPane representing the game area.
	 */
	public AnchorPane gameArea() {
		return gameArea;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currentGameState.textProperty().bind(model.currentGameState());
		
		debugLocals.setModel(new PlayersDebugModel(model.players(), languageService));
		
		setupStyles();
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "debugstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.DEBUG), this, false);
		
		String style = styleService.get(StyledComponents.DEBUG);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
