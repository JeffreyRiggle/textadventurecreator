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
import ilusr.textadventurecreator.views.player.AttributeViewer;
import ilusr.textadventurecreator.views.player.NamedPersistenceObjectModel;
import textadventurelib.persistence.player.AttributePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AttributeFinderModel extends BaseFinderModel<AttributePersistenceObject> {

	private final String libraryName;
	private final String libraryAuthor;
	private final String attributeName;
	private final String attributeDescription;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to get library items from.
	 * @param languageService A @see LanguageService to get display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 */
	public AttributeFinderModel(LibraryService libraryService, 
								ILanguageService languageService,
								IDialogService dialogService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.ATTRIBUTE_NAME), 
							languageService.getValue(DisplayStrings.ATTRIBUTE_DESCRIPTION), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
				Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		any = languageService.getValue(DisplayStrings.ANY);
		attributeName = languageService.getValue(DisplayStrings.ATTRIBUTE_NAME);
		attributeDescription = languageService.getValue(DisplayStrings.ATTRIBUTE_DESCRIPTION);
		libraryName = languageService.getValue(DisplayStrings.LIBRARY_NAME);
		libraryAuthor = languageService.getValue(DisplayStrings.LIBRARY_AUTHOR);
		libraryScope = languageService.getValue(DisplayStrings.LIBRARY);
		
		this.libraryService = libraryService;
		this.dialogService = dialogService;
		this.languageService = languageService;
	}
	
	@Override
	protected void search(String text) {
		if (text.equals(helpText().get())) {
			return;
		}
		
		List<AttributePersistenceObject> found = new ArrayList<AttributePersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<AttributePersistenceObject> findInLibrary(String text, String searchField) {
		List<AttributePersistenceObject> retVal = new ArrayList<AttributePersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.attributes());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.attributes());
				continue;
			}
			
			for (AttributePersistenceObject att : item.attributes()) {
				if ((searchField.equals(attributeName) || searchField.equals(any)) && att.objectName().contains(text)) {
					retVal.add(att);
				} else if ((searchField.equals(attributeDescription) || searchField.equals(any)) && att.description().contains(text)) {
					retVal.add(att);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(AttributePersistenceObject item) {
		LogRunner.logger().log(Level.INFO, String.format("Inspecting attribute %s", item.objectName()));
		AttributeViewer view = new AttributeViewer(new NamedPersistenceObjectModel(item), languageService);
		view.setDisable(true);
		dialogService.displayModal(view, item.objectName());
	}
}
