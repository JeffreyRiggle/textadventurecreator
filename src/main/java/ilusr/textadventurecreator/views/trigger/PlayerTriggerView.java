package ilusr.textadventurecreator.views.trigger;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerTriggerView extends BaseTriggerView implements Initializable {

	@FXML
	private ComboBox<String> player;
	
	@FXML
	private ComboBox<String> id;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private Label additionalIdLabel;
	
	@FXML
	private ComboBox<String> additionalId;
	
	@FXML
	private ComboBox<String> dataMember;
	
	@FXML
	private Label dataMemberLabel;
	
	@FXML
	private ComboBox<String> additionalDataMember;
	
	@FXML
	private Label additionalDataMemberLabel;
	
	@FXML
	private ComboBox<String> property;
	
	@FXML
	private ComboBox<String> condition;
	
	@FXML
	private TextField expectedValue;
	
	@FXML
	private GridPane pane;
	
	@FXML
	private Label playerLabel;
	
	@FXML
	private Label propertyLabel;
	
	@FXML
	private Label conditionLabel;
	
	@FXML
	private Label expectedValueLabel;
	
	private PlayerTriggerModel model;
	
	/**
	 * 
	 * @param model The model to bind to.
	 */
	public PlayerTriggerView(PlayerTriggerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerTriggerView.fxml"));
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
		playerLabel.textProperty().bind(model.playerText());
		propertyLabel.textProperty().bind(model.propertyText());
		idLabel.textProperty().bind(model.idText());
		additionalIdLabel.textProperty().bind(model.additionalIdText());
		dataMemberLabel.textProperty().bind(model.dataMemberText());
		additionalDataMemberLabel.textProperty().bind(model.additionalDataMemberText());
		conditionLabel.textProperty().bind(model.conditionText());
		expectedValueLabel.textProperty().bind(model.expectedValueText());
		
		toggleAdditionalIds();
		toggleIdAndDataMember();
		
		player.setItems(model.players().list());
		player.valueProperty().addListener((v, o, n) -> {
			model.players().selected().set(n);
		});
		player.setValue(model.players().selected().get());
		
		property.setItems(model.properties().list());
		property.valueProperty().addListener((v, o, n) -> {
			model.properties().selected().set(n);
			toggleIdAndDataMember();
		});
		property.setValue(model.properties().selected().get());
		
		id.setItems(model.ids().list());
		id.valueProperty().addListener((v, o, n) -> {
			model.ids().selected().set(n);
			toggleAdditionalIds();
		});
		id.setValue(model.ids().selected().get());
		
		additionalId.setItems(model.additionalIds().list());
		additionalId.setValue(model.additionalIds().selected().get());
		additionalId.valueProperty().addListener((v, o, n) -> {
			model.additionalIds().selected().set(n);
		});
		
		dataMember.setItems(model.dataMembers().list());
		dataMember.valueProperty().addListener((v, o, n) -> {
			model.dataMembers().selected().set(n);
			toggleAdditionalIds();
		});
		dataMember.setValue(model.dataMembers().selected().get());
		
		additionalDataMember.setItems(model.additionalDataMembers().list());
		if (model.additionalIds().selected().get() != null) {
			additionalDataMember.setValue(model.additionalDataMembers().selected().get());
		}
		additionalDataMember.valueProperty().addListener((v, o, n) -> {
			model.additionalDataMembers().selected().set(n);
		});
		
		condition.setItems(model.conditions().list());
		condition.setValue(model.conditions().selected().get());
		condition.valueProperty().addListener((v, o, n) -> {
			model.conditions().selected().set(n);
		});
		
		expectedValue.textProperty().bindBidirectional(model.expectedValue());
	}

	private void toggleIdAndDataMember() {
		RowConstraints row1 = pane.getRowConstraints().get(2);
		RowConstraints row2 = pane.getRowConstraints().get(4);
		
		if (model.idAndDataMemberNeeded()) {
			id.setVisible(true);
			idLabel.setVisible(true);
			row1.setMaxHeight(50);
			row1.setPrefHeight(50);
			dataMember.setVisible(true);
			dataMemberLabel.setVisible(true);
			row2.setMaxHeight(50);
			row2.setPrefHeight(50);
			return;
		} 
		
		id.setVisible(false);
		idLabel.setVisible(false);
		row1.setMaxHeight(0);
		row1.setPrefHeight(0);
		dataMember.setVisible(false);
		dataMemberLabel.setVisible(false);
		row2.setMaxHeight(0);
		row2.setPrefHeight(0);
	}
	
	private void toggleAdditionalIds() {
		RowConstraints row1 = pane.getRowConstraints().get(3);
		RowConstraints row2 = pane.getRowConstraints().get(5);
		
		if (model.additionalIdsNeeded()) {
			additionalId.setVisible(true);
			additionalIdLabel.setVisible(true);
			row1.setMaxHeight(50);
			row1.setPrefHeight(50);
			additionalDataMember.setVisible(true);
			additionalDataMemberLabel.setVisible(true);
			row2.setMaxHeight(50);
			row2.setPrefHeight(50);
			return;
		} 
		
		additionalId.setVisible(false);
		additionalIdLabel.setVisible(false);
		row1.setMaxHeight(0);
		row1.setPrefHeight(0);
		additionalDataMember.setVisible(false);
		additionalDataMemberLabel.setVisible(false);
		row2.setMaxHeight(0);
		row2.setPrefHeight(0);
	}

	@Override
	public TriggerPersistenceObject triggerPersistenceObject() {
		return model.persistenceObject();
	}
}
