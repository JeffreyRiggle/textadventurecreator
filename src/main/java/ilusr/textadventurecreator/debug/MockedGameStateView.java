package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.gamestatemanager.GameStateManager;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MockedGameStateView extends AnchorPane implements Initializable {

	@FXML
	private Label displayLabel;
	@FXML
	private Button exit;
	
	private final ILanguageService languageService;
	private final String id;
	
	private SimpleStringProperty display;
	private GameStateManager manager;
	
	/**
	 * 
	 * @param id The id of the game state.
	 * @param manager A @see GameStateManager.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MockedGameStateView(String id, GameStateManager manager, ILanguageService languageService) {
		this.manager = manager;
		this.languageService = languageService;
		this.id = id;
		display = new SimpleStringProperty(languageService.getValue(DisplayStrings.MOVING_GAMESTATE) + id);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MockedGameStateView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.getStylesheets().add(getClass().getResource("MockedGameStateView.css").toExternalForm());
		displayLabel.textProperty().bindBidirectional(display);
		exit.setOnAction((e) -> {
			manager.finish();
		});
		exit.setText(languageService.getValue(DisplayStrings.EXIT));
		
		languageService.addListener(() -> {
			exit.setText(languageService.getValue(DisplayStrings.EXIT));
			display.set(languageService.getValue(DisplayStrings.MOVING_GAMESTATE) + id);
		});
	}
}
