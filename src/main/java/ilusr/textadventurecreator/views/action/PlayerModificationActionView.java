package ilusr.textadventurecreator.views.action;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import ilusr.iroshell.core.IViewProvider;
import ilusr.iroshell.core.ViewSwitcher;
import ilusr.textadventurecreator.views.player.InventorySelector;
import ilusr.textadventurecreator.views.player.RemoveCharacteristicView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationActionView extends BaseActionView implements Initializable {

	@FXML
	private GridPane pane;
	
	@FXML
	private ComboBox<String> player;

	@FXML
	private ComboBox<String> property;

	@FXML
	private Label idLabel;
	
	@FXML
	private ComboBox<String> id;

	@FXML
	private Label dataMemberLabel;
	
	//TODO: I do not think this will be needed.
	@FXML
	private ComboBox<String> dataMember;

	@FXML
	private Label changeTypeLabel;
	
	@FXML
	private ComboBox<String> changeType;
	
	@FXML
	private ComboBox<String> modificationType;
	
	@FXML
	private ViewSwitcher<String> modificationData;
	
	@FXML
	private Label playerLabel;
	
	@FXML
	private Label propertyLabel;
	
	@FXML
	private Label modDataLabel;
	
	@FXML
	private Label modTypeLabel;
	
	private final Map<String, IViewProvider<PlayerDataView>> providers;
	private PlayerModificationActionModel model;
	
	/**
	 * 
	 * @param model A @see PlayerModificationActionModel to bind to.
	 * @param providers The providers to use for player changes.
	 */
	public PlayerModificationActionView(PlayerModificationActionModel model, Map<String, IViewProvider<PlayerDataView>> providers) {
		this.model = model;
		this.providers = providers;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerModificationActionView.fxml"));
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
		toggleChange();
		setupViewProvider();
		player.setItems(model.players().list());
		player.setValue(model.players().selected().get());
		player.valueProperty().addListener((v, o, n) -> {
			model.players().selected().set(n);
		});
		
		property.setItems(model.modificationObject().list());
		property.valueProperty().addListener((v, o, n) -> {
			model.modificationObject().selected().set(n);
			updateModData();
		});
		property.setValue(model.modificationObject().selected().get());
		
		id.setItems(model.id().list());
		id.setValue(model.id().selected().get());
		id.valueProperty().addListener((v, o, n) -> {
			model.id().selected().set(n);
			
			if (model.isBodyModification() && model.isChange()) {
				updateChangeData();
			}
		});
		
		changeType.setItems(model.changeTypes().list());
		changeType.setValue(model.changeTypes().selected().get());
		changeType.valueProperty().addListener((v, o, n) -> {
			model.changeTypes().selected().set(n);
			
			if (model.isBodyModification() && model.isChange()) {
				updateChangeData();
			}
		});
		
		modificationType.setItems(model.modifications().list());
		modificationType.valueProperty().addListener((v, o, n) -> {
			model.modifications().selected().set(n);
			toggleChange();
			updateModData();
		});
		modificationType.setValue(model.modifications().selected().get());
		
		playerLabel.textProperty().bind(model.playerText());
		propertyLabel.textProperty().bind(model.propertyText());
		idLabel.textProperty().bind(model.idText());
		dataMemberLabel.textProperty().bind(model.dataMemberText());
		modTypeLabel.textProperty().bind(model.modTypeText());
		changeTypeLabel.textProperty().bind(model.changeTypeText());
		modDataLabel.textProperty().bind(model.modDataText());
	}

	private void setupViewProvider() {
		for (Entry<String, IViewProvider<PlayerDataView>> e : providers.entrySet()) {
			modificationData.addView(e.getKey(), e.getValue());
		}
	}
	
	private void updateModData() {
		if (!model.isChange() && !model.isRemove() && !model.isAdd()) {
			return;
		}
		
		if (model.isChange()) {
			updateChangeData();
			return;
		}
		
		if (model.isRemove()) {
			modificationData.switchView("Remove");
			return;
		}
		
		if (model.isEquipmentModification()) {
			modificationData.switchView("Equipment");
			InventorySelector view = (InventorySelector)providers.get("Equipment").getView();
			view.setInventory(model.getCurrentInventory());
			model.modificationData().bind(providers.get("Equipment").getView().getData());
			return;
		}
		
		String view = model.modificationObject().selected().get();
		modificationData.switchView(view);
		model.modificationData().set(providers.get(view).getView().getData().get());
	}
	
	private void updateChangeData() {
		if (!model.isBodyModification()) {
			modificationData.switchView("Change");
			model.modificationData().bind(providers.get("Change").getView().getData());
			return;
		}
		
		if (model.isAddChange()) {
			modificationData.switchView("Characteristic");
			model.modificationData().bind(providers.get("Characteristic").getView().getData());
		} else if (model.isAssign()) {
			modificationData.switchView("BodyPart");
			model.modificationData().bind(providers.get("BodyPart").getView().getData());
		} else if (model.isSubtract()){
			modificationData.switchView("RemoveCharacteristic");
			RemoveCharacteristicView view = (RemoveCharacteristicView)providers.get("RemoveCharacteristic").getView();
			view.setBodyPart(model.getCurrentBodyPart());
			model.modificationData().bind(providers.get("RemoveCharacteristic").getView().getData());
		}
		
		
	}
	private void toggleChange() {
		RowConstraints row1 = pane.getRowConstraints().get(2);
		RowConstraints row2 = pane.getRowConstraints().get(3);
		RowConstraints row3 = pane.getRowConstraints().get(5);
		
		if (model.isChange()) {
			showAll(row1, row2, row3);
			return;
		}
		
		if (model.isRemove() || (model.isAdd() && model.isEquipmentModification())) {
			showAllButChange(row1, row2, row3);
			return;
		}
		
		hideAll(row1, row2, row3);
	}
	
	private void showAll(RowConstraints row1, RowConstraints row2, RowConstraints row3) {
		changeType.setVisible(true);
		changeTypeLabel.setVisible(true);
		row1.setMaxHeight(50);
		row1.setPrefHeight(50);
		id.setVisible(true);
		idLabel.setVisible(true);
		
		if (model.needsDataMember()) {
			dataMember.setVisible(true);
			dataMemberLabel.setVisible(true);
			row2.setMaxHeight(50);
			row2.setPrefHeight(50);
		} else {
			dataMember.setVisible(false);
			dataMemberLabel.setVisible(false);
			row2.setMaxHeight(0);
			row2.setPrefHeight(0);
		}
		
		row3.setMaxHeight(50);
		row3.setPrefHeight(50);
	}
	
	private void showAllButChange(RowConstraints row1, RowConstraints row2, RowConstraints row3) {
		changeType.setVisible(false);
		changeTypeLabel.setVisible(false);
		row1.setMaxHeight(50);
		row1.setPrefHeight(50);
		
		if (model.needsDataMember()) {
			dataMember.setVisible(true);
			dataMemberLabel.setVisible(true);
			row2.setMaxHeight(50);
			row2.setPrefHeight(50);
		} else {
			dataMember.setVisible(false);
			dataMemberLabel.setVisible(false);
			row2.setMaxHeight(0);
			row2.setPrefHeight(0);
		}
		
		id.setVisible(true);
		idLabel.setVisible(true);
		row3.setMaxHeight(0);
		row3.setPrefHeight(0);
	}
	
	private void hideAll(RowConstraints row1, RowConstraints row2, RowConstraints row3) {
		changeType.setVisible(false);
		changeTypeLabel.setVisible(false);
		row1.setMaxHeight(0);
		row1.setPrefHeight(0);
		id.setVisible(false);
		idLabel.setVisible(false);
		row2.setMaxHeight(0);
		row2.setPrefHeight(0);
		dataMember.setVisible(false);
		dataMemberLabel.setVisible(false);
		row3.setMaxHeight(0);
		row3.setPrefHeight(0);
	}
	
	@Override
	public ActionPersistenceObject getPersistenceObject() {
		return model.getPersistenceObject();
	}

}
