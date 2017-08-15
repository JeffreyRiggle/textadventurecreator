package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.gamestate.CompletionTimerModel;
import ilusr.textadventurecreator.views.gamestate.CompletionTimerView;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.TimerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TimerFinderModel extends BaseFinderModel<TimerPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 */
	public TimerFinderModel(LibraryService libraryService, 
							ILanguageService languageService,
							IDialogService dialogService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		any = languageService.getValue(DisplayStrings.ANY);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<TimerPersistenceObject> found = new ArrayList<TimerPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<TimerPersistenceObject> findInLibrary(String text, String searchField) {
		List<TimerPersistenceObject> retVal = new ArrayList<TimerPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.timers());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.timers());
				continue;
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(TimerPersistenceObject item) {
		LogRunner.logger().info("Inspecting timer.");
		CompletionTimerView view = new CompletionTimerView(new CompletionTimerModel((CompletionTimerPersistenceObject)item, languageService));
		view.setDisable(true);
		dialogService.displayModal(view);
	}
}
