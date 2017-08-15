package ilusr.textadventurecreator.views.layout;

import java.net.URL;
import java.util.Map.Entry;

import ilusr.logrunner.LogRunner;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

//TODO validation textbox.
/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutPropertiesView extends AnchorPane implements Initializable {

	@FXML
	private Label rowLabel;
	
	@FXML
	private TextField rows;
	
	@FXML
	private Label colLabel;
	
	@FXML
	private TextField columns;
	
	@FXML
	private GridPane additionalProperties;
	
	private LayoutCreatorModel model;
	private boolean initialized;
	
	/**
	 * Creates a new layout properties view.
	 */
	public LayoutPropertiesView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutPropertiesView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialized = true;
		setModel(model);
	}
	
	/**
	 * 
	 * @param model The new @see LayoutCreatorModel to use.
	 */
	public void setModel(LayoutCreatorModel model) {
		if (!initialized) {
			this.model = model;
			return;
		}
		
		if (this.model != null) {
			rows.textProperty().unbind();
			columns.textProperty().unbind();
		}
		
		this.model = model;
		
		if (this.model == null) {
			return;
		}
		
		rowLabel.textProperty().bind(model.rowText());
		rows.textProperty().set(Integer.toString(model.rows().get()));
		rows.textProperty().addListener((v, o, n) -> {
			try {
				int val = Integer.parseInt(n);
				model.rows().set(val);
			} catch (Exception e) {
				
			}
		});
		
		colLabel.textProperty().bind(model.colText());
		columns.textProperty().set(Integer.toString(model.columns().get()));
		columns.textProperty().addListener((v, o, n) -> {
			try {
				int val = Integer.parseInt(n);
				model.columns().set(val);
			} catch (Exception e) {
				
			}
		});
		
		model.selectedComponent().addListener((v, o, n) -> {
			updateProperties(n);
		});
	}
	
	private void updateProperties(LayoutComponent component) {
		additionalProperties.getChildren().clear();
		
		int iter = 0;
		for (Entry<String, String> prop : component.node().getProperties().entrySet()) {
			additionalProperties.add(new Label(prop.getKey()), 0, iter);
			TextField value = new TextField(prop.getValue());
			value.textProperty().addListener((v, o, n) -> {
				component.node().addProperty(prop.getKey(), n);
			});
			additionalProperties.add(value, 2, iter);
			iter++;
		}
	}
}
