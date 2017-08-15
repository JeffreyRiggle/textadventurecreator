package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionModel;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.gamestate.OptionInspector;
import ilusr.textadventurecreator.views.gamestate.OptionModel;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.OptionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionFinderModel extends BaseFinderModel<OptionPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ActionViewFactory actionViewFactory;
	private final TriggerViewFactory triggerViewFactory;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param actionViewFactory A @see ActionViewFactory to create action views.
	 * @param triggerViewFactory A @see TriggerViewFactory to create trigger views.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionFinderModel(LibraryService libraryService, 
							 ILanguageService languageService,
							 IDialogService dialogService,
							 ActionViewFactory actionViewFactory,
							 TriggerViewFactory triggerViewFactory,
							 IDialogProvider dialogProvider,
							 IStyleContainerService styleService,
							 InternalURLProvider urlProvider) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.actionViewFactory = actionViewFactory;
		this.triggerViewFactory = triggerViewFactory;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<OptionPersistenceObject> found = new ArrayList<OptionPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<OptionPersistenceObject> findInLibrary(String text, String searchField) {
		List<OptionPersistenceObject> retVal = new ArrayList<OptionPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.options());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.options());
				continue;
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(OptionPersistenceObject item) {
		LogRunner.logger().info("Inspecting option.");
		OptionModel model = new OptionModel(dialogService, item, null, languageService, null, dialogProvider, styleService, urlProvider);
		ActionModel actionModel = new ActionModel(model.action().get(), languageService);												
		OptionInspector view = new OptionInspector(model, actionViewFactory.create(actionModel), triggerViewFactory, languageService, dialogService);
		dialogService.displayModal(view, languageService.getValue(DisplayStrings.OPTION));
	}
}
