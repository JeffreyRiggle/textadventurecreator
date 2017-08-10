package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExecutionActionView extends BaseActionView implements Initializable {

	@FXML
	private TextField exe;
	
	@FXML
	private ToggleGroup blocking;
	
	@FXML
	private RadioButton yes;
	
	@FXML
	private RadioButton no;
	
	@FXML
	private Label exeLabel;
	
	@FXML
	private Label blockingLabel;
	
	private ExecutionActionModel model;
	
	/**
	 * 
	 * @param model A @see ExecutionActionModel to bind to.
	 */
	public ExecutionActionView(ExecutionActionModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ExecutionActionView.fxml"));
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
		exe.textProperty().bindBidirectional(model.executable());
		yes.setUserData(true);
		no.setUserData(false);
		
		if (model.blocking().get()) {
			yes.setSelected(true);
		} else {
			no.setSelected(true);
		}
		
		blocking.selectedToggleProperty().addListener((v, o, n) -> {
			if (n == null) {
				model.blocking().set(false);
				return;
			}
			
			model.blocking().set((Boolean)n.getUserData());
		});
		
		exeLabel.textProperty().bind(model.exeText());
		blockingLabel.textProperty().bind(model.blockingText());
		yes.textProperty().bind(model.yesText());
		no.textProperty().bind(model.noText());
	}

	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}

}
