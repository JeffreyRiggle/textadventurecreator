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
import ilusr.textadventurecreator.search.TimerFinderModel;
import textadventurelib.persistence.TimerPersistenceObject;

public class TimerFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private TimerPersistenceObject timer1;
	private TimerPersistenceObject timer2;
	private TimerPersistenceObject timer3;
	
	private TimerFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		timer1 = mock(TimerPersistenceObject.class);
		when(item1.timers()).thenReturn(Arrays.asList(timer1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		timer2 = mock(TimerPersistenceObject.class);
		timer3 = mock(TimerPersistenceObject.class);
		when(item2.timers()).thenReturn(Arrays.asList(timer2, timer3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		
		model = new TimerFinderModel(libraryService, languageService, dialogService);
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
		
		assertTrue(model.results().list().contains(timer1));
		assertTrue(model.results().list().contains(timer2));
		assertTrue(model.results().list().contains(timer3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(timer1));
		assertTrue(model.results().list().contains(timer2));
		assertTrue(model.results().list().contains(timer3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(timer1));
		assertFalse(model.results().list().contains(timer2));
		assertFalse(model.results().list().contains(timer3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(timer1));
		assertTrue(model.results().list().contains(timer2));
		assertTrue(model.results().list().contains(timer3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(timer1));
		assertFalse(model.results().list().contains(timer2));
		assertFalse(model.results().list().contains(timer3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(timer1));
		assertTrue(model.results().list().contains(timer2));
		assertTrue(model.results().list().contains(timer3));
	}
}
