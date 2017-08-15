package ilusr.textadventurecreator.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import textadventurelib.persistence.LayoutPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutFinderModel extends BaseFinderModel<LayoutPersistenceObject> {

	private final String layoutName;
	private final String libraryName;
	private final String libraryAuthor;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param urlProvider A @see InternalURLProvider to provide resources.
	 */
	public LayoutFinderModel(LibraryService libraryService, 
								ILanguageService languageService,
								IDialogService dialogService,
								InternalURLProvider urlProvider) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.LAYOUT), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		layoutName = languageService.getValue(DisplayStrings.LAYOUT);
		any = languageService.getValue(DisplayStrings.ANY);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.urlProvider = urlProvider;
	}

	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<LayoutPersistenceObject> found = new ArrayList<LayoutPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<LayoutPersistenceObject> findInLibrary(String text, String searchField) {
		List<LayoutPersistenceObject> retVal = new ArrayList<LayoutPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.layouts());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.layouts());
				continue;
			}
			
			for (LayoutPersistenceObject layout : item.layouts()) {
				if ((searchField.equals(layoutName) || searchField.equals(any)) && layout.id().contains(text)) {
					retVal.add(layout);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(LayoutPersistenceObject item) {
		LogRunner.logger().info(String.format("Inspecting layout %s", item.id()));
		LayoutCreatorModel model = new LayoutCreatorModel(item, languageService, dialogService, urlProvider, new SelectionManager());
		model.compile();
	}
}
