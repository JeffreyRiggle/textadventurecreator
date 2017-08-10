package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionActionView extends BaseActionView implements Initializable {

	@FXML
	private TextField completionData;
	
	@FXML
	private Label stateLabel;
	
	private CompletionActionModel model;
	
	/**
	 * 
	 * @param model A @see CompletionActionModel to bind to.
	 */
	public CompletionActionView(CompletionActionModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CompletionActionView.fxml"));
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
		completionData.textProperty().bindBidirectional(model.completionData());
		stateLabel.textProperty().bind(model.stateText());
	}

	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}

}
