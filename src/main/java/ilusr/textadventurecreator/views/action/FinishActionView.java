package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class FinishActionView extends BaseActionView implements Initializable {

	@FXML
	private Label finishText;
	
	private FinishActionModel model;

	/**
	 * 
	 * @param model A @see FinishActionModel to bind to.
	 */
	public FinishActionView(FinishActionModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FinishActionView.fxml"));
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
		finishText.textProperty().bind(model.finishText());
	}
	
	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}
}
