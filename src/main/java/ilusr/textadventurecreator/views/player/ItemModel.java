package ilusr.textadventurecreator.views.player;

import java.util.List;
import java.util.UUID;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.PropertyFinder;
import ilusr.textadventurecreator.search.PropertyFinderModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ItemModel {

	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private ItemPersistenceObject item;
	
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private ObservableList<PropertyPersistenceObject> properties;
	private PropertyPersistenceObject addKey;
	private LanguageAwareString nameText;
	private LanguageAwareString descriptionText;
	private LanguageAwareString propertyText;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param item The @see ItemPersistenceObject to bind to.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public ItemModel(ItemPersistenceObject item, 
					 LibraryService libraryService, 
					 IDialogService dialogService,
					 ILanguageService languageService,
					 IDialogProvider dialogProvider,
					 IStyleContainerService styleService,
					 InternalURLProvider urlProvider) {
		this.item = item;
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		name = new SimpleStringProperty(item.itemName());
		description = new SimpleStringProperty(item.itemDescription());
		properties = FXCollections.observableArrayList();
		nameText = new LanguageAwareString(languageService, DisplayStrings.ITEM_NAME);
		descriptionText = new LanguageAwareString(languageService, DisplayStrings.ITEM_DESCRIPTION);
		propertyText = new LanguageAwareString(languageService, DisplayStrings.PROPERTIES);
		valid = new SimpleBooleanProperty(item.itemName() != null && !item.itemName().isEmpty());
		
		initialize();
		bind();
	}
	
	private void initialize() {
		try {
			addKey = new PropertyPersistenceObject();
			addKey.objectName(UUID.randomUUID().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		properties.addAll(item.properties());
	}
	
	private void bind() {
		name.addListener((v, o, n) -> {
			valid.set(n != null && !n.isEmpty());
			item.itemName(n);
		});
		
		description.addListener((v, o, n) -> {
			item.itemDescription(n);
		});
		
		properties.addListener((Change<? extends PropertyPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends PropertyPersistenceObject> rProp = c.getRemoved();
			List<? extends PropertyPersistenceObject> aProp = c.getList();
			
			for (PropertyPersistenceObject model : rProp) {
				if (aProp.contains(model)) {
					continue;
				}
				
				item.removeProperty(model);
			}
		});
	}
	
	/**
	 * 
	 * @return If library actions allowed
	 */
	public boolean allowLibraryAdd() {
		return libraryService != null;
	}
	
	/**
	 * 
	 * @return The name of the item.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return The description for the item.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return Display text for item name.
	 */
	public SimpleStringProperty nameText() {
		return nameText;
	}
	
	/**
	 * 
	 * @return Display text for description.
	 */
	public SimpleStringProperty descriptionText() {
		return descriptionText;
	}
	
	/**
	 * 
	 * @return Display text for property.
	 */
	public SimpleStringProperty propertyText() {
		return propertyText;
	}
	
	/**
	 * 
	 * @return If the item is valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return The properties associated with this item.
	 */
	public ObservableList<PropertyPersistenceObject> properties() {
		return properties;
	}
	
	/**
	 * 
	 * @return The key to use as an add item for properties.
	 */
	public PropertyPersistenceObject addKey() {
		return addKey;
	}
	
	/**
	 * 
	 * @return The associated language service.
	 */
	public ILanguageService getLanguageService() {
		return languageService;
	}
	
	/**
	 * Adds a property to this item.
	 */
	public void addProperty() {
		try {
			LogRunner.logger().info("Adding property to item.");
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			item.addProperty(prop);
			properties.add(prop);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Adds a property to this item from a library item.
	 */
	public void addPropertyFromLibrary() {
		PropertyFinderModel finder = new PropertyFinderModel(libraryService, languageService, dialogService);
		Dialog dialog = dialogProvider.create(new PropertyFinder(finder, styleService, urlProvider));
			
		dialog.setOnComplete(() -> {
			if (finder.foundValue() == null) {
				return;
			}
			
			LogRunner.logger().info(String.format("Adding property %s to item.", finder.foundValue().objectName()));
			item.addProperty(finder.foundValue());
			properties.add(finder.foundValue());
		});
			
		LogRunner.logger().info("Attempting to property to item from library.");
		dialogService.displayModal(dialog);
	}
}
