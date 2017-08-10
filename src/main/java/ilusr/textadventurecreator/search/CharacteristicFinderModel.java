package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.player.CharacteristicViewer;
import ilusr.textadventurecreator.views.player.NamedPersistenceObjectModel;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CharacteristicFinderModel extends BaseFinderModel<CharacteristicPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String charName;
	private final String charDescription;
	private final String any;
	
	private final String libraryScope;
	private final String playerScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	
	private PlayerPersistenceObject player;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 */
	public CharacteristicFinderModel(LibraryService libraryService, 
									 ILanguageService languageService,
									 IDialogService dialogService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.CHARACTERISTIC_NAME), 
							languageService.getValue(DisplayStrings.CHARACTERISTIC_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		charName = languageService.getValue(DisplayStrings.CHARACTERISTIC_NAME);
		charDescription = languageService.getValue(DisplayStrings.CHARACTERISTIC_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
	}
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param player A @see PlayerPersistenceObject representing a player.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 */
	public CharacteristicFinderModel(LibraryService libraryService, 
									 PlayerPersistenceObject player, 
									 ILanguageService languageService,
									 IDialogService dialogService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.CHARACTERISTIC_NAME), 
							languageService.getValue(DisplayStrings.CHARACTERISTIC_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
					  		languageService.getValue(DisplayStrings.LIBRARY), 
					  		languageService.getValue(DisplayStrings.PLAYER)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		charName = languageService.getValue(DisplayStrings.CHARACTERISTIC_NAME);
		charDescription = languageService.getValue(DisplayStrings.CHARACTERISTIC_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.player = player;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<CharacteristicPersistenceObject> found = new ArrayList<CharacteristicPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		if ((searchScope.equals(playerScope) || searchScope.equals(any)) && player != null) {
			found.addAll(findInPlayer(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<CharacteristicPersistenceObject> findInLibrary(String text, String searchField) {
		List<CharacteristicPersistenceObject> retVal = new ArrayList<CharacteristicPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.characteristics());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.characteristics());
				continue;
			}
			
			for (CharacteristicPersistenceObject charc : item.characteristics()) {
				if ((searchField.equals(charName) || searchField.endsWith(any)) && charc.objectName().contains(text)) {
					retVal.add(charc);
				} else if ((searchField.equals(charDescription) || searchField.endsWith(any)) && charc.description().contains(text)) {
					retVal.add(charc);
				}
			}
		}
		
		return retVal;
	}
	
	private List<CharacteristicPersistenceObject> findInPlayer(String text, String searchField) {
		List<CharacteristicPersistenceObject> retVal = new ArrayList<CharacteristicPersistenceObject>();
		for (CharacteristicPersistenceObject charc : player.characteristics()) {
			if ((searchField.equals(charName) || searchField.endsWith(any)) && charc.objectName().contains(text)) {
				retVal.add(charc);
			} else if ((searchField.equals(charDescription) || searchField.endsWith(any)) && charc.description().contains(text)) {
				retVal.add(charc);
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(CharacteristicPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, String.format("Inspecting characteristic %s", item.objectName()));
		CharacteristicViewer view = new CharacteristicViewer(new NamedPersistenceObjectModel(item), languageService);
		view.setDisable(true);
		dialogService.displayModal(view, item.objectName());
	}
}
