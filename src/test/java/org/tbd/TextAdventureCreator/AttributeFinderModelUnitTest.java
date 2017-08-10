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
import ilusr.textadventurecreator.search.AttributeFinderModel;
import textadventurelib.persistence.player.AttributePersistenceObject;

public class AttributeFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private AttributePersistenceObject att1;
	private AttributePersistenceObject att2;
	private AttributePersistenceObject att3;
	
	private AttributeFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		att1 = mock(AttributePersistenceObject.class);
		when(att1.objectName()).thenReturn("Age");
		when(att1.description()).thenReturn("How old the player is");
		when(item1.attributes()).thenReturn(Arrays.asList(att1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		att2 = mock(AttributePersistenceObject.class);
		when(att2.objectName()).thenReturn("Class");
		when(att2.description()).thenReturn("What the players class is");
		att3 = mock(AttributePersistenceObject.class);
		when(att3.objectName()).thenReturn("Profession");
		when(att3.description()).thenReturn("The players profession");
		when(item2.attributes()).thenReturn(Arrays.asList(att2, att3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.ATTRIBUTE_NAME)).thenReturn("Name");
		when(languageService.getValue(DisplayStrings.ATTRIBUTE_DESCRIPTION)).thenReturn("Description");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		
		model = new AttributeFinderModel(libraryService, languageService, dialogService);
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
		assertTrue(model.fields().list().contains("Name"));
		assertTrue(model.fields().list().contains("Description"));
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
		
		assertTrue(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
		
		model.searchText().set("Pr");
		assertFalse(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
	}
	
	@Test
	public void testSearchAttributeName() {
		model.fields().selected().set("Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Age");
		assertTrue(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("Class");
		assertFalse(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("Profession");
		assertFalse(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
	}
	
	@Test
	public void testSearchAttributeDescription() {
		model.fields().selected().set("Description");
		model.scope().selected().set("Library");
		
		model.searchText().set("How old the player is");
		assertTrue(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("What the players class is");
		assertFalse(model.results().list().contains(att1));
		assertTrue(model.results().list().contains(att2));
		assertFalse(model.results().list().contains(att3));
		
		model.searchText().set("The players profession");
		assertFalse(model.results().list().contains(att1));
		assertFalse(model.results().list().contains(att2));
		assertTrue(model.results().list().contains(att3));
	}
}
