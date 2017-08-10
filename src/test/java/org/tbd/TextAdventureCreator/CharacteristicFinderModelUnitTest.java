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
import ilusr.textadventurecreator.search.CharacteristicFinderModel;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class CharacteristicFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private PlayerPersistenceObject player1;
	
	private CharacteristicPersistenceObject chr1;
	private CharacteristicPersistenceObject chr2;
	private CharacteristicPersistenceObject chr3;
	private CharacteristicPersistenceObject chr4;
	
	private CharacteristicFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		chr1 = mock(CharacteristicPersistenceObject.class);
		when(chr1.objectName()).thenReturn("Hair Color");
		when(chr1.description()).thenReturn("The players hair color");
		when(item1.characteristics()).thenReturn(Arrays.asList(chr1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		chr2 = mock(CharacteristicPersistenceObject.class);
		when(chr2.objectName()).thenReturn("skin color");
		when(chr2.description()).thenReturn("The players skin color");
		chr3 = mock(CharacteristicPersistenceObject.class);
		when(chr3.objectName()).thenReturn("Hair Style");
		when(chr3.description()).thenReturn("The players hair style");
		when(item2.characteristics()).thenReturn(Arrays.asList(chr2, chr3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.CHARACTERISTIC_NAME)).thenReturn("Characteristic Name");
		when(languageService.getValue(DisplayStrings.CHARACTERISTIC_DESCRIPTION)).thenReturn("Characteristic Description");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		
		player1 = mock(PlayerPersistenceObject.class);
		chr4 = mock(CharacteristicPersistenceObject.class);
		when(chr4.objectName()).thenReturn("Eye Color");
		when(chr4.description()).thenReturn("The players eye color");
		when(player1.characteristics()).thenReturn(Arrays.asList(chr4));
	}
	
	@Test
	public void testCreateWithoutPlayer() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		assertNotNull(model);
	}

	@Test
	public void testCreateWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		assertNotNull(model);
	}
	
	@Test
	public void testScopesWithoutPlayer() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		assertTrue(model.scope().list().contains("Library"));
	}
	
	@Test
	public void testScopesWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		
		assertTrue(model.scope().list().contains("Library"));
		assertTrue(model.scope().list().contains("Player"));
		assertTrue(model.scope().list().contains("Any"));
	}
	
	@Test
	public void testFields() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		assertTrue(model.fields().list().contains("Any"));
		assertTrue(model.fields().list().contains("Library Name"));
		assertTrue(model.fields().list().contains("Library Author"));
		assertTrue(model.fields().list().contains("Characteristic Name"));
		assertTrue(model.fields().list().contains("Characteristic Description"));
	}
	
	@Test
	public void testSearchHelpText() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("Help");
		
		assertTrue(model.results().list().size() == 0);
	}
	
	@Test
	public void testSearchAnyFieldWithoutPlayer() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchAnyFieldWithPlayerAndAnyScope() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Any");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchLibraryName() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchLibraryNameWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Any");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchLibraryAuthorWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Any");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchName() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Characteristic Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Hair Color");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("skin color");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Hair Style");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Eye Color");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchNameWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		model.fields().selected().set("Characteristic Name");
		model.scope().selected().set("Any");
		
		model.searchText().set("Hair Color");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("skin color");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Hair Style");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("Eye Color");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertTrue(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchDescription() {
		model = new CharacteristicFinderModel(libraryService, languageService, dialogService);
		model.fields().selected().set("Characteristic Description");
		model.scope().selected().set("Library");
		
		model.searchText().set("The players hair color");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players skin color");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players hair style");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players eye color");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
	}
	
	@Test
	public void testSearchDescriptionWithPlayer() {
		model = new CharacteristicFinderModel(libraryService, player1, languageService, dialogService);
		model.fields().selected().set("Characteristic Description");
		model.scope().selected().set("Any");
		
		model.searchText().set("The players hair color");
		assertTrue(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players skin color");
		assertFalse(model.results().list().contains(chr1));
		assertTrue(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players hair style");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertTrue(model.results().list().contains(chr3));
		assertFalse(model.results().list().contains(chr4));
		
		model.searchText().set("The players eye color");
		assertFalse(model.results().list().contains(chr1));
		assertFalse(model.results().list().contains(chr2));
		assertFalse(model.results().list().contains(chr3));
		assertTrue(model.results().list().contains(chr4));
	}
}
