package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ItemFinderModel extends BaseFinderModel<ItemPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String itemName;
	private final String itemDescription;
	private final String any;
	
	private final String libraryScope;
	private final String playerScope;
	
	private final LibraryService libraryService;
	private PlayerPersistenceObject player;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ItemFinderModel(LibraryService libraryService, ILanguageService languageService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.ITEM_NAME), 
							languageService.getValue(DisplayStrings.ITEM_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		itemName = languageService.getValue(DisplayStrings.ITEM_NAME);
		itemDescription = languageService.getValue(DisplayStrings.ITEM_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = libraryService;
	}

	/**
	 * 
	 * @param librarySerivce A @see LibraryService to provide library items.
	 * @param player A @see PlayerPersistenceObject represeting a player.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ItemFinderModel(LibraryService librarySerivce, PlayerPersistenceObject player, ILanguageService languageService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.ITEM_NAME), 
							languageService.getValue(DisplayStrings.ITEM_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
					  		languageService.getValue(DisplayStrings.LIBRARY), 
					  		languageService.getValue(DisplayStrings.PLAYER)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		itemName = languageService.getValue(DisplayStrings.ITEM_NAME);
		itemDescription = languageService.getValue(DisplayStrings.ITEM_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = librarySerivce;
		this.player = player;
	}
	
	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<ItemPersistenceObject> found = new ArrayList<ItemPersistenceObject>();
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
	
	private List<ItemPersistenceObject> findInLibrary(String text, String searchField) {
		List<ItemPersistenceObject> retVal = new ArrayList<ItemPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.items());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.items());
				continue;
			}
			
			for (ItemPersistenceObject lItem : item.items()) {
				if ((searchField.equals(itemName) || searchField.endsWith(any)) && lItem.itemName().contains(text)) {
					retVal.add(lItem);
				} else if ((searchField.equals(itemDescription) || searchField.endsWith(any)) && lItem.itemDescription().contains(text)) {
					retVal.add(lItem);
				}
			}
		}
		
		return retVal;
	}
	
	private List<ItemPersistenceObject> findInPlayer(String text, String searchField) {
		List<ItemPersistenceObject> retVal = new ArrayList<ItemPersistenceObject>();
		for (ItemPersistenceObject item : player.inventory().items()) {
			if ((searchField.equals(itemName) || searchField.endsWith(any)) && item.itemName().contains(text)) {
				retVal.add(item);
			} else if ((searchField.equals(itemDescription) || searchField.endsWith(any)) && item.itemDescription().contains(text)) {
				retVal.add(item);
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(ItemPersistenceObject item) {
		LogRunner.logger().info(String.format("Inspecting item %s", item.itemName()));
	}
}
