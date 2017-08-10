package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.search.ItemFinderModel;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class ItemFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private PlayerPersistenceObject player1;
	
	private ItemPersistenceObject itm1;
	private ItemPersistenceObject itm2;
	private ItemPersistenceObject itm3;
	private ItemPersistenceObject itm4;
	
	private ItemFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		itm1 = mock(ItemPersistenceObject.class);
		when(itm1.itemName()).thenReturn("Potion");
		when(itm1.itemDescription()).thenReturn("Restores HP");
		when(item1.items()).thenReturn(Arrays.asList(itm1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		itm2 = mock(ItemPersistenceObject.class);
		when(itm2.itemName()).thenReturn("Either");
		when(itm2.itemDescription()).thenReturn("Restores MP");
		itm3 = mock(ItemPersistenceObject.class);
		when(itm3.itemName()).thenReturn("Elixer");
		when(itm3.itemDescription()).thenReturn("Restores HP and MP");
		when(item2.items()).thenReturn(Arrays.asList(itm2, itm3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.ITEM_NAME)).thenReturn("Item Name");
		when(languageService.getValue(DisplayStrings.ITEM_DESCRIPTION)).thenReturn("Item Description");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		
		player1 = mock(PlayerPersistenceObject.class);
		InventoryPersistenceObject inv = mock(InventoryPersistenceObject.class);
		itm4 = mock(ItemPersistenceObject.class);
		when(itm4.itemName()).thenReturn("Sock");
		when(itm4.itemDescription()).thenReturn("A sock");
		when(inv.items()).thenReturn(Arrays.asList(itm4));
		when(player1.inventory()).thenReturn(inv);
	}
	
	@Test
	public void testCreateWithoutPlayer() {
		model = new ItemFinderModel(libraryService, languageService);
		assertNotNull(model);
	}

	@Test
	public void testCreateWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		assertNotNull(model);
	}
	
	@Test
	public void testScopesWithoutPlayer() {
		model = new ItemFinderModel(libraryService, languageService);
		assertTrue(model.scope().list().contains("Library"));
	}
	
	@Test
	public void testScopesWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		
		assertTrue(model.scope().list().contains("Library"));
		assertTrue(model.scope().list().contains("Player"));
		assertTrue(model.scope().list().contains("Any"));
	}
	
	@Test
	public void testFields() {
		model = new ItemFinderModel(libraryService, languageService);
		assertTrue(model.fields().list().contains("Any"));
		assertTrue(model.fields().list().contains("Library Name"));
		assertTrue(model.fields().list().contains("Library Author"));
		assertTrue(model.fields().list().contains("Item Name"));
		assertTrue(model.fields().list().contains("Item Description"));
	}
	
	@Test
	public void testSearchHelpText() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("Help");
		
		assertTrue(model.results().list().size() == 0);
	}
	
	@Test
	public void testSearchAnyFieldWithoutPlayer() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("P");
		assertTrue(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchAnyFieldWithPlayerAndAnyScope() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		model.fields().selected().set("Any");
		model.scope().selected().set("Any");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("P");
		assertTrue(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchLibraryName() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchLibraryNameWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Any");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchLibraryAuthorWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Any");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchName() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Item Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Potion");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Either");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Elixer");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Sock");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchNameWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		model.fields().selected().set("Item Name");
		model.scope().selected().set("Any");
		
		model.searchText().set("Potion");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Either");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Elixer");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Sock");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertTrue(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchDescription() {
		model = new ItemFinderModel(libraryService, languageService);
		model.fields().selected().set("Item Description");
		model.scope().selected().set("Library");
		
		model.searchText().set("Restores HP");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Restores MP");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Restores HP and MP");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("A sock");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
	}
	
	@Test
	public void testSearchDescriptionWithPlayer() {
		model = new ItemFinderModel(libraryService, player1, languageService);
		model.fields().selected().set("Item Description");
		model.scope().selected().set("Any");
		
		model.searchText().set("Restores HP");
		assertTrue(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Restores MP");
		assertFalse(model.results().list().contains(itm1));
		assertTrue(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("Restores HP and MP");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertTrue(model.results().list().contains(itm3));
		assertFalse(model.results().list().contains(itm4));
		
		model.searchText().set("A sock");
		assertFalse(model.results().list().contains(itm1));
		assertFalse(model.results().list().contains(itm2));
		assertFalse(model.results().list().contains(itm3));
		assertTrue(model.results().list().contains(itm4));
	}
}
