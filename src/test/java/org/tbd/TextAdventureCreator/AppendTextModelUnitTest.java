package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.AppendTextModel;
import textadventurelib.persistence.AppendTextActionPersistence;

public class AppendTextModelUnitTest {

	private AppendTextActionPersistence action;
	private ILanguageService languageService;
	
	private AppendTextModel model;
	
	@Before
	public void setup() {
		action = mock(AppendTextActionPersistence.class);
		when(action.appendText()).thenReturn("Text to append");
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.APPEND_TEXT)).thenReturn("Append Text");
		
		model = new AppendTextModel(action, languageService);
	}
	
	@Test
	public void testAppendText() {
		assertEquals("Text to append", model.appendText().get());
		model.appendText().set("New Append Text");
		assertEquals("New Append Text", model.appendText().get());
		verify(action, times(1)).appendText("New Append Text");
	}

	@Test
	public void testLabelText() {
		assertEquals("Append Text", model.labelText().get());
	}
	
	@Test
	public void testPersistence() {
		assertEquals(action, model.getPersistenceObject());
	}
}
