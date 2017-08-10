package ilusr.textadventurecreator.wizard;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameConfigurationWizardView extends GameWizardView implements Initializable {

	@FXML
	private CheckBox inlinePlayer;
	
	@FXML
	private CheckBox inlineGame;
	
	@FXML
	private CheckBox inlineLayout;
	
	@FXML
	private CheckBox buffer;
	
	@FXML
	private Label sizeLabel;
	
	@FXML
	private TextField bufferSize;
	
	@FXML
	private TextField playersLocation;
	
	@FXML
	private TextField gameStateLocation;
	
	@FXML
	private TextField layoutLocation;
	
	@FXML
	private Button browsePlayer;
	
	@FXML
	private Button browseGameState;
	
	@FXML
	private Button browseLayout;
	
	private GameSettingsModel model;
	private boolean initialized;
	
	/**
	 * Creates a game configuration wizard view.
	 */
	public GameConfigurationWizardView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameConfigurationWizardView.fxml"));
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
		bufferSize.setDisable(true);
		sizeLabel.setDisable(true);
		
		buffer.selectedProperty().addListener((v, o, n) -> {
			bufferSize.setDisable(!n);
			sizeLabel.setDisable(!n);
		});
		
		inlinePlayer.selectedProperty().addListener((v, o, n) -> {
			playersLocation.setDisable(n);
			browsePlayer.setDisable(n);
		});
		
		browsePlayer.setOnAction(e -> {
			browse(playersLocation);
		});
		
		browseGameState.setOnAction(e -> {
			browse(gameStateLocation);
		});
		
		browseLayout.setOnAction(e -> {
			browse(layoutLocation);
		});
		
		inlineGame.selectedProperty().addListener((v, o, n) -> {
			gameStateLocation.setDisable(n);
			browseGameState.setDisable(n);
		});
		
		inlineLayout.selectedProperty().addListener((v, o, n) -> {
			layoutLocation.setDisable(n);
			browseLayout.setDisable(n);
		});
		
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
		inlineGame.selectedProperty().bindBidirectional(model.gameStatesInline());
		gameStateLocation.textProperty().bindBidirectional(model.gameStatesLocation());
		inlinePlayer.selectedProperty().bindBidirectional(model.playersInline());
		playersLocation.textProperty().bindBidirectional(model.playersLocation());
		inlinePlayer.textProperty().bind(model.inlinePlayerText());
		browsePlayer.textProperty().bind(model.browseText());
		inlineGame.textProperty().bind(model.inlineGameText());
		browseGameState.textProperty().bind(model.browseText());
		inlineLayout.selectedProperty().bindBidirectional(model.layoutInline());
		layoutLocation.textProperty().bindBidirectional(model.layoutLocation());
		inlineLayout.textProperty().bind(model.inlineLayoutText());
		browseLayout.textProperty().bind(model.browseText());
		buffer.textProperty().bind(model.bufferText());
		sizeLabel.textProperty().bind(model.sizeText());
		
		bufferSize.textProperty().addListener((v, o, n) -> {
			try {
				model.bufferSize().set(Integer.parseInt(n));
			} catch (Exception e) {
				//TODO validation.
			}
		});
	}
	
	private void unbind() {
		inlineGame.selectedProperty().unbind();
		inlinePlayer.selectedProperty().unbind();
		bufferSize.textProperty().unbind();
		inlinePlayer.textProperty().unbind();
		browsePlayer.textProperty().unbind();
		inlineGame.textProperty().unbind();
		browseGameState.textProperty().unbind();
		inlineLayout.selectedProperty().unbind();
		inlineLayout.textProperty().unbind();
		browseLayout.textProperty().unbind();
		buffer.textProperty().unbind();
		sizeLabel.textProperty().unbind();
	}

	private void browse(TextField field) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = chooser.showDialog(super.getScene().getWindow());
		
		if (file != null) {
			field.textProperty().set(file.getAbsolutePath());
		}
	}
}
