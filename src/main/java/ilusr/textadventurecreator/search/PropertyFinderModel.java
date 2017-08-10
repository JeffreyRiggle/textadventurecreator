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
import ilusr.textadventurecreator.views.player.NamedPersistenceObjectModel;
import ilusr.textadventurecreator.views.player.PropertyViewer;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PropertyFinderModel extends BaseFinderModel<PropertyPersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String propName;
	private final String propDescription;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 */
	public PropertyFinderModel(LibraryService libraryService, 
							   ILanguageService languageService,
							   IDialogService dialogService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.PROPERTY_NAME), 
							languageService.getValue(DisplayStrings.PROPERTY_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)),
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		any = languageService.getValue(DisplayStrings.ANY);
		propName = languageService.getValue(DisplayStrings.PROPERTY_NAME);
		propDescription = languageService.getValue(DisplayStrings.PROPERTY_DESCRIPTION);
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
		
		List<PropertyPersistenceObject> found = new ArrayList<PropertyPersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<PropertyPersistenceObject> findInLibrary(String text, String searchField) {
		List<PropertyPersistenceObject> retVal = new ArrayList<PropertyPersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.properties());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.properties());
				continue;
			}
			
			for (PropertyPersistenceObject prop : item.properties()) {
				if ((searchField.equals(propName) || searchField.endsWith(any)) && prop.objectName().contains(text)) {
					retVal.add(prop);
				} else if ((searchField.equals(propDescription) || searchField.endsWith(any)) && prop.description().contains(text)) {
					retVal.add(prop);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(PropertyPersistenceObject item) {
		LogRunner.logger().log(Level.INFO, String.format("Inspecting property %s", item.objectName()));
		PropertyViewer view = new PropertyViewer(new NamedPersistenceObjectModel(item), languageService);
		view.setDisable(true);
		dialogService.displayModal(view, item.objectName());
	}
}
