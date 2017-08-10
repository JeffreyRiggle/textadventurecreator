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
import ilusr.textadventurecreator.search.PlayerFinderModel;
import ilusr.textadventurecreator.views.IDialogProvider;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private PlayerPersistenceObject player1;
	private PlayerPersistenceObject player2;
	private PlayerPersistenceObject player3;
	
	private PlayerFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		player1 = mock(PlayerPersistenceObject.class);
		when(player1.playerName()).thenReturn("Player1");
		when(item1.players()).thenReturn(Arrays.asList(player1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		player2 = mock(PlayerPersistenceObject.class);
		when(player2.playerName()).thenReturn("Player2");
		player3 = mock(PlayerPersistenceObject.class);
		when(player3.playerName()).thenReturn("Player3");
		when(item2.players()).thenReturn(Arrays.asList(player2, player3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.PLAYER_NAME)).thenReturn("Player Name");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new PlayerFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
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
		assertTrue(model.fields().list().contains("Player Name"));
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
		
		assertTrue(model.results().list().contains(player1));
		assertTrue(model.results().list().contains(player2));
		assertTrue(model.results().list().contains(player3));
		
		model.searchText().set("Pr");
		assertFalse(model.results().list().contains(player1));
		assertTrue(model.results().list().contains(player2));
		assertTrue(model.results().list().contains(player3));
	}
	
	@Test
	public void testSearchLibraryName() {
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(player1));
		assertFalse(model.results().list().contains(player2));
		assertFalse(model.results().list().contains(player3));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(player1));
		assertTrue(model.results().list().contains(player2));
		assertTrue(model.results().list().contains(player3));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(player1));
		assertFalse(model.results().list().contains(player2));
		assertFalse(model.results().list().contains(player3));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(player1));
		assertTrue(model.results().list().contains(player2));
		assertTrue(model.results().list().contains(player3));
	}
	
	@Test
	public void testSearchId() {
		model.fields().selected().set("Player Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Player1");
		assertTrue(model.results().list().contains(player1));
		assertFalse(model.results().list().contains(player2));
		assertFalse(model.results().list().contains(player3));
		
		model.searchText().set("Player2");
		assertFalse(model.results().list().contains(player1));
		assertTrue(model.results().list().contains(player2));
		assertFalse(model.results().list().contains(player3));
		
		model.searchText().set("Player3");
		assertFalse(model.results().list().contains(player1));
		assertFalse(model.results().list().contains(player2));
		assertTrue(model.results().list().contains(player3));
	}
}
