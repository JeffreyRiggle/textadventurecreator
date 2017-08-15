package ilusr.textadventurecreator.wizard;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class HostedGameWizardView extends GameWizardView implements Initializable {

	@FXML
	private ComboBox<String> type;
	
	@FXML
	private TextField mediaLocation;
	
	@FXML
	private Button mediaButton;
	
	@FXML
	private Label transitionType;
	
	@FXML
	private Label media;
	
	private GameSettingsModel model;
	private boolean initialized;
	
	/**
	 * Creates a hosted game view.
	 */
	public HostedGameWizardView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HostedGameWizardView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialized = true;
		
		if (this.model != null) {
			bindModel();
		}
	}

	@Override
	public void setModel(GameSettingsModel model) {
		if (this.model != null && initialized) {
			unBindModel();
		}
		
		this.model = model;
		
		if (this.model != null && initialized) {
			bindModel();
		}
	}
	
	private void bindModel() {
		transitionType.textProperty().bind(model.transitionTypeText());
		media.textProperty().bind(model.mediaText());
		mediaButton.textProperty().bind(model.browseText());
		
		mediaButton.setOnAction((e) -> {
			model.browseContent(super.getScene().getWindow());
		});
		
		type.setItems(model.transitionTypes().list());
		type.setValue(model.transitionTypes().selected().get());
		type.valueProperty().addListener((v, o, n) -> {
			model.transitionTypes().selected().set(n);
		});
		
		mediaLocation.textProperty().bindBidirectional(model.mediaLocation());
	}
	
	private void unBindModel() {
		mediaLocation.textProperty().unbind();
		transitionType.textProperty().unbind();
		media.textProperty().unbind();
		mediaButton.textProperty().unbind();
	}
}
