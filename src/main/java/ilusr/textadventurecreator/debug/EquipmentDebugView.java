package ilusr.textadventurecreator.debug;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EquipmentDebugView extends AnchorPane implements Initializable {

	@FXML
	private VBox content;
	
	@FXML
	private TitledPane pane;
	
	private EquipDebugModel model;
	private String lastClass;
	private DebugItemView lastItem;
	
	/**
	 * 
	 * @param model A @see EquipDebugModel to associate to this view.
	 */
	public EquipmentDebugView(EquipDebugModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EquipDebugView.fxml"));
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
		this.getStylesheets().add(getClass().getResource("EquipmentDebugView.css").toExternalForm());
		pane.textProperty().bind(model.displayName());
		
		content.getChildren().add(new BodyPartDebugView(model.bodyPart()));
		
		lastItem = new DebugItemView(model.item().get());
		content.getChildren().add(lastItem);
		
		pane.setExpanded(false);
		
		objectChanged(model.changed().get(), model.added().get(), model.removed().get());
		
		model.added().addListener((v, o, n) -> {
			objectChanged(false, n, false);
		});
		
		model.removed().addListener((v, o, n) -> {
			objectChanged(false, false, n);
		});
		
		model.item().addListener((v, o, n) -> {
			content.getChildren().remove(lastItem);
			lastItem = new DebugItemView(n);
			content.getChildren().add(lastItem);
		});
		
		model.changed().addListener((v, o, n) -> {
			objectChanged(n, false, false);
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
