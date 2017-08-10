package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.FinishActionModel;
import textadventurelib.persistence.FinishActionPersistenceObject;

public class FinishActionModelUnitTest {

	private FinishActionPersistenceObject action;
	private ILanguageService languageService;
	
	private FinishActionModel model;
	
	@Before
	public void setup() {
		action = mock(FinishActionPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.FINISH_ACTION_TEXT)).thenReturn("Finish Action");
		
		model = new FinishActionModel(action, languageService);
	}
	
	@Test
	public void testFinishText() {
		assertEquals("Finish Action", model.finishText().get());
	}

	@Test
	public void testPersistence() {
		assertEquals(action, model.getPersistenceObject());
	}
}
