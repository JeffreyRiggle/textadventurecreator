package ilusr.textadventurecreator.wizard;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameTypeWizardView extends GameWizardView implements Initializable {

	@FXML
	private CheckBox isDev;
	
	@FXML
	private ToggleGroup type;
	
	@FXML
	private RadioButton hosted;
	
	@FXML
	private RadioButton standAlone;
	
	@FXML
	private Label typeLabel;
	
	private GameSettingsModel model;
	private boolean initialized;
	
	/**
	 * Creates a game wizard view.
	 */
	public GameTypeWizardView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameTypeWizardView.fxml"));
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
		isDev.setDisable(true);
		hosted.setUserData(true);
		standAlone.setUserData(false);
		
		type.selectedToggleProperty().addListener((v, o, n) -> {
			if (n == null) {
				isDev.setDisable(true);
				return;
			}
			
			boolean hosted = (Boolean)n.getUserData();
			
			if (model != null) {
				model.standAlone().set(!hosted);
			}
			
			isDev.setDisable(hosted);
		});
		
		initialized = true;
	}

	@Override
	public void setModel(GameSettingsModel model) {
		
		if (this.model != null && initialized) {
			unbind();
		}
		
		this.model = model;
		
		if (this.model != null && initialized) {
			bind();
		}
	}

	private void bind() {
		typeLabel.textProperty().bind(model.gameTypeText());
		hosted.textProperty().bind(model.hostedText());
		standAlone.textProperty().bind(model.standAloneText());
		isDev.textProperty().bind(model.isDevText());
		
		isDev.selectedProperty().bindBidirectional(model.isDev());
		
		if (model.standAlone().get()) {
			standAlone.selectedProperty().set(true);
		} else {
			hosted.selectedProperty().set(true);
		}
	}
	
	private void unbind() {
		isDev.selectedProperty().unbind();
		typeLabel.textProperty().unbind();
		hosted.textProperty().unbind();
		standAlone.textProperty().unbind();
		isDev.textProperty().unbind();
	}
}
