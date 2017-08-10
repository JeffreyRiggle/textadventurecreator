package ilusr.textadventurecreator.settings;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModSettings extends AnchorPane implements Initializable {

	@FXML
	private Label modLabel;
	
	@FXML
	private Label enableModLabel;
	
	@FXML
	private CheckBox enableMods;
	
	@FXML
	private Label modsLabel;
	
	@FXML
	private ListView<ModSetting> mods;
	
	private ModSettingsModel model;
	
	/**
	 * 
	 * @param model A @see ModSettingsModel to bind to.
	 */
	public ModSettings(ModSettingsModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ModSettings.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		modLabel.textProperty().bind(model.settingText());
		enableModLabel.textProperty().bind(model.enableModText());
		enableMods.selectedProperty().bindBidirectional(model.enableMods());
		modsLabel.textProperty().bind(model.modsText());
		
		ObservableListBinder<ModSetting> binder = new ObservableListBinder<ModSetting>(model.mods(), mods.getItems());
		binder.bindSourceToTarget();
		
		mods.setCellFactory((f) -> {
			return new ModListCell();
		});
		
		updateModArea(model.enableMods().get());
		model.enableMods().addListener((v, o, n) -> {
			updateModArea(n);
		});
	}
	
	private void updateModArea(boolean active) {
		modsLabel.setDisable(!active);
		mods.setDisable(!active);
	}
	
	private class ModListCell extends ListCell<ModSetting> {
		private Button activateBtn;
		private Button deactivateBtn;
		
		public ModListCell() {
			
			activateBtn = new Button();
			activateBtn.getStylesheets().add(getClass().getResource("ActivateBtn.css").toExternalForm());
			deactivateBtn = new Button();
			deactivateBtn.getStylesheets().add(getClass().getResource("DeactivateBtn.css").toExternalForm());
		}
		
		@Override
		public void updateItem(ModSetting item, boolean empty) {
			super.updateItem(item, empty);
			
			if (empty || item == null) {
				setText("");
				setGraphic(null);
				return;
			}
			
			setStandardGraphic(item);
		}
		
		private void setStandardGraphic(ModSetting item) {
			Label label = new Label(item.name());
			label.setTooltip(new Tooltip(item.id()));

			activateBtn.setOnAction((e) -> { item.active().set(true); });
			deactivateBtn.setOnAction((e) -> { item.active().set(false); });
			
			AnchorPane root = new AnchorPane();
			GridPane grid = new GridPane();
			AnchorPane.setBottomAnchor(grid, 0.0);
			AnchorPane.setTopAnchor(grid, 0.0);
			AnchorPane.setLeftAnchor(grid, 0.0);
			AnchorPane.setRightAnchor(grid, 0.0);
			grid.getColumnConstraints().add(new ColumnConstraints(50, 50, Integer.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
			grid.getColumnConstraints().add(new ColumnConstraints(30, 50, Integer.MAX_VALUE, Priority.NEVER, HPos.CENTER, true));
			grid.add(label, 0, 0);
			StackPane stack = new StackPane();
			stack.getChildren().add(activateBtn);
			stack.getChildren().add(deactivateBtn);
			grid.add(stack, 1, 0);
			root.getChildren().add(grid);
			
			changeActivation(item.active().get());
			item.active().addListener((v, o, n) -> {
				changeActivation(n);
			});
			
			setGraphic(root);
		}
		
		private void changeActivation(boolean active) {
			deactivateBtn.setVisible(active);
			deactivateBtn.setDisable(!active);
			activateBtn.setVisible(!active);
			activateBtn.setDisable(active);
		}
	}
}
