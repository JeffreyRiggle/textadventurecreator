package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.GameStateFinderModel;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.GameStatePersistenceObject;

public class GameStateFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private ActionViewFactory actionViewFactory;
	private TriggerViewFactory triggerViewFactory;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private GameStatePersistenceObject gs1;
	private GameStatePersistenceObject gs2;
	private GameStatePersistenceObject gs3;
	
	private GameStateFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		gs1 = mock(GameStatePersistenceObject.class);
		when(gs1.stateId()).thenReturn("GameState1");
		when(item1.gameStates()).thenReturn(Arrays.asList(gs1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		gs2 = mock(GameStatePersistenceObject.class);
		when(gs2.stateId()).thenReturn("GameState2");
		gs3 = mock(GameStatePersistenceObject.class);
		when(gs3.stateId()).thenReturn("GameState3");
		when(item2.gameStates()).thenReturn(Arrays.asList(gs2, gs3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.GAME_STATE)).thenReturn("Game State");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		actionViewFactory = mock(ActionViewFactory.class);
		triggerViewFactory = mock(TriggerViewFactory.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new GameStateFinderModel(libraryService, languageService, dialogService, actionViewFactory, triggerViewFactory,
				dialogProvider, urlProvider, styleService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}

	@Test
	public void testScopes() {
		assertTrue(model.scope().list().contains("Library"));
	}
	
	@Test
	public void testFields() {
		assertTrue(model.fields().list().contains("Any"));
		assertTrue(model.fields().list().contains("Library Name"));
		assertTrue(model.fields().list().contains("Library Author"));
		assertTrue(model.fields().list().contains("Game State"));
	}
	
	@Test
	public void testSearchHelpText() {
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("Help");
		
		assertTrue(model.results().list().size() == 0);
	}
	
	@Test
	public void testSearchAnyField() {
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(gs1));
		assertTrue(model.results().list().contains(gs2));
		assertTrue(model.results().list().contains(gs3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(gs1));
		assertTrue(model.results().list().contains(gs2));
		assertTrue(model.results().list().contains(gs3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(gs1));
		assertFalse(model.results().list().contains(gs2));
		assertFalse(model.results().list().contains(gs3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(gs1));
		assertTrue(model.results().list().contains(gs2));
		assertTrue(model.results().list().contains(gs3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(gs1));
		assertFalse(model.results().list().contains(gs2));
		assertFalse(model.results().list().contains(gs3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(gs1));
		assertTrue(model.results().list().contains(gs2));
		assertTrue(model.results().list().contains(gs3));
	}
	
	@Test
	public void testSearchId() {
		model.fields().selected().set("Game State");
		model.scope().selected().set("Library");
		
		model.searchText().set("GameState1");
		assertTrue(model.results().list().contains(gs1));
		assertFalse(model.results().list().contains(gs2));
		assertFalse(model.results().list().contains(gs3));
		
		model.searchText().set("GameState2");
		assertFalse(model.results().list().contains(gs1));
		assertTrue(model.results().list().contains(gs2));
		assertFalse(model.results().list().contains(gs3));
		
		model.searchText().set("GameState3");
		assertFalse(model.results().list().contains(gs1));
		assertFalse(model.results().list().contains(gs2));
		assertTrue(model.results().list().contains(gs3));
	}
}
