package ilusr.textadventurecreator.views;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of objects to select from.
 */
public class ItemSelector<T> extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ListView<T> itemList;
	
	@FXML
	private Button select;
	
	private final List<T> items;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private EventHandler<ActionEvent> handler;
	
	/**
	 * 
	 * @param items The items to select from.
	 */
	public ItemSelector(List<T> items) {
		this(items, null, null, null);
	}
	
	/**
	 * 
	 * @param items The items to select from.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public ItemSelector(List<T> items, 
						ILanguageService languageService,
						IStyleContainerService styleService,
						InternalURLProvider urlProvider) {
		this.items = items;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ItemSelector.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param handler A event handler to run when an item is selected.
	 */
	public void setSelectedAction(EventHandler<ActionEvent> handler) {
		this.handler = handler;
	}
	
	/**
	 * 
	 * @return The item that is selected.
	 */
	public T selectedItem() {
		return itemList.selectionModelProperty().get().selectedItemProperty().get();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.getStyleClass().add("root");
		itemList.getItems().addAll(items);
		select.setOnAction((e) -> {
			handler.handle(e);
			Stage s = (Stage)super.getScene().getWindow();
	        s.close();
		});
		
		setupDisplay();
		
		if (styleService != null && urlProvider != null) {
			setupStyles();
		}
	}
	
	private void setupDisplay() {
		if (languageService == null) {
			select.setText("Select");
			return;
		}
		
		select.setText(languageService.getValue(DisplayStrings.SELECT));
		languageService.addListener(() -> {
			select.setText(languageService.getValue(DisplayStrings.SELECT));
		});
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "itemselectorstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.ITEM_SELECTOR), this, false);
		
		String style = styleService.get(StyledComponents.ITEM_SELECTOR);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
