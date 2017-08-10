package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.PlayerModel;
import ilusr.textadventurecreator.views.player.PlayerView;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerFinderModel extends BaseFinderModel<PlayerPersistenceObject> {

	private final String playerName;
	private final String libraryName;
	private final String libraryAuthor;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public PlayerFinderModel(LibraryService libraryService, 
							 ILanguageService languageService,
							 IDialogService dialogService,
							 IDialogProvider dialogProvider,
							 IStyleContainerService styleService,
							 InternalURLProvider urlProvider) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.PLAYER_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		playerName = languageService.getValue(DisplayStrings.PLAYER_NAME);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<PlayerPersistenceObject> found = new ArrayList<PlayerPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<PlayerPersistenceObject> findInLibrary(String text, String searchField) {
		List<PlayerPersistenceObject> retVal = new ArrayList<PlayerPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.players());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.players());
				continue;
			}
			
			for (PlayerPersistenceObject player : item.players()) {
				if ((searchField.equals(playerName) || searchField.endsWith(any)) && player.playerName().contains(text)) {
					retVal.add(player);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(PlayerPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, String.format("Inspecting player %s", item.playerName()));
		PlayerView view = new PlayerView(new PlayerModel(dialogService, null, item, languageService, dialogProvider, styleService, urlProvider),
				languageService, styleService, urlProvider);
		view.setDisable(true);
		dialogService.displayModal(view, item.playerName());
	}
}
