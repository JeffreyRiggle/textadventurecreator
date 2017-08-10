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
import ilusr.textadventurecreator.views.action.ActionModel;
import ilusr.textadventurecreator.views.action.ActionView;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionFinderModel extends BaseFinderModel<ActionPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String actionType;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ActionViewFactory actionViewFactory;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param actionViewFactory A @see ActionViewFactory to create action views.
	 */
	public ActionFinderModel(LibraryService libraryService, 
							 ILanguageService languageService,
							 IDialogService dialogService,
							 ActionViewFactory actionViewFactory) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.ACTION_TYPE), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		actionType = languageService.getValue(DisplayStrings.ACTION_TYPE);
		any = languageService.getValue(DisplayStrings.ANY);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.actionViewFactory = actionViewFactory;
		this.languageService = languageService;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<ActionPersistenceObject> found = new ArrayList<ActionPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<ActionPersistenceObject> findInLibrary(String text, String searchField) {
		List<ActionPersistenceObject> retVal = new ArrayList<ActionPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.actions());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.actions());
				continue;
			}
			
			for (ActionPersistenceObject action : item.actions()) {
				if (searchField.equals(actionType) && action.type().contains(text)) {
					retVal.add(action);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(ActionPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, "Inspecting action.");
		ActionView view = actionViewFactory.create(new ActionModel(item, languageService), getPlayers());
		view.setDisable(true);
		dialogService.displayModal(view);
	}
	
	private List<PlayerPersistenceObject> getPlayers() {
		List<PlayerPersistenceObject> retVal = new ArrayList<PlayerPersistenceObject>();
		
		for (LibraryItem item : libraryService.getItems()) {
			retVal.addAll(item.players());
		}
		
		return retVal;
	}
}
