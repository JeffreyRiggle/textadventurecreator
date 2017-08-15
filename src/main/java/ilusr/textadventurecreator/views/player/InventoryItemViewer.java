package ilusr.textadventurecreator.views.player;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.ItemFinder;
import ilusr.textadventurecreator.search.ItemFinderModel;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import textadventurelib.persistence.player.ItemPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventoryItemViewer extends PlayerDataView implements Initializable, IStyleWatcher {

	@FXML
	private Button addItem;
	
	@FXML
	private Button findItem;
	
	@FXML
	private Button editItem;
	
	@FXML
	private TextField amount;
	
	@FXML
	private Label item;
	
	@FXML
	private Label itemLabel;
	
	@FXML
	private Label amountLabel;
	
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private InventoryItem inventoryItem;
	
	private SimpleObjectProperty<InventoryItem> data;
	private LanguageAwareString itemText;
	private LanguageAwareString newText;
	private LanguageAwareString findText;
	private LanguageAwareString amountText;
	private LanguageAwareString editText;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param item A @see InventoryItem to create a view for.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public InventoryItemViewer(IDialogService dialogService, 
							   InventoryItem item, 
							   ILanguageService languageService,
							   IDialogProvider dialogProvider,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider) {
		this(dialogService, item, null, languageService, dialogProvider, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param item A @see InventoryItem to create a view for.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public InventoryItemViewer(IDialogService dialogService, 
							   InventoryItem item, 
							   LibraryService libraryService,
							   ILanguageService languageService,
							   IDialogProvider dialogProvider,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.inventoryItem = item;
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		data = new SimpleObjectProperty<InventoryItem>(item);
		itemText = new LanguageAwareString(languageService, DisplayStrings.ITEM);
		newText = new LanguageAwareString(languageService, DisplayStrings.NEW);
		findText = new LanguageAwareString(languageService, DisplayStrings.FIND);
		editText = new LanguageAwareString(languageService, DisplayStrings.EDIT);
		amountText = new LanguageAwareString(languageService, DisplayStrings.AMOUNT);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryItemViewer.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		itemLabel.textProperty().bind(itemText);
		addItem.textProperty().bind(newText);
		findItem.textProperty().bind(findText);
		amountLabel.textProperty().bind(amountText);
		editItem.textProperty().bind(editText);
		editItem.setDisable(true);
		
		if (inventoryItem.getItem() != null) {
			item.setText(inventoryItem.getItem().toString());
		}
		
		if (item.getText() != null && !item.getText().isEmpty()) {
			editItem.setDisable(false);
		}
		
		if (libraryService == null) {
			findItem.setVisible(false);
		} else {
			setupFind();
		}
		
		amount.textProperty().set(Integer.toString(inventoryItem.getAmount()));
		
		addItem.setOnAction((e) -> {
			try {
				ItemPersistenceObject pItem = new ItemPersistenceObject();
				ItemModel model = new ItemModel(pItem, libraryService, dialogService, languageService, dialogProvider, styleService, urlProvider);
				
				Dialog dialog = dialogProvider.create(new ItemViewer(model, styleService, urlProvider));
				dialog.isValid().bind(model.valid());
				dialog.setOnComplete(() -> {
					inventoryItem.setItem(pItem);
					item.setText(pItem.toString());
					editItem.setDisable(false);
				});
				
				dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
			} catch (Exception ex) {
				LogRunner.logger().severe(ex);
			}
		});
		
		editItem.setOnAction((e) -> {
			ItemModel model = new ItemModel(inventoryItem.getItem(), libraryService, dialogService, languageService, dialogProvider, styleService, urlProvider);
			
			Dialog dialog = dialogProvider.create(new ItemViewer(model, styleService, urlProvider));
			dialog.isValid().bind(model.valid());
			dialog.setOnComplete(() -> {
				item.setText(inventoryItem.getItem().toString());
			});
			
			dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.ITEM));
		});
		
		amount.textProperty().addListener((v, o, n) -> {
			try {
				inventoryItem.setAmount(Integer.parseInt(n));
			} catch (Exception ex) {
				//TODO Validation
			}
		});
		
		setupStyles();
	}

	private void setupFind() {
		findItem.setOnAction((e) -> { 
			try {
				ItemFinderModel finder = new ItemFinderModel(libraryService, languageService);
				Dialog dialog = dialogProvider.create(new ItemFinder(finder, styleService, urlProvider));
				dialog.setOnComplete(() -> {
					inventoryItem.setItem(finder.foundValue());
					item.setText(finder.foundValue().toString());
				});
				
				dialogService.displayModal(dialog);
			} catch (Exception ex) {
				LogRunner.logger().severe(ex);
			}
		});
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "inventorystylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.INVENTORY), this, false);
		
		String style = styleService.get(StyledComponents.INVENTORY);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
	
	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return data;
	}
}
