package ilusr.textadventurecreator.library;

import java.util.UUID;
import java.util.logging.Level;

import ilusr.core.interfaces.Callback;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import textadventurelib.persistence.ActionPersistenceConverter;
import textadventurelib.persistence.TimerPersistenceConverter;
import textadventurelib.persistence.TriggerPersistenceConverter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryViewModel {

	private final String LIBRARY_ITEM_PERSISTENCE = "LibraryItemName: ";
	private final LibraryService libraryService;
	private final ILayoutService layoutService;
	private final IDialogService dialogService;
	private final TriggerViewFactory triggerViewFactory;
	private final ActionViewFactory actionViewFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final InternalURLProvider urlProvider;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	
	private LanguageAwareString libraryText;
	private LanguageAwareString importText;
	private LanguageAwareString exportText;
	
	private LibraryItem addItemKey;
	private ObservableList<LibraryItem> items;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to maintain library items.
	 * @param layoutService A @see LayoutService to add and remove tabs with.
	 * @param dialogService A @see IDialogService to display views with.
	 * @param triggerFactory A @see TriggerFactory to create triggers.
	 * @param actionFactory A @see ActionFactory to create actions.
	 * @param playerModFactory A @see PlayerModFactory to create player mods.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param urlProvider A @see InternalURLProvider to provide resources.
	 * @param dialogProvider A @see IDialogProvider to provide dialogs.
	 * @param styleService service to manage styles.
	 */
	public LibraryViewModel(LibraryService libraryService, 
							ILayoutService layoutService, 
							IDialogService dialogService, 
							TriggerViewFactory triggerFactory,
							ActionViewFactory actionFactory,
							PlayerModProviderFactory playerModFactory,
							ILanguageService languageService,
							InternalURLProvider urlProvider,
							IDialogProvider dialogProvider,
							IStyleContainerService styleService) {
		this.libraryService = libraryService;
		this.layoutService = layoutService;
		this.dialogService = dialogService;
		this.triggerViewFactory = triggerFactory;
		this.actionViewFactory = actionFactory;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.urlProvider = urlProvider;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		items = FXCollections.observableArrayList();
		
		libraryText = new LanguageAwareString(languageService, DisplayStrings.LIBRARIES);
		importText = new LanguageAwareString(languageService, DisplayStrings.IMPORT);
		exportText = new LanguageAwareString(languageService, DisplayStrings.EXPORT);
		
		initialize();
	}
	
	private void initialize() {
		LogRunner.logger().log(Level.INFO, "Initializing the library view model.");
		items.addAll(libraryService.getItems());
		
		libraryService.addLibraryChangedListener(() -> {
			Platform.runLater(() -> {
				LogRunner.logger().log(Level.INFO, "Library Service changed updating items.");
				items.clear();
				items.addAll(libraryService.getItems());
			});
		});
		
		addItemKey = new LibraryItem(UUID.randomUUID().toString(), 
				new TriggerPersistenceConverter(), new ActionPersistenceConverter(), new TimerPersistenceConverter());
	}
	
	/**
	 * 
	 * @param item The @see LibraryItem to remove from the view and service.
	 */
	public void removeLibraryItem(LibraryItem item) {
		LogRunner.logger().log(Level.INFO, String.format("Removing library item %s from model and service.", item.getLibraryName()));
		libraryService.removeLibraryItem(item);
		items.remove(item);
	}
	
	/**
	 * 
	 * @param path The path to load a library item from.
	 */
	public void importLibrary(String path) {
		libraryService.importLibrary(path, (l) -> {
			LogRunner.logger().log(Level.INFO, String.format("Imported library item %s", l.getLibraryName()));
			Platform.runLater(() -> { items.add(l); });
		});
	}
	
	/**
	 * 
	 * @param path The path to save a library item to.
	 * @param item The @see LibraryItem to save.
	 */
	public void exportLibrary(String path, LibraryItem item) {
		libraryService.exportLibrary(path, item, null);
	}
	
	/**
	 * 
	 * @return A list of the @see LibraryItem associated with this model.
	 */
	public ObservableList<LibraryItem> items() {
		return items;
	}
	
	/**
	 * 
	 * @return A @see LibraryItem to use as a key for adding other library items.
	 */
	public LibraryItem addItemKey() {
		return addItemKey;
	}
	
	/**
	 * 
	 * @return A @see Callback to run to edit a @see LibraryItem.
	 */
	public Callback<LibraryItem> getEditAction() {
		return (libraryItem) -> {
			try {
				LogRunner.logger().log(Level.INFO, String.format("Editing library item %s.", libraryItem.getLibraryName()));
				layoutService.addTab(BluePrintNames.LibraryItem, LIBRARY_ITEM_PERSISTENCE + libraryItem.getLibraryName() + ";=;" + libraryItem.getAuthor());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	/**
	 * 
	 * @return A @see ActionEvent to run to create a Library Item.
	 */
	public EventHandler<ActionEvent> getAddAction() {
		return (e) -> { addLibraryItem(); };
	}
	
	private void addLibraryItem() {
		LogRunner.logger().log(Level.INFO, "Adding library item to model and service.");
		LibraryItem item = new LibraryItem();
		libraryService.importLibrary(item);
		LibraryItemModel model = new LibraryItemModel(dialogService, item, triggerViewFactory, actionViewFactory, playerModFactory, languageService, urlProvider, ServiceManager.getInstance(), dialogProvider, styleService);
		layoutService.addTab(new LibraryItemContentTab(new LibraryItemView(model, languageService, styleService, urlProvider), languageService));
		items.add(item);
	}
	
	/**
	 * 
	 * @return The text to display for library.
	 */
	public SimpleStringProperty libraryText() {
		return libraryText;
	}
	
	/**
	 * 
	 * @return The text to display for import.
	 */
	public SimpleStringProperty importText() {
		return importText;
	}
	
	/**
	 * 
	 * @return The text to display for export.
	 */
	public SimpleStringProperty exportText() {
		return exportText;
	}
}
