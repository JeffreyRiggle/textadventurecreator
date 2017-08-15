package ilusr.textadventurecreator.settings;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameSettings extends AnchorPane implements Initializable {

	@FXML
	private Label settingLabel;
	
	@FXML
	private Label startGameStateLabel;
	
	@FXML
	private Label noSettings;
	
	@FXML
	private GridPane settingPane;
	
	@FXML
	private ComboBox<String> startGameState;
	
	private GameSettingsModel model;
	
	/**
	 * 
	 * @param model A @see GameSettingsModel to bind to.
	 */
	public GameSettings(GameSettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameSettings.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		applySettingState(model.gameLoaded().get());
		noSettings.textProperty().bind(model.noSettingsText());
		settingLabel.textProperty().bind(model.settingText());
		startGameStateLabel.textProperty().bind(model.startGameStateText());
		startGameState.setItems(model.firstGameState().list());
		startGameState.valueProperty().bindBidirectional(model.firstGameState().selected());
	}

	private void applySettingState(boolean loaded) {
		if (loaded) {
			noSettings.setVisible(false);
			settingPane.setVisible(true);
		} else {
			noSettings.setVisible(true);
			settingPane.setVisible(false);
		}
	}
}
