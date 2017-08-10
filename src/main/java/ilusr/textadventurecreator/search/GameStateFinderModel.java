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
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.gamestate.GameStateInspector;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.GameStatePersistenceObject;
/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateFinderModel extends BaseFinderModel<GameStatePersistenceObject> {

	private final String gsName;
	private final String libraryName;
	private final String libraryAuthor;
	private final String any;
	
	private final String libraryScope;
	
	private final LibraryService libraryService;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final ActionViewFactory actionViewFactory;
	private final TriggerViewFactory triggerViewFactory;
	private final IDialogProvider dialogProvider;
	private final InternalURLProvider urlProvider;
	private final IStyleContainerService styleService;
	
	/**
	 * 
	 * @param libraryService A @see LibraryService to provide library items.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param actionViewFactory A @see ActionViewFactory to create action views.
	 * @param triggerViewFactory A @see TriggerViewFactory to create trigger views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param urlProvider A @see InternalURLProvider to provide internal resources.
	 * @param styleService service to manage styles.
	 */
	public GameStateFinderModel(LibraryService libraryService, 
								ILanguageService languageService,
								IDialogService dialogService,
								ActionViewFactory actionViewFactory,
								TriggerViewFactory triggerViewFactory,
								IDialogProvider dialogProvider,
								InternalURLProvider urlProvider,
								IStyleContainerService styleService) {
		super(Arrays.asList(languageService.getValue(DisplayStrings.ANY), 
							languageService.getValue(DisplayStrings.GAME_STATE), 
							languageService.getValue(DisplayStrings.LIBRARY_NAME), 
							languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)), 
			  Arrays.asList(languageService.getValue(DisplayStrings.LIBRARY)), languageService);
		
		gsName = languageService.getValue(DisplayStrings.GAME_STATE);
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
		
		List<GameStatePersistenceObject> found = new ArrayList<GameStatePersistenceObject>();
		String searchScope = scope().selected().get();
		String searchField = fields().selected().get();
		
		if (searchScope.equals(libraryScope) || searchScope.equals(any)) {
			found.addAll(findInLibrary(text, searchField));
		}
		
		results().list().clear();
		results().list().addAll(found);
	}
	
	private List<GameStatePersistenceObject> findInLibrary(String text, String searchField) {
		List<GameStatePersistenceObject> retVal = new ArrayList<GameStatePersistenceObject>();
		for (LibraryItem item : libraryService.getItems()) {
			if ((searchField.equals(libraryName) || searchField.equals(any)) && item.getLibraryName().contains(text)) {
				retVal.addAll(item.gameStates());
				continue;
			}
			
			if ((searchField.equals(libraryAuthor) || searchField.equals(any)) && item.getAuthor().contains(text)) {
				retVal.addAll(item.gameStates());
				continue;
			}
			
			for (GameStatePersistenceObject gameState : item.gameStates()) {
				if ((searchField.equals(gsName) || searchField.equals(any)) && gameState.stateId().contains(text)) {
					retVal.add(gameState);
				}
			}
		}
		
		return retVal;
	}

	@Override
	protected void inspect(GameStatePersistenceObject item) {
		//TODO: Create gamestate inspector view.
		LogRunner.logger().log(Level.INFO, String.format("Inspecting game state %s", item.stateId()));
		GameStateModel model = new GameStateModel(item, dialogService, null, null, null, null,
				languageService, null, dialogProvider, styleService, urlProvider);
		dialogService.displayModal(new GameStateInspector(model, languageService, dialogService, actionViewFactory, triggerViewFactory, dialogProvider, styleService, urlProvider), item.stateId());
	}
}
