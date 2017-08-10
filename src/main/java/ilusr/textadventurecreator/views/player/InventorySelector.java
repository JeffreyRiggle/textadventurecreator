package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventorySelector extends PlayerDataView implements Initializable {

	@FXML
	private ComboBox<String> items;
	
	@FXML
	private Label itemLabel;

	private InventoryPersistenceObject inventory;
	private SimpleObjectProperty<ItemPersistenceObject> data;
	private Map<String, ItemPersistenceObject> itemMap;
	private boolean initialized;
	private String selectItem = null;
	private LanguageAwareString itemText;
	
	/**
	 * 
	 * @param inventory A @see InventoryPersistenceObject to select from.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public InventorySelector(InventoryPersistenceObject inventory, ILanguageService languageService) {
		this(inventory, null, languageService);
	}
	
	/**
	 * 
	 * @param inventory A @see InventoryPersistenceObject to select from.
	 * @param item the initially selected item.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public InventorySelector(InventoryPersistenceObject inventory, String item, ILanguageService languageService) {
		this.inventory = inventory;
		this.selectItem = item;
		
		itemMap = new HashMap<String, ItemPersistenceObject>();
		data = new SimpleObjectProperty<ItemPersistenceObject>();
		itemText = new LanguageAwareString(languageService, DisplayStrings.ITEM_TO_USE);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("InventorySelector.fxml"));
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
		
		items.valueProperty().addListener((v, o, n) -> {
			if (itemMap.containsKey(n)) {
				data.set(itemMap.get(n));
			}
		});
		
		itemLabel.textProperty().bind(itemText);
		initialized = true;
	}

	/**
	 * 
	 * @param inventory The @see InventoryPersistenceObject to use.
	 */
	public void setInventory(InventoryPersistenceObject inventory) {
		if (!initialized) {
			this.inventory = inventory;
			return;
		}
		
		if (this.inventory != null) {
			tearDown();
		}
		
		this.inventory = inventory;
		
		if (this.inventory != null) {
			setup();
		}
	}
	
	/**
	 * 
	 * @param name The new item name to select.
	 */
	public void setSelected(String name) {
		if (!initialized) {
			selectItem = name;
			return;
		}
		
		Platform.runLater(() -> {
			items.valueProperty().set(name);
		});
	}
	
	private void tearDown() {
		items.getItems().clear();
		itemMap.clear();
	}
	
	private void setup() {
		for (ItemPersistenceObject item : inventory.items()) {
			try {
				itemMap.put(item.itemName(), new ItemPersistenceObject(item));
				items.getItems().add(item.itemName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (selectItem != null) {
			items.valueProperty().set(selectItem);
		}
	}
	
	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return data;
	}

}
