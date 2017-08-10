package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class RemoveCharacteristicView extends PlayerDataView implements Initializable {

	@FXML
	private ComboBox<String> characteristics;
	
	@FXML
	private Label removeLabel;
	
	private Map<String, CharacteristicPersistenceObject> characters;
	
	private BodyPartPersistenceObject bodyPart;
	private SimpleObjectProperty<CharacteristicPersistenceObject> data;
	private boolean initialized;
	private LanguageAwareString removeText;
	
	/**
	 * 
	 * @param bodyPart A @see BodyPartPersistenceObject to remove from.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public RemoveCharacteristicView(BodyPartPersistenceObject bodyPart, ILanguageService languageService) {
		this.bodyPart = bodyPart;
		
		characters = new HashMap<String, CharacteristicPersistenceObject>();
		data = new SimpleObjectProperty<CharacteristicPersistenceObject>();
		removeText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTIC_TO_REMOVE);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoveCharacteristicView.fxml"));
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
		setup();
		bind();
		initialized = true;
	}

	/**
	 * 
	 * @param bodyPart The new @see BodyPartPersistenceObject to bind to.
	 */
	public void setBodyPart(BodyPartPersistenceObject bodyPart) {
		if (!initialized) {
			this.bodyPart = bodyPart;
			return;
		}
		
		if (this.bodyPart != null) {
			teardown();
		}
		
		this.bodyPart = bodyPart;
		
		if (this.bodyPart != null) {
			setup();
		}
	}
	
	private void teardown() {
		characteristics.getItems().clear();
		characters.clear();
	}
	
	private void setup() {
		for (CharacteristicPersistenceObject chr : bodyPart.characteristics()) {
			characteristics.getItems().add(chr.objectName());
			characters.put(chr.objectName(), chr);
		}
	}
	
	private void bind() {
		removeLabel.textProperty().bind(removeText);
		characteristics.valueProperty().addListener((v, o, n) -> {
			if (characters.containsKey(n)) {
				data.set(characters.get(n));
			}
		});
	}
	
	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return data;
	}

}
