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
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerFinderModel extends BaseFinderModel<TriggerPersistenceObject> {
	
	private final String libraryName;
	private final String libraryAuthor;
	private final String triggerType;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final TriggerViewFactory viewFactory;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param viewFactory A @see TriggerViewFactory to create trigger views.
	 */
	public TriggerFinderModel(LibraryService libraryService, 
							  ILanguageService languageService,
							  IDialogService dialogService,
							  TriggerViewFactory viewFactory) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.ACTION_TYPE), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		triggerType = languageService.getValue(DisplayStrings.ACTION_TYPE);
		any = languageService.getValue(DisplayStrings.ANY);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.viewFactory = viewFactory;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<TriggerPersistenceObject> found = new ArrayList<TriggerPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<TriggerPersistenceObject> findInLibrary(String text, String searchField) {
		List<TriggerPersistenceObject> retVal = new ArrayList<TriggerPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.triggers());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.triggers());
				continue;
			}
			
			for (TriggerPersistenceObject trigger : item.triggers()) {
				if ((searchField.equals(triggerType) || searchField.endsWith(any)) && trigger.type().contains(text)) {
					retVal.add(trigger);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(TriggerPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, "Inspecting trigger.");
		TriggerModel model = new TriggerModel(item, languageService);
		TriggerView view = viewFactory.create(model);
		view.setDisable(true);
		dialogService.displayModal(view, languageService.getValue(DisplayStrings.TRIGGER));
	}
}
