package ilusr.textadventurecreator.style;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ThemeSettings extends AnchorPane implements Initializable {

	@FXML
	private Label settingLabel;
	
	@FXML
	private Label selectedThemeLabel;
	
	@FXML
	private ComboBox<String> themes;
	
	@FXML
	private Button apply;
	
	private ThemeSettingsModel model;
	
	public ThemeSettings(ThemeSettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ThemeSettings.fxml"));
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
		settingLabel.textProperty().bind(model.themeSettingsText());
		selectedThemeLabel.textProperty().bind(model.selectedText());
		apply.textProperty().bind(model.applyText());
		
		themes.setItems(model.selectedTheme().list());
		themes.valueProperty().bindBidirectional(model.selectedTheme().selected());
		
		apply.setOnAction(e -> {
			model.apply();
		});
	}

}
