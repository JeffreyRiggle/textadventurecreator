package ilusr.textadventurecreator.views.trigger;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.textadventurecreator.views.EditRemoveListCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MultiTriggerView extends BaseTriggerView implements Initializable {

	@FXML
	private ListView<TriggerPersistenceObject> triggers;
	
	@FXML
	private Label triggersLabel;
	
	private MultiTriggerModel model;
	
	/**
	 * 
	 * @param model A @see MultiTriggerModel to bind to.
	 */
	public MultiTriggerView(MultiTriggerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiTriggerView.fxml"));
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
		triggersLabel.textProperty().bind(model.triggersText());
		triggers.setCellFactory(new EditRemoveListCellFactory<TriggerPersistenceObject>(model.addTriggerKey(), model.getEditTriggerAction(), model.getAddTriggerAction(), new TriggerViewCreator()));
		triggers.itemsProperty().get().add(model.addTriggerKey());
		ObservableListBinder<TriggerPersistenceObject> triggerBinder = new ObservableListBinder<TriggerPersistenceObject>(model.triggers(), triggers.getItems());
		triggerBinder.bindSourceToTarget();
	}

	@Override
	public TriggerPersistenceObject triggerPersistenceObject() {
		return model.getPersistenceObject();
	}

}
