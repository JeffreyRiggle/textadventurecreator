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
public class GeneralSettings extends AnchorPane implements Initializable {

	@FXML
	private CheckBox hideLanding;
	
	@FXML
	private Label settingLabel;
	
	@FXML
	private Label showGreeting;
	
	@FXML
	private Label layoutLabel;
	
	@FXML
	private CheckBox includeLayout;
	
	private GeneralSettingsModel model;
	
	/**
	 * 
	 * @param model A @see GeneralSettingsModel to bind to.
	 */
	public GeneralSettings(GeneralSettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GeneralSettings.fxml"));
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
		hideLanding.selectedProperty().bindBidirectional(model.hideLanding());
		settingLabel.textProperty().bind(model.settingText());
		showGreeting.textProperty().bind(model.showGreetingText());
		includeLayout.selectedProperty().bindBidirectional(model.includeLayout());
		layoutLabel.textProperty().bind(model.layoutText());
	}
}
