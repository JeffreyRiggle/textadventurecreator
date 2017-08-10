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
import ilusr.textadventurecreator.views.player.BodyPartModel;
import ilusr.textadventurecreator.views.player.BodyPartViewer;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartFinderModel extends BaseFinderModel<BodyPartPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String bodName;
	private final String bodDescrption;
	private final String any;
	
	private final String libraryScope;
	private final String playerScope;
	
	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private PlayerPersistenceObject player;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BodyPartFinderModel(LibraryService libraryService, 
							   ILanguageService languageService,
							   IDialogService dialogService,
							   IDialogProvider dialogProvider,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.BODY_PART_NAME), 
							languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)),
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		bodName = languageService.getValue(DisplayStrings.BODY_PART_NAME);
		bodDescrption = languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}

	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param player A @see PlayerPersistenceObject representing a player.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BodyPartFinderModel(LibraryService libraryService, 
							   PlayerPersistenceObject player, 
							   ILanguageService languageService,
							   IDialogService dialogService,
							   IDialogProvider dialogProvider,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.BODY_PART_NAME), 
							languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)),
			  Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
					  		languageService.getValue(DisplayStrings.PLAYER), 
					  		languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		bodName = languageService.getValue(DisplayStrings.BODY_PART_NAME);
		bodDescrption = languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		playerScope = languageService.getValue(DisplayStrings.PLAYER);
		
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.player = player;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<BodyPartPersistenceObject> found = new ArrayList<BodyPartPersistenceObject>();
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
	
	private List<BodyPartPersistenceObject> findInLibrary(String text, String searchField) {
		List<BodyPartPersistenceObject> retVal = new ArrayList<BodyPartPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.bodyParts());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.bodyParts());
				continue;
			}
			
			for (BodyPartPersistenceObject bPart : item.bodyParts()) {
				if ((searchField.equals(bodName) || searchField.endsWith(any)) && bPart.objectName().contains(text)) {
					retVal.add(bPart);
				} else if ((searchField.equals(bodDescrption) || searchField.endsWith(any)) && bPart.description().contains(text)) {
					retVal.add(bPart);
				}
			}
		}
		
		return retVal;
	}
	
	private List<BodyPartPersistenceObject> findInPlayer(String text, String searchField) {
		List<BodyPartPersistenceObject> retVal = new ArrayList<BodyPartPersistenceObject>();
		for (BodyPartPersistenceObject bPart : player.bodyParts()) {
			if ((searchField.equals(bodName) || searchField.endsWith(any)) && bPart.objectName().contains(text)) {
				retVal.add(bPart);
			} else if ((searchField.equals(bodDescrption) || searchField.endsWith(any)) && bPart.description().contains(text)) {
				retVal.add(bPart);
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(BodyPartPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, String.format("Inspecting body part %s", item.objectName()));
		BodyPartViewer view = new BodyPartViewer(new BodyPartModel(item, null, dialogService, languageService, dialogProvider, styleService, urlProvider),
				languageService, styleService, urlProvider);
		view.setDisable(true);
		dialogService.displayModal(view, item.objectName());
	}
}
