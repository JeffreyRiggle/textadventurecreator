package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.ResourceBundle;

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
public class DebugItemView extends AnchorPane implements Initializable {

	@FXML
	private TitledPane pane;
	
	@FXML
	private Label description;
	
	@FXML
	private VBox propertiesArea;
	
	@FXML
	private TitledPane propertiesPane;
	
	private DebugItemModel model;
	private String lastClass;
	
	/**
	 * 
	 * @param model A @see DebugItemModel to associate with this view.
	 */
	public DebugItemView(DebugItemModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DebugItemView.fxml"));
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
		this.getStylesheets().add(getClass().getResource("DebugItemView.css").toExternalForm());
		pane.textProperty().bind(model.name());
		description.textProperty().bind(model.description());
		propertiesPane.textProperty().bind(model.propTitle());
		
		for (DebugNamedObjectModel dModel : model.properties()) {
			propertiesArea.getChildren().add(new DebugNamedObject(dModel));
		}
		
		pane.setExpanded(false);
		objectChanged(false, model.added().get(), model.removed().get());
		
		model.removed().addListener((v, o, n) -> {
			objectChanged(false, false, n);
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
}
