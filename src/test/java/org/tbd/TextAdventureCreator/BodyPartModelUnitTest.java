package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.BodyPartModel;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class BodyPartModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private BodyPartPersistenceObject bodyPart; 
	private LibraryService libraryService;
	private IDialogService dialogService;
	private PlayerPersistenceObject player;
	private ILanguageService languageService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private BodyPartModel model;
	
	@Before
	public void setup() {
		bodyPart = mock(BodyPartPersistenceObject.class);
		libraryService = mock(LibraryService.class);
		dialogService = mock(IDialogService.class);
		player = mock(PlayerPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.BODY_PART_NAME)).thenReturn("Body Part Name");
		when(languageService.getValue(DisplayStrings.BODY_PART_DESCRIPTION)).thenReturn("Body Part Description");
		when(languageService.getValue(DisplayStrings.CHARACTERISTICS)).thenReturn("Characteristics");
		
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new BodyPartModel(bodyPart, libraryService, dialogService, player, languageService, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testName() {
		assertNull(model.name().get());
		model.name().set("TestPart");
		assertEquals("TestPart", model.name().get());
		verify(bodyPart, times(1)).objectName("TestPart");
	}

	@Test
	public void testDescription() {
		assertNull(model.description().get());
		model.description().set("desc");
		assertEquals("desc", model.description().get());
		verify(bodyPart, times(1)).description("desc");
	}
	
	@Test
	public void testAllowLibraryWithoutLibrary() {
		BodyPartModel bmodel = new BodyPartModel(bodyPart, null, dialogService, player, languageService, dialogProvider, styleService, urlProvider);
		assertFalse(bmodel.allowLibraryAdd());
	}
	
	@Test
	public void testAllowLibraryWithLibrary() {
		assertTrue(model.allowLibraryAdd());
	}
	
	@Test
	public void testBodyNameText() {
		assertEquals(model.bodyNameText().get(), "Body Part Name");
	}
	
	@Test
	public void testBodyDescriptionText() {
		assertEquals(model.bodyDescriptionText().get(), "Body Part Description");
	}
	
	@Test
	public void testCharacteristicText() {
		assertEquals(model.characteristicText().get(), "Characteristics");
	}
	
	@Test
	public void testCharacteristicKey() {
		assertNotNull(model.addKey());
	}
	
	@Test
	public void testAddCharacteristic() {
		model.addCharacteristic();
		assertEquals(1, model.characteristics().size());
		verify(bodyPart, times(1)).addCharacteristic(any());
	}
	
	@Test
	public void testAddFromLibrary() {
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addFromLibrary();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(dialog);
	}
	
	@Test
	public void testPersistence() {
		assertNotNull(model.data().get());
	}
}
