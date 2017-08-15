package ilusr.textadventurecreator.views.trigger;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextTriggerView extends BaseTriggerView implements Initializable {

	@FXML
	private TextField textValue;
	
	@FXML
	private ComboBox<String> matchTypes;
	
	@FXML
	private ToggleGroup casing;
	
	@FXML
	private RadioButton yes;
	
	@FXML
	private RadioButton no;
	
	@FXML
	private Label textLabel;
	
	@FXML
	private Label caseLabel;
	
	@FXML
	private Label typeLabel;
	
	private TextTriggerModel model;
	
	public TextTriggerView(TextTriggerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TextTriggerView.fxml"));
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
		textLabel.textProperty().bind(model.textText());
		caseLabel.textProperty().bind(model.caseText());
		yes.textProperty().bind(model.yesText());
		no.textProperty().bind(model.noText());
		typeLabel.textProperty().bind(model.typeText());
		
		textValue.textProperty().bindBidirectional(model.text());
		matchTypes.itemsProperty().set(model.matchType().list());
		matchTypes.valueProperty().addListener((v, o, n) -> {
			model.matchType().selected().set(n);
		});
		matchTypes.setValue(model.matchType().selected().get());
		
		yes.setUserData(true);
		no.setUserData(false);
		
		if (model.caseSensitive().get()) {
			yes.setSelected(true);
		} else {
			no.setSelected(true);
		}
		
		casing.selectedToggleProperty().addListener((v, o, n) -> {
			if (n == null) {
				model.caseSensitive().set(false);
				return;
			}
			
			model.caseSensitive().set((Boolean)n.getUserData());
		});
	}

	@Override
	public TriggerPersistenceObject triggerPersistenceObject() {
		return model.persistenceObject();
	}
}
