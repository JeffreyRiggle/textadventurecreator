package ilusr.textadventurecreator.wizard;

import java.io.File;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StandAloneGameWizardView extends GameWizardView implements Initializable {

	@FXML
	private ComboBox<String> language;
	
	@FXML
	private Label languageLabel;
	
	@FXML
	private TextField location;
	
	@FXML
	private Button browse;
	
	@FXML
	private TextField background;
	
	@FXML
	private Button browseBackground;
	
	@FXML
	private Label locationLabel;
	
	@FXML
	private Label backgroundLabel;
	
	private GameSettingsModel model;
	private boolean initialized;
	
	/**
	 * Creates a stand alone view.
	 */
	public StandAloneGameWizardView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StandAloneGameWizardView.fxml"));
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
		initialized = true;
		if (this.model != null) {
			bind();
		}
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
		locationLabel.textProperty().bind(model.projectLocationText());
		browse.textProperty().bind(model.browseText());
		backgroundLabel.textProperty().bind(model.backgroundText());
		browseBackground.textProperty().bind(model.browseText());
		languageLabel.textProperty().bind(model.languageText());
		
		language.setItems(model.supportedLanguages().list());
		language.setValue(model.supportedLanguages().selected().get());
		language.valueProperty().addListener((v, o, n) -> {
			model.supportedLanguages().selected().set(n);
		});
		
		model.isDev().addListener((v, o, n) -> {
			language.setDisable(!n);
			languageLabel.setDisable(!n);
		});
		
		location.textProperty().bindBidirectional(model.projectLocation());
		
		background.textProperty().bindBidirectional(model.backgroundLocation());
		
		browse.setOnAction((e) -> {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File file = chooser.showDialog(super.getScene().getWindow());
			
			if (file != null) {
				location.textProperty().set(file.getAbsolutePath());
			}
		});
		
		browseBackground.setOnAction((e) -> {
			FileChooser chooser = new FileChooser();
			File file = chooser.showOpenDialog(super.getScene().getWindow());
			
			if (file != null) {
				background.textProperty().set(file.getAbsolutePath());
			}
		});
	}
	
	private void unbind() {
		language.valueProperty().unbind();
		location.textProperty().unbind();
		locationLabel.textProperty().unbind();
		browse.textProperty().unbind();
		backgroundLabel.textProperty().unbind();
		browseBackground.textProperty().unbind();
		languageLabel.textProperty().unbind();
	}
}
