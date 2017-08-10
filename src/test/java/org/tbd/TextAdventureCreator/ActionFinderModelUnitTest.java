package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.ActionFinderModel;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import textadventurelib.persistence.ActionPersistenceObject;

public class ActionFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private ActionViewFactory actionViewFactory;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private ActionPersistenceObject action1;
	private ActionPersistenceObject action2;
	private ActionPersistenceObject action3;
	
	private ActionFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		action1 = mock(ActionPersistenceObject.class);
		when(action1.type()).thenReturn("Finish");
		when(item1.actions()).thenReturn(Arrays.asList(action1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		action2 = mock(ActionPersistenceObject.class);
		when(action2.type()).thenReturn("Complete");
		action3 = mock(ActionPersistenceObject.class);
		when(action3.type()).thenReturn("AppendText");
		when(item2.actions()).thenReturn(Arrays.asList(action2, action3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.ACTION_TYPE)).thenReturn("Action Type");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		actionViewFactory = mock(ActionViewFactory.class);
		
		model = new ActionFinderModel(libraryService, languageService, dialogService, actionViewFactory);
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
		assertTrue(model.fields().list().contains("Action Type"));
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
		
		assertTrue(model.results().list().contains(action1));
		assertTrue(model.results().list().contains(action2));
		assertTrue(model.results().list().contains(action3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(action1));
		assertTrue(model.results().list().contains(action2));
		assertTrue(model.results().list().contains(action3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(action1));
		assertFalse(model.results().list().contains(action2));
		assertFalse(model.results().list().contains(action3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(action1));
		assertTrue(model.results().list().contains(action2));
		assertTrue(model.results().list().contains(action3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(action1));
		assertFalse(model.results().list().contains(action2));
		assertFalse(model.results().list().contains(action3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(action1));
		assertTrue(model.results().list().contains(action2));
		assertTrue(model.results().list().contains(action3));
	}
	
	@Test
	public void testSearchType() {
		model.fields().selected().set("Action Type");
		model.scope().selected().set("Library");
		
		model.searchText().set("Finish");
		assertTrue(model.results().list().contains(action1));
		assertFalse(model.results().list().contains(action2));
		assertFalse(model.results().list().contains(action3));
		
		model.searchText().set("Complete");
		assertFalse(model.results().list().contains(action1));
		assertTrue(model.results().list().contains(action2));
		assertFalse(model.results().list().contains(action3));
		
		model.searchText().set("AppendText");
		assertFalse(model.results().list().contains(action1));
		assertFalse(model.results().list().contains(action2));
		assertTrue(model.results().list().contains(action3));
	}
}
