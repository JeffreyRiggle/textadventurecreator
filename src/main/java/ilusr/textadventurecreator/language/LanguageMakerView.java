package ilusr.textadventurecreator.language;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LanguageMakerView extends AnchorPane implements Initializable {

	@FXML
	private TextField code;
	
	@FXML
	private TextField name;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label codeLabel;
	
	@FXML
	private TableView<LanguageItem> table;
	
	@FXML
	private TableColumn<LanguageItem, String> keyword;
	
	@FXML
	private TableColumn<LanguageItem, String> enValue;
	
	@FXML
	private TableColumn<LanguageItem, String> newValue;
	
	private LanguageMakerModel model;
	
	/**
	 * 
	 * @param model A @see LanguageMakerModel to use.
	 */
	public LanguageMakerView(LanguageMakerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LanguageMaker.fxml"));
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
		code.textProperty().bindBidirectional(model.code());
		name.textProperty().bindBidirectional(model.name());
		nameLabel.textProperty().bind(model.nameText());
		codeLabel.textProperty().bind(model.codeText());
		
		keyword.setCellValueFactory(new PropertyValueFactory<>("keyword"));
		keyword.textProperty().bind(model.keywordText());
		enValue.setCellValueFactory(new PropertyValueFactory<>("enValue"));
		enValue.textProperty().bind(model.usEnglishText());
		
		table.setEditable(true);
		newValue.setCellValueFactory(cellData -> cellData.getValue().newValueProperty());
		newValue.setCellFactory((c) -> new TableEditCell<LanguageItem>((t) -> {
			t.value().getTableView().getItems().get(t.key().key()).setNewValue(t.key().value());
		}));
		newValue.textProperty().bind(model.valueText());
		
		table.setItems(model.items());
	}

}
