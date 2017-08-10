package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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
import ilusr.textadventurecreator.views.player.ItemModel;
import javafx.beans.property.SimpleBooleanProperty;
import textadventurelib.persistence.player.ItemPersistenceObject;

public class ItemModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ItemPersistenceObject item; 
	private LibraryService libraryService; 
	private IDialogService dialogService;
	private ILanguageService languageService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private ItemModel model;
	
	@Before
	public void setup() {
		item = mock(ItemPersistenceObject.class);
		libraryService = mock(LibraryService.class);
		dialogService = mock(IDialogService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.ITEM_NAME)).thenReturn("Item Name");
		when(languageService.getValue(DisplayStrings.ITEM_DESCRIPTION)).thenReturn("Item Description");
		when(languageService.getValue(DisplayStrings.PROPERTIES)).thenReturn("Properties");
		
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new ItemModel(item, libraryService, dialogService, languageService, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testName() {
		assertNull(model.name().get());
		model.name().set("ItemTest");
		assertEquals("ItemTest", model.name().get());
		verify(item, times(1)).itemName("ItemTest");
	}

	@Test
	public void testDescription() {
		assertNull(model.description().get());
		model.description().set("desc");
		assertEquals("desc", model.description().get());
		verify(item, times(1)).itemDescription("desc");
	}
	
	@Test
	public void testAllowLibraryAddWithoutLibrary() {
		ItemModel bmodel = new ItemModel(item, null, dialogService, languageService, dialogProvider, styleService, urlProvider);
		assertFalse(bmodel.allowLibraryAdd());
	}
	
	@Test
	public void testAllowLibraryAddWithLibrary() {
		assertTrue(model.allowLibraryAdd());
	}
	
	@Test
	public void testNameText() {
		assertEquals("Item Name", model.nameText().get());
	}
	
	@Test
	public void testDescriptionText() {
		assertEquals("Item Description", model.descriptionText().get());
	}
	
	@Test
	public void testPropertyText() {
		assertEquals("Properties", model.propertyText().get());
	}
	
	@Test
	public void testAddPropertyKey() {
		assertNotNull(model.addKey());
	}
	
	@Test
	public void testAddProperty() {
		model.addProperty();
		assertEquals(1, model.properties().size());
		verify(item, times(1)).addProperty(any());
	}
	
	@Test
	public void testAddPropertyFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addPropertyFromLibrary();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
}
