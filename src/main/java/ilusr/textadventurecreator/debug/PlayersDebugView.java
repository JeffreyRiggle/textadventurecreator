package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayersDebugView extends AnchorPane implements Initializable {

	@FXML
	private TitledPane playersPane;
	
	@FXML
	private VBox playersArea;
	
	@FXML
	private Label noPlayers;
	
	private PlayersDebugModel model;
	private boolean initialized;
	
	/**
	 * 
	 */
	public PlayersDebugView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayersDebugView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		noPlayers.setVisible(true);
		playersPane.setVisible(false);
		
		if (model != null) {
			setup();
		}
		
		initialized = true;
	}
	
	/**
	 * 
	 * @param model A @see PlayersDebugModel to associate with this view.
	 */
	public void setModel(PlayersDebugModel model) {
		this.model = model;
		
		if (model != null && initialized) {
			setup();
		}
	}
	
	private void setup() {
		playersPane.textProperty().bind(model.playersText());
		noPlayers.textProperty().bind(model.noPlayersText());
		
		if (model.players().size() == 0) {
			noPlayers.setVisible(true);
			playersPane.setVisible(false);
			return;
		}
		
		noPlayers.setVisible(false);
		playersPane.setVisible(true);
		
		for (PlayerDebugModel dModel : model.players()) {
			playersArea.getChildren().add(new PlayerDebugView(dModel));
		}
	}
}
