package ilusr.textadventurecreator.views.trigger;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedTriggerView extends BaseTriggerView implements Initializable{

	@FXML
	private TextArea script;
	
	@FXML
	private Button editor;
	
	@FXML
	private Label scriptLabel;
	
	private ScriptedTriggerModel model;
	
	/**
	 * 
	 * @param model The model to bind to.
	 */
	public ScriptedTriggerView(ScriptedTriggerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ScriptedTriggerView.fxml"));
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
		script.textProperty().bindBidirectional(model.script());
		scriptLabel.textProperty().bind(model.scriptText());
		
		editor.setOnAction((e) -> {
			model.showScriptInEditor();
		});
		editor.textProperty().bind(model.editorText());
	}

	@Override
	public TriggerPersistenceObject triggerPersistenceObject() {
		return model.getPersistenceObject();
	}

}
