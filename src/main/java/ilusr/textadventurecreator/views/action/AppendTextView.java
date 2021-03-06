package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AppendTextView extends BaseActionView implements Initializable{

	@FXML
	private TextArea appendText;
	
	@FXML
	private Label appendTextLabel;
	
	private AppendTextModel model;
	
	/**
	 * 
	 * @param model A @see AppendTextModel to bind to.
	 */
	public AppendTextView(AppendTextModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AppendTextView.fxml"));
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
		appendText.textProperty().bindBidirectional(model.appendText());
		appendTextLabel.textProperty().bind(model.labelText());
	}

	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}
}
