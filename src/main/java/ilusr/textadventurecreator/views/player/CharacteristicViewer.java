package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import textadventurelib.persistence.player.NamedPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CharacteristicViewer extends PlayerDataView implements Initializable {

	@FXML
	private ComboBox<String> type;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField description;
	
	@FXML
	private TextField value;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private Label valueLabel;
	
	private NamedPersistenceObjectModel characteristic;
	private SimpleObjectProperty<NamedPersistenceObject> data;
	private LanguageAwareString nameText;
	private LanguageAwareString descriptionText;
	private LanguageAwareString valueText;
	
	/**
	 * 
	 * @param characteristic A characteristic to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CharacteristicViewer(NamedPersistenceObjectModel characteristic, ILanguageService languageService) {
		this.characteristic = characteristic;
		data = new SimpleObjectProperty<NamedPersistenceObject>(characteristic.getPersistence());
		nameText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTIC_NAME);
		descriptionText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTIC_DESCRIPTION);
		valueText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTIC_VALUE);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacteristicViewer.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception exception) {
			LogRunner.logger().severe(exception);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		type.getItems().addAll(characteristic.types().list());
		type.setValue(characteristic.types().selected().get());
		
		type.valueProperty().addListener((v, o, n) -> {
			characteristic.types().selected().set(n);
		});
		
		name.textProperty().bindBidirectional(characteristic.name());
		description.textProperty().bindBidirectional(characteristic.description());
		value.textProperty().bindBidirectional(characteristic.value());
		nameLabel.textProperty().bind(nameText);
		descriptionLabel.textProperty().bind(descriptionText);
		valueLabel.textProperty().bind(valueText);
	}

	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return data;
	}
}
