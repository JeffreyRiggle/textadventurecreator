package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.TriggerFinderModel;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.TriggerPersistenceObject;

public class TriggerFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private TriggerViewFactory triggerViewFactory;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private TriggerPersistenceObject trigger1;
	private TriggerPersistenceObject trigger2;
	private TriggerPersistenceObject trigger3;
	
	private TriggerFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		trigger1 = mock(TriggerPersistenceObject.class);
		when(trigger1.type()).thenReturn("Text");
		when(item1.triggers()).thenReturn(Arrays.asList(trigger1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		trigger2 = mock(TriggerPersistenceObject.class);
		when(trigger2.type()).thenReturn("Player");
		trigger3 = mock(TriggerPersistenceObject.class);
		when(trigger3.type()).thenReturn("Player");
		when(item2.triggers()).thenReturn(Arrays.asList(trigger2, trigger3));
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
		triggerViewFactory = mock(TriggerViewFactory.class);
		
		model = new TriggerFinderModel(libraryService, languageService, dialogService, triggerViewFactory);
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
		
		assertTrue(model.results().list().contains(trigger1));
		assertTrue(model.results().list().contains(trigger2));
		assertTrue(model.results().list().contains(trigger3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(trigger1));
		assertTrue(model.results().list().contains(trigger2));
		assertTrue(model.results().list().contains(trigger3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(trigger1));
		assertFalse(model.results().list().contains(trigger2));
		assertFalse(model.results().list().contains(trigger3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(trigger1));
		assertTrue(model.results().list().contains(trigger2));
		assertTrue(model.results().list().contains(trigger3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(trigger1));
		assertFalse(model.results().list().contains(trigger2));
		assertFalse(model.results().list().contains(trigger3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(trigger1));
		assertTrue(model.results().list().contains(trigger2));
		assertTrue(model.results().list().contains(trigger3));
	}
	
	@Test
	public void testSearchType() {
		model.fields().selected().set("Action Type");
		model.scope().selected().set("Library");
		
		model.searchText().set("Text");
		assertTrue(model.results().list().contains(trigger1));
		assertFalse(model.results().list().contains(trigger2));
		assertFalse(model.results().list().contains(trigger3));
		
		model.searchText().set("Player");
		assertFalse(model.results().list().contains(trigger1));
		assertTrue(model.results().list().contains(trigger2));
		assertTrue(model.results().list().contains(trigger3));
	}
}
