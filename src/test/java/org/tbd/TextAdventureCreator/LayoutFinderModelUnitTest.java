package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.LayoutFinderModel;
import textadventurelib.persistence.LayoutPersistenceObject;

public class LayoutFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private InternalURLProvider urlProvider;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private LayoutPersistenceObject layout1;
	private LayoutPersistenceObject layout2;
	private LayoutPersistenceObject layout3;
	
	private LayoutFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		layout1 = mock(LayoutPersistenceObject.class);
		when(layout1.id()).thenReturn("Layout1");
		when(item1.layouts()).thenReturn(Arrays.asList(layout1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		layout2 = mock(LayoutPersistenceObject.class);
		when(layout2.id()).thenReturn("Layout2");
		layout3 = mock(LayoutPersistenceObject.class);
		when(layout3.id()).thenReturn("Layout3");
		when(item2.layouts()).thenReturn(Arrays.asList(layout2, layout3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.LAYOUT)).thenReturn("Layout");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new LayoutFinderModel(libraryService, languageService, dialogService, urlProvider);
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
		assertTrue(model.fields().list().contains("Layout"));
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
		
		assertTrue(model.results().list().contains(layout1));
		assertTrue(model.results().list().contains(layout2));
		assertTrue(model.results().list().contains(layout3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(layout1));
		assertTrue(model.results().list().contains(layout2));
		assertTrue(model.results().list().contains(layout3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(layout1));
		assertFalse(model.results().list().contains(layout2));
		assertFalse(model.results().list().contains(layout3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(layout1));
		assertTrue(model.results().list().contains(layout2));
		assertTrue(model.results().list().contains(layout3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(layout1));
		assertFalse(model.results().list().contains(layout2));
		assertFalse(model.results().list().contains(layout3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(layout1));
		assertTrue(model.results().list().contains(layout2));
		assertTrue(model.results().list().contains(layout3));
	}
	
	@Test
	public void testSearchId() {
		model.fields().selected().set("Layout");
		model.scope().selected().set("Library");
		
		model.searchText().set("Layout1");
		assertTrue(model.results().list().contains(layout1));
		assertFalse(model.results().list().contains(layout2));
		assertFalse(model.results().list().contains(layout3));
		
		model.searchText().set("Layout2");
		assertFalse(model.results().list().contains(layout1));
		assertTrue(model.results().list().contains(layout2));
		assertFalse(model.results().list().contains(layout3));
		
		model.searchText().set("Layout3");
		assertFalse(model.results().list().contains(layout1));
		assertFalse(model.results().list().contains(layout2));
		assertTrue(model.results().list().contains(layout3));
	}
}
