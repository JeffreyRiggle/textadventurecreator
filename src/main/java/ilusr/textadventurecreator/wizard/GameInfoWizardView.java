package ilusr.textadventurecreator.wizard;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameInfoWizardView extends GameWizardView implements Initializable{

	@FXML
	private TextField gameName;
	
	@FXML
	private TextArea gameDescription;
	
	@FXML
	private TextField iconLocation;
	
	@FXML
	private Button iconButton;
	
	@FXML
	private TextField creator;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private Label iconLabel;
	
	@FXML
	private Label creatorLabel;
	
	private GameSettingsModel model;
	private boolean initialized;
	
	/**
	 * Creates a game info wizard view.
	 */
	public GameInfoWizardView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameInfoWizardView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setModel(GameSettingsModel model) {
		if (!initialized) {
			return;
		}
		
		if (this.model != null) {
			unbind();
		}
		
		this.model = model;
		
		if (this.model != null) {
			bind();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (this.model != null) {
			bind();
		}
		
		iconButton.setOnAction((e) -> {
			FileChooser chooser = new FileChooser();
			File content = chooser.showOpenDialog(super.sceneProperty().get().windowProperty().get());
			
			if (content != null) {
				iconLocation.textProperty().set(content.getAbsolutePath());
			}
		});
		
		initialized = true;
	}

	private void bind() {
		gameName.textProperty().bindBidirectional(model.gameName());
		gameDescription.textProperty().bindBidirectional(model.gameDescription());
		iconLocation.textProperty().bindBidirectional(model.iconLocation());
		creator.textProperty().bindBidirectional(model.creator());
		nameLabel.textProperty().bind(model.gameNameText());
		descriptionLabel.textProperty().bind(model.gameDescriptionText());
		iconLabel.textProperty().bind(model.gameIconText());
		iconButton.textProperty().bind(model.browseText());
		creatorLabel.textProperty().bind(model.creatorText());
		
		super.valid().bind(model.gameInfoValid());
	}
	
	private void unbind() {
		gameName.textProperty().unbind();
		gameDescription.textProperty().unbind();
		iconLocation.textProperty().unbind();
		creator.textProperty().unbind();
		nameLabel.textProperty().unbind();
		descriptionLabel.textProperty().unbind();
		iconLabel.textProperty().unbind();
		iconButton.textProperty().unbind();
		creatorLabel.textProperty().unbind();
	}
}
