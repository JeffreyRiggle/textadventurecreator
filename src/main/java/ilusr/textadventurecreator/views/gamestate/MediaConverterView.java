package ilusr.textadventurecreator.views.gamestate;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MediaConverterView extends AnchorPane implements Initializable {
	
	@FXML
	private TextField newFile;
	
	@FXML
	private ComboBox<String> type;
	
	@FXML
	private Label result;
	
	@FXML
	private Button convert;
	
	@FXML
	private Label mediaTitle;
	
	@FXML
	private Label typeLabel;
	
	@FXML
	private Label newNameLabel;
	
	@FXML
	private ProgressBar convertingBar;
	
	private MediaConverterModel model;
	
	/**
	 * 
	 * @param model The @see MediaConverterModel to bind to.
	 */
	public MediaConverterView(MediaConverterModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MediaConverterView.fxml"));
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
		convertingBar.setVisible(false);
		convertingBar.setProgress(-1);
		newFile.textProperty().bindBidirectional(model.newFile());
		mediaTitle.textProperty().bind(model.titleText());
		typeLabel.textProperty().bind(model.typeText());
		newNameLabel.textProperty().bind(model.newFileText());
		
		type.setItems(model.types().list());
		type.valueProperty().addListener((v, o, n) -> {
			model.types().selected().set(n);
		});
		
		model.converting().addListener((v, o, n) -> {
			if (n) {
				convertingBar.setVisible(true);
				result.setVisible(false);
				convert.setDisable(true);
			} else {
				convertingBar.setVisible(false);
				result.setVisible(true);
				convert.setDisable(false);
			}
		});
		
		convert.setOnAction((e) -> {
			result.textProperty().unbind();
			
			model.convert((v) -> {
				updateResult(v);
			});

		});
		convert.textProperty().bind(model.convertText());
	}

	private void updateResult(boolean valid) {
		Platform.runLater(() -> {
			if (valid) {
				result.textProperty().bind(model.okText());
			} else {
				result.textProperty().bind(model.errorText());				
			}
		});
	}
}
