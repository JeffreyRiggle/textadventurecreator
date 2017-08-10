package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.SaveActionModel;
import textadventurelib.persistence.SaveActionPersistenceObject;

public class SaveActionModelUnitTest {

	private SaveActionPersistenceObject action;
	private ILanguageService languageService;
	
	private SaveActionModel model;
	
	@Before
	public void setup() {
		action = mock(SaveActionPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SAVE_LOCATION)).thenReturn("Save Location");
		when(languageService.getValue(DisplayStrings.BLOCKING)).thenReturn("Blocking");
		when(languageService.getValue(DisplayStrings.YES)).thenReturn("Yes");
		when(languageService.getValue(DisplayStrings.NO)).thenReturn("No");
		
		model = new SaveActionModel(action, languageService);
	}
	
	@Test
	public void testCreateWithData() {
		SaveActionPersistenceObject saction = mock(SaveActionPersistenceObject.class);
		when(saction.blocking()).thenReturn(true);
		when(saction.saveLocation()).thenReturn("./files/save.xml");
		
		SaveActionModel mod = new SaveActionModel(saction, languageService);
		assertEquals("./files/save.xml", mod.saveLocation().get());
		assertTrue(mod.blocking().get());
	}

	@Test
	public void testSaveLocation() {
		assertEquals("./save/save1.xml", model.saveLocation().get());
		model.saveLocation().set("./save/something.xml");
		assertEquals("./save/something.xml", model.saveLocation().get());
		verify(action, times(1)).saveLocation("./save/something.xml");
	}
	
	@Test
	public void testBlocking() {
		assertFalse(model.blocking().get());
		model.blocking().set(true);
		assertTrue(model.blocking().get());
		verify(action, times(1)).blocking(true);
	}
	
	@Test
	public void testSaveLocationText() {
		assertEquals("Save Location", model.saveLocationText().get());
	}
	
	@Test
	public void testBlockingText() {
		assertEquals("Blocking", model.blockingText().get());
	}
	
	@Test
	public void testYesText() {
		assertEquals("Yes", model.yesText().get());
	}
	
	@Test
	public void testNoText() {
		assertEquals("No", model.noText().get());
	}
}
