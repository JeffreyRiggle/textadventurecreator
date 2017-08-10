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
import ilusr.textadventurecreator.search.PropertyFinderModel;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class PropertyFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private PropertyPersistenceObject prop1;
	private PropertyPersistenceObject prop2;
	private PropertyPersistenceObject prop3;
	
	private PropertyFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		prop1 = mock(PropertyPersistenceObject.class);
		when(prop1.objectName()).thenReturn("Breakable");
		when(prop1.description()).thenReturn("If the item can break");
		when(item1.properties()).thenReturn(Arrays.asList(prop1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		prop2 = mock(PropertyPersistenceObject.class);
		when(prop2.objectName()).thenReturn("Consumable");
		when(prop2.description()).thenReturn("If the item can be consumed");
		prop3 = mock(PropertyPersistenceObject.class);
		when(prop3.objectName()).thenReturn("Key");
		when(prop3.description()).thenReturn("If the item is a key");
		when(item2.properties()).thenReturn(Arrays.asList(prop2, prop3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.PROPERTY_NAME)).thenReturn("Property Name");
		when(languageService.getValue(DisplayStrings.PROPERTY_DESCRIPTION)).thenReturn("Property Description");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		
		model = new PropertyFinderModel(libraryService, languageService, dialogService);
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
		assertTrue(model.fields().list().contains("Property Name"));
		assertTrue(model.fields().list().contains("Property Description"));
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
		
		assertTrue(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
	}
	
	@Test
	public void testSearchName() {
		model.fields().selected().set("Property Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Breakable");
		assertTrue(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("Consumable");
		assertFalse(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("Key");
		assertFalse(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
	}
	
	@Test
	public void testSearchDescription() {
		model.fields().selected().set("Property Description");
		model.scope().selected().set("Library");
		
		model.searchText().set("If the item can break");
		assertTrue(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("If the item can be consumed");
		assertFalse(model.results().list().contains(prop1));
		assertTrue(model.results().list().contains(prop2));
		assertFalse(model.results().list().contains(prop3));
		
		model.searchText().set("If the item is a key");
		assertFalse(model.results().list().contains(prop1));
		assertFalse(model.results().list().contains(prop2));
		assertTrue(model.results().list().contains(prop3));
	}
}
