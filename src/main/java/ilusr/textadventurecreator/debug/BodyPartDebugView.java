package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartDebugView extends AnchorPane implements Initializable {

	@FXML
	private TitledPane pane;
	
	@FXML
	private Label description;
	
	@FXML
	private VBox characteristicArea;
	
	@FXML
	private TitledPane characteristicPane;
	
	private BodyPartDebugModel model;
	private String lastClass;
	private Map<DebugNamedObjectModel, DebugNamedObject> characteristics;
	
	/**
	 * 
	 * @param model A @see BodyPartDebugModel to bind this view to.
	 */
	public BodyPartDebugView(BodyPartDebugModel model) {
		this.model = model;
		characteristics = new HashMap<DebugNamedObjectModel, DebugNamedObject>();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BodyPartDebugView.fxml"));
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
		this.getStylesheets().add(getClass().getResource("BodyPartDebugView.css").toExternalForm());
		pane.textProperty().bind(model.name());
		characteristicPane.textProperty().bind(model.charTitle());
		
		description.textProperty().bind(model.description());
		addCharacteristics(model.charactersitics());
		
		model.charactersitics().addListener((Change<? extends DebugNamedObjectModel>c) -> {
			if (!c.next()) {
				return;
			}
			
			addCharacteristics(c.getAddedSubList());
			removeCharacteristics(c.getRemoved());
		});
		
		pane.expandedProperty().set(false);
		
		objectChanged(model.changed().get(), model.added().get(), model.removed().get());
		
		model.removed().addListener((v, o, n) -> {
			objectChanged(false, false, n);
		});
		
		model.changed().addListener((v, o, n) -> {
			objectChanged(n, false, false);
		});
		
		model.added().addListener((v, o, n) -> {
			objectChanged(false, n, false);
		});
	}
	
	private void objectChanged(boolean updated, boolean added, boolean removed) {
		if (updated) {
			lastClass = "changed";
			pane.getStyleClass().add(lastClass);
			return;
		} else if (added) {
			lastClass = "added";
			pane.getStyleClass().add(lastClass);
			return;
		} else if (removed) {
			lastClass = "removed";
			pane.getStyleClass().add(lastClass);
			return;
		}
		
		if (lastClass != null && !lastClass.isEmpty()) {
			pane.getStyleClass().remove(lastClass);
			this.applyCss();
		}
	}
	
	private void addCharacteristics(List<? extends DebugNamedObjectModel> chars) {
		for (DebugNamedObjectModel characteristic : chars) {
			if (characteristics.containsKey(characteristic)) {
				continue;
			}
			
			DebugNamedObject charView = new DebugNamedObject(characteristic);
			characteristics.put(characteristic, charView);
			characteristicArea.getChildren().add(charView);
		}
	}
	
	private void removeCharacteristics(List<? extends DebugNamedObjectModel> chars) {
		for (DebugNamedObjectModel character : chars) {
			if (!characteristics.containsKey(character)) {
				continue;
			}
			
			characteristicArea.getChildren().remove(characteristics.get(character));
			characteristics.remove(character);
		}
	}
}
