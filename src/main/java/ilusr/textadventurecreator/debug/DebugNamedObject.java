package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugNamedObject extends AnchorPane implements Initializable {

	@FXML
	private Label name;
	
	@FXML
	private Label value;
	
	private DebugNamedObjectModel model;
	private String lastClass;
	
	/**
	 * 
	 * @param model A @see DebugNamedObjectModel to associate with this view.
	 */
	public DebugNamedObject(DebugNamedObjectModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DebugNamedObject.fxml"));
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
		this.getStylesheets().add(getClass().getResource("DebugNamedObject.css").toExternalForm());
		name.textProperty().bind(model.name());
		
		Tooltip description = new Tooltip();
		description.textProperty().bind(model.description());
		
		name.tooltipProperty().set(description);
		
		value.textProperty().bind(model.value());
		
		model.changed().addListener((v, o, n) -> {
			objectChanged(n, false, false);
		});
		
		if (model.added().get()) {
			objectChanged(false, true, false);
		}
		
		model.added().addListener((v, o, n) -> {
			objectChanged(false, n, false);
		});
		
		model.removed().addListener((v, o, n) -> {
			objectChanged(false, false, n);
		});
	}
	
	private void objectChanged(boolean updated, boolean added, boolean removed) {
		if (updated) {
			lastClass = "changed";
			name.getStyleClass().add(lastClass);
			value.getStyleClass().add(lastClass);
			return;
		} else if (added) {
			lastClass = "added";
			name.getStyleClass().add(lastClass);
			value.getStyleClass().add(lastClass);
			return;
		} else if (removed) {
			lastClass = "removed";
			name.getStyleClass().add(lastClass);
			value.getStyleClass().add(lastClass);
			return;
		}
		
		if (lastClass != null && !lastClass.isEmpty()) {
			name.getStyleClass().remove(lastClass);
			value.getStyleClass().remove(lastClass);
		}
	}
}
