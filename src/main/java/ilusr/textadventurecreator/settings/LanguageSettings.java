package ilusr.textadventurecreator.settings;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LanguageSettings extends AnchorPane implements Initializable {

	@FXML
	private Label title;
	
	@FXML
	private Label langLabel;
	
	@FXML
	private ListView<String> languages;
	
	@FXML
	private Button addLanguage;
	
	@FXML
	private Button apply;
	
	@FXML
	private Button editLanguage;
	
	private LanguageSettingsModel model;
	
	/**
	 * 
	 * @param model A @see LanguageSettingsModel to bind to.
	 */
	public LanguageSettings(LanguageSettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LanguageSettings.fxml"));
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
		ObservableListBinder<String> binder = new ObservableListBinder<String>(model.languages().list(), languages.getItems());
		binder.bindSourceToTarget();
		
		title.textProperty().bind(model.titleText());
		langLabel.textProperty().bind(model.langText());
		
		languages.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
			model.languages().selected().set(n);
			editLanguage.setDisable(false);
		});
		
		apply.textProperty().bind(model.applyText());
		apply.setOnAction((e) -> {
			model.apply();
		});
		
		addLanguage.textProperty().bind(model.addText());
		addLanguage.setOnAction((e) -> {
			model.add();
		});
		
		editLanguage.textProperty().bind(model.editText());
		editLanguage.setOnAction((e) -> {
			model.edit();
		});
		editLanguage.setDisable(true);
	}
}
