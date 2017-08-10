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
import ilusr.textadventurecreator.search.BodyPartFinderModel;
import ilusr.textadventurecreator.views.IDialogProvider;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class BodyPartFinderModelUnitTest {
	
	private LibraryService libraryService; 
	private ILanguageService languageService;
	private IDialogService dialogService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private LibraryItem item1;
	private LibraryItem item2;
	
	private PlayerPersistenceObject player1;
	
	private BodyPartPersistenceObject bp1;
	private BodyPartPersistenceObject bp2;
	private BodyPartPersistenceObject bp3;
	private BodyPartPersistenceObject bp4;
	
	private BodyPartFinderModel model;
	
	@Before
	public void setup() {
		libraryService = mock(LibraryService.class);
		item1 = mock(LibraryItem.class);
		bp1 = mock(BodyPartPersistenceObject.class);
		when(bp1.objectName()).thenReturn("Head");
		when(bp1.description()).thenReturn("The players head");
		when(item1.bodyParts()).thenReturn(Arrays.asList(bp1));
		when(item1.getLibraryName()).thenReturn("Lib1");
		when(item1.getAuthor()).thenReturn("Tester");
		
		item2 = mock(LibraryItem.class);
		bp2 = mock(BodyPartPersistenceObject.class);
		when(bp2.objectName()).thenReturn("Left Arm");
		when(bp2.description()).thenReturn("The players left arm");
		bp3 = mock(BodyPartPersistenceObject.class);
		when(bp3.objectName()).thenReturn("Right Arm");
		when(bp3.description()).thenReturn("The players right arm");
		when(item2.bodyParts()).thenReturn(Arrays.asList(bp2, bp3));
		when(item2.getLibraryName()).thenReturn("Lib2");
		when(item2.getAuthor()).thenReturn("Producer");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item1, item2));
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_NAME)).thenReturn("Library Name");
		when(languageService.getValue(DisplayStrings.LIBRARY_AUTHOR)).thenReturn("Library Author");
		when(languageService.getValue(DisplayStrings.BODY_PART_NAME)).thenReturn("Body Part Name");
		when(languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION)).thenReturn("Body Part Description");
		when(languageService.getValue(DisplayStrings.ANY)).thenReturn("Any");
		when(languageService.getValue(DisplayStrings.LIBRARY)).thenReturn("Library");
		when(languageService.getValue(DisplayStrings.PLAYER)).thenReturn("Player");
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		dialogService = mock(IDialogService.class);
		
		player1 = mock(PlayerPersistenceObject.class);
		bp4 = mock(BodyPartPersistenceObject.class);
		when(bp4.objectName()).thenReturn("Left foot");
		when(bp4.description()).thenReturn("The players left foot");
		when(player1.bodyParts()).thenReturn(Arrays.asList(bp4));
		
		dialogProvider = mock(IDialogProvider.class);
		urlProvider = mock(InternalURLProvider.class);
		styleService = mock(IStyleContainerService.class);
	}
	
	@Test
	public void testCreateWithoutPlayer() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		assertNotNull(model);
	}

	@Test
	public void testCreateWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		assertNotNull(model);
	}
	
	@Test
	public void testScopesWithoutPlayer() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		assertTrue(model.scope().list().contains("Library"));
	}
	
	@Test
	public void testScopesWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		
		assertTrue(model.scope().list().contains("Library"));
		assertTrue(model.scope().list().contains("Player"));
		assertTrue(model.scope().list().contains("Any"));
	}
	
	@Test
	public void testFields() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		assertTrue(model.fields().list().contains("Any"));
		assertTrue(model.fields().list().contains("Library Name"));
		assertTrue(model.fields().list().contains("Library Author"));
		assertTrue(model.fields().list().contains("Body Part Name"));
		assertTrue(model.fields().list().contains("Body Part Description"));
	}
	
	@Test
	public void testSearchHelpText() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("Help");
		
		assertTrue(model.results().list().size() == 0);
	}
	
	@Test
	public void testSearchAnyFieldWithoutPlayer() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Any");
		model.scope().selected().set("Library");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchAnyFieldWithPlayerAndAnyScope() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Any");
		model.scope().selected().set("Any");
		model.searchText().set("L");
		
		assertTrue(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertTrue(model.results().list().contains(bp4));
		
		model.searchText().set("P");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchLibraryName() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Library");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchLibraryNameWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Library Name");
		model.scope().selected().set("Any");
		model.searchText().set("Lib1");
		
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Lib2");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchLibraryAuthor() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Library");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchLibraryAuthorWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Library Author");
		model.scope().selected().set("Any");
		
		model.searchText().set("Tester");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Producer");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchName() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Body Part Name");
		model.scope().selected().set("Library");
		
		model.searchText().set("Head");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Left Arm");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Right Arm");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Left foot");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchNameWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Body Part Name");
		model.scope().selected().set("Any");
		
		model.searchText().set("Head");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Left Arm");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Right Arm");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("Left foot");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertTrue(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchDescription() {
		model = new BodyPartFinderModel(libraryService, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Body Part Description");
		model.scope().selected().set("Library");
		
		model.searchText().set("The players head");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players left arm");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players right arm");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players left foot");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
	}
	
	@Test
	public void testSearchDescriptionWithPlayer() {
		model = new BodyPartFinderModel(libraryService, player1, languageService, dialogService, dialogProvider, styleService, urlProvider);
		model.fields().selected().set("Body Part Description");
		model.scope().selected().set("Any");
		
		model.searchText().set("The players head");
		assertTrue(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players left arm");
		assertFalse(model.results().list().contains(bp1));
		assertTrue(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players right arm");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertTrue(model.results().list().contains(bp3));
		assertFalse(model.results().list().contains(bp4));
		
		model.searchText().set("The players left foot");
		assertFalse(model.results().list().contains(bp1));
		assertFalse(model.results().list().contains(bp2));
		assertFalse(model.results().list().contains(bp3));
		assertTrue(model.results().list().contains(bp4));
	}
}
