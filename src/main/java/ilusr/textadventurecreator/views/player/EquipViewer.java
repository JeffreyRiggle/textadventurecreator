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
import ilusr.textadventurecreator.search.BodyPartFinder;
import ilusr.textadventurecreator.search.BodyPartFinderModel;
import ilusr.textadventurecreator.search.ItemFinder;
import ilusr.textadventurecreator.search.ItemFinderModel;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.player.EquipPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EquipViewer extends AnchorPane implements Initializable, IStyleWatcher{

	@FXML
	private Button selectBodyPart;
	
	@FXML
	private Button selectItem;
	
	@FXML
	private Button addItem;
	
	@FXML
	private Label bodyPart;
	
	@FXML
	private Label item;
	
	@FXML
	private Label bodyPartLabel;
	
	@FXML
	private Label itemLabel;
	
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private PlayerPersistenceObject player;
	private EquipPersistenceObject equip;
	private LanguageAwareString bodyPartText;
	private LanguageAwareString selectText;
	private LanguageAwareString itemText;
	private LanguageAwareString newText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param player A @see PlayerPersistenceObject to bind to.
	 * @param equip A @see EquipPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public EquipViewer(IDialogService dialogService, 
					   LibraryService libraryService, 
					   PlayerPersistenceObject player, 
					   EquipPersistenceObject equip,
					   ILanguageService languageService,
					   IDialogProvider dialogProvider,
					   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.player = player;
		this.equip = equip;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		bodyPartText = new LanguageAwareString(languageService, DisplayStrings.BODY_PART);
		selectText = new LanguageAwareString(languageService, DisplayStrings.SELECT);
		itemText = new LanguageAwareString(languageService, DisplayStrings.ITEM);
		newText = new LanguageAwareString(languageService, DisplayStrings.NEW);
		valid = new SimpleBooleanProperty();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EquipViewer.fxml"));
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
		bodyPartLabel.textProperty().bind(bodyPartText);
		selectBodyPart.textProperty().bind(selectText);
		itemLabel.textProperty().bind(itemText);
		selectItem.textProperty().bind(selectText);
		addItem.textProperty().bind(newText);
		
		if (player.bodyParts().isEmpty()) {
			selectBodyPart.setDisable(true);
		}
		
		if (player.inventory().items().isEmpty()) {
			selectItem.setDisable(true);
		}
		
		if (equip.bodyPart() != null) {
			bodyPart.setText(equip.bodyPart().toString());
		}
		
		if (equip.item() != null) {
			item.setText(equip.item().toString());
		}
		
		selectBodyPart.setOnAction((e) -> {
			BodyPartFinderModel finder = new BodyPartFinderModel(libraryService, player, languageService, dialogService, dialogProvider, styleService, urlProvider);
			Dialog dialog = new Dialog(new BodyPartFinder(finder, styleService, urlProvider), languageService);
			
			dialog.setOnComplete(() -> {
				equip.bodyPart(finder.foundValue());
				bodyPart.setText(finder.foundValue().toString());
				applyValues();
			});
			
			dialogService.displayModal(dialog);
		});
		
		selectItem.setOnAction((e) -> {
			ItemFinderModel finder = new ItemFinderModel(libraryService, player, languageService);
			Dialog dialog = dialogProvider.create(new ItemFinder(finder, styleService, urlProvider));
			
			dialog.setOnComplete(() -> {
				equip.item(finder.foundValue());
				item.setText(finder.foundValue().toString());
				applyValues();
			});
			
			dialogService.displayModal(dialog);
		});
		
		addItem.setOnAction((e) -> {
			try {
				ItemPersistenceObject pItem = new ItemPersistenceObject();
				Dialog dialog = dialogProvider.create(new ItemViewer(new ItemModel(pItem, libraryService, dialogService, languageService, dialogProvider, styleService, urlProvider), styleService, urlProvider));
				dialog.setOnComplete(() -> {
					equip.item(pItem);
					item.setText(pItem.toString());
					applyValues();
				});
				
				dialogService.displayModal(dialog);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		valid.set(equip.item().itemName() != null && !equip.item().itemName().isEmpty() && 
				  equip.bodyPart().objectName() != null && !equip.bodyPart().objectName().isEmpty());
		setupStyles();
	}

	private void applyValues() {
		player.equipment().equip(equip.item(), equip.bodyPart());
		valid.set(equip.item().itemName() != null && !equip.item().itemName().isEmpty() && 
				  equip.bodyPart().objectName() != null && !equip.bodyPart().objectName().isEmpty());
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "equipmentstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.EQUIPMENT), this, false);
		
		String style = styleService.get(StyledComponents.EQUIPMENT);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
	
	/**
	 * 
	 * @return If the equipment is valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
}
