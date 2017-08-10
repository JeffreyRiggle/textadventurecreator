package ilusr.textadventurecreator.settings;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibrarySettings extends AnchorPane implements Initializable {

	@FXML
	private CheckBox globalLibrary;
	
	@FXML
	private CheckBox gameLibrary;
	
	@FXML
	private Label settingsLabel;
	
	@FXML
	private Label globalLabel;
	
	@FXML
	private Label individualLabel;
	
	private LibrarySettingsModel model;
	
	/**
	 * 
	 * @param model A @see LibrarySettingsModel to bind to.
	 */
	public LibrarySettings(LibrarySettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LibrarySettings.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		globalLibrary.selectedProperty().bindBidirectional(model.globalLibrary());
		
		gameLibrary.setDisable(globalLibrary.selectedProperty().get());
		
		globalLibrary.selectedProperty().addListener((v, o, n) -> {
			gameLibrary.setDisable(n);
		});
		
		gameLibrary.selectedProperty().bindBidirectional(model.gameLibrary());
		settingsLabel.textProperty().bindBidirectional(model.settingText());
		globalLabel.textProperty().bindBidirectional(model.globalText());
		individualLabel.textProperty().bind(model.individualText());
	}
}
