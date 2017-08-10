package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PropertyViewer extends AnchorPane implements Initializable{

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
	
	private NamedPersistenceObjectModel property;
	private LanguageAwareString nameText;
	private LanguageAwareString descriptionText;
	private LanguageAwareString valueText;
	
	/**
	 * 
	 * @param property A Property to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PropertyViewer(NamedPersistenceObjectModel property, ILanguageService languageService) {
		this.property = property;
		
		nameText = new LanguageAwareString(languageService, DisplayStrings.PROPERTY_NAME);
		descriptionText = new LanguageAwareString(languageService, DisplayStrings.PROPERTY_DESCRIPTION);
		valueText = new LanguageAwareString(languageService, DisplayStrings.PROPERTY_VALUE);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PropertyViewer.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameLabel.textProperty().bind(nameText);
		descriptionLabel.textProperty().bind(descriptionText);
		valueLabel.textProperty().bind(valueText);
		
		type.getItems().addAll(property.types().list());
		type.setValue(property.types().selected().get());
		
		type.valueProperty().addListener((v, o, n) -> {
			property.types().selected().set(n);
		});
		
		name.textProperty().bindBidirectional(property.name());
		description.textProperty().bindBidirectional(property.description());
		value.textProperty().bindBidirectional(property.value());
	}
}
