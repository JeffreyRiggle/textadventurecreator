package ilusr.textadventurecreator.views.player;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.CharacteristicFinder;
import ilusr.textadventurecreator.search.CharacteristicFinderModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartModel {

	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private BodyPartPersistenceObject bodyPart;
	private PlayerPersistenceObject player;
	
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	private CharacteristicPersistenceObject addKey;
	private ObservableList<CharacteristicPersistenceObject> characteristics;
	private SimpleObjectProperty<? extends Object> data;
	private SimpleBooleanProperty valid;
	private LanguageAwareString bodyNameText;
	private LanguageAwareString bodyDescriptionText;
	private LanguageAwareString characteristicText;
	
	/**
	 * 
	 * @param bodyPart The @see BodyPartPersistenceObject to bind to.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public BodyPartModel(BodyPartPersistenceObject bodyPart, 
						 LibraryService libraryService, 
						 IDialogService dialogService,
						 ILanguageService languageService,
						 IDialogProvider dialogProvider,
						 IStyleContainerService styleService,
						 InternalURLProvider urlProvider) {
		this(bodyPart, libraryService, dialogService, null, languageService, dialogProvider, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param bodyPart The @see BodyPartPersistenceObject to bind to.
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param player A @see PlayerPersistenceObject find characteristics from.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public BodyPartModel(BodyPartPersistenceObject bodyPart, 
						 LibraryService libraryService, 
						 IDialogService dialogService, 
						 PlayerPersistenceObject player,
						 ILanguageService languageService,
						 IDialogProvider dialogProvider,
						 IStyleContainerService styleService,
						 InternalURLProvider urlProvider) {
		this.bodyPart = bodyPart;
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.player = player;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		name = new SimpleStringProperty(bodyPart.objectName());
		description = new SimpleStringProperty(bodyPart.description());
		data = new SimpleObjectProperty<BodyPartPersistenceObject>(bodyPart);
		characteristics = FXCollections.observableArrayList();
		bodyNameText = new LanguageAwareString(languageService, DisplayStrings.BODY_PART_NAME);
		bodyDescriptionText = new LanguageAwareString(languageService, DisplayStrings.BODY_PART_DESCRIPTION);
		characteristicText = new LanguageAwareString(languageService, DisplayStrings.CHARACTERISTICS);
		valid = new SimpleBooleanProperty(bodyPart.objectName() != null && !bodyPart.objectName().isEmpty());
		
		initialize();
		bind();
	}
	
	private void initialize() {
		try {
			addKey = new CharacteristicPersistenceObject();
			addKey.objectName(UUID.randomUUID().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		characteristics.addAll(bodyPart.characteristics());
	}
	
	private void bind() {
		name.addListener((v, o, n) -> {
			bodyPart.objectName(n);
			valid.set(n != null && !n.isEmpty());
		});
		
		description.addListener((v, o, n) -> {
			bodyPart.description(n);
		});
		
		characteristics.addListener((Change<? extends CharacteristicPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends CharacteristicPersistenceObject> rChar = c.getRemoved();
			List<? extends CharacteristicPersistenceObject> aChar = c.getList();
			
			for (CharacteristicPersistenceObject model : rChar) {
				if (aChar.contains(model)) {
					continue;
				}
				
				player.removeCharacteristic(model);
			}
		});
	}
	
	/**
	 * 
	 * @return If library actions are allowed.
	 */
	public boolean allowLibraryAdd() {
		return libraryService != null;
	}
	
	/**
	 * 
	 * @return The name of this body part
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return The description for this body part.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return Display string for body part name.
	 */
	public SimpleStringProperty bodyNameText() {
		return bodyNameText;
	}
	
	/**
	 * 
	 * @return Display string for description.
	 */
	public SimpleStringProperty bodyDescriptionText() {
		return bodyDescriptionText;
	}
	
	/**
	 * 
	 * @return Display string for characteristic.
	 */
	public SimpleStringProperty characteristicText() {
		return characteristicText;
	}
	
	/**
	 * 
	 * @return If the body part is currently valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return A key to be used as a add key for characteristics.
	 */
	public CharacteristicPersistenceObject addKey() {
		return addKey;
	}
	
	/**
	 * 
	 * @return The characteristics associated with this body part.
	 */
	public ObservableList<CharacteristicPersistenceObject> characteristics() {
		return characteristics;
	}
	
	/**
	 * Adds a characteristic to this body part.
	 */
	public void addCharacteristic() {
		try {
			LogRunner.logger().log(Level.INFO, "Adding characteristic to body part.");
			CharacteristicPersistenceObject characteristic = new CharacteristicPersistenceObject();
			bodyPart.addCharacteristic(characteristic);
			characteristics.add(characteristic);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Adds a characteristic to this body part from a library item.
	 */
	public void addFromLibrary() {
		try {
			LogRunner.logger().log(Level.INFO, "Attempting to find a characteristic for body.");
			CharacteristicFinderModel finder;
			
			if (player != null) {
				finder = new CharacteristicFinderModel(libraryService, player, languageService, dialogService);
			} else {
				finder = new CharacteristicFinderModel(libraryService, languageService, dialogService);
			}

			Dialog dialog = dialogProvider.create(new CharacteristicFinder(finder, styleService, urlProvider));
			
			dialog.setOnComplete(() -> {
				if (finder.foundValue() == null) {
					return;
				}
				
				LogRunner.logger().log(Level.INFO, String.format("Adding found characteristic %s to body part.", finder.foundValue().objectName()));
				player.addCharacteristic(finder.foundValue());
				characteristics.add(finder.foundValue());
			});
			
			dialogService.displayModal(dialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The body part persistence object.
	 */
	public SimpleObjectProperty<? extends Object> data() {
		return data;
	}
}
