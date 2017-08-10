package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedActionView extends BaseActionView implements Initializable {

	@FXML
	private TextArea script;
	
	@FXML
	private Button editor;
	
	@FXML
	private Label actionLabel;
	
	private ScriptedActionModel model;
	
	/**
	 * 
	 * @param model The @see ScriptedActionModel to bind to.
	 */
	public ScriptedActionView(ScriptedActionModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ScriptedActionView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		script.textProperty().bindBidirectional(model.script());
		
		editor.setOnAction((e) -> {
			model.showScriptInEditor();
		});
		
		actionLabel.textProperty().bind(model.actionText());
		editor.textProperty().bind(model.viewText());
	}
	
	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}

}
