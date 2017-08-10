package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.CompletionActionModel;
import textadventurelib.persistence.CompletionActionPersistence;

public class CompletionActionModelUnitTest {

	private CompletionActionPersistence action;
	private ILanguageService languageService;
	
	private CompletionActionModel model;
	
	@Before
	public void setup() {
		action = mock(CompletionActionPersistence.class);
		when(action.completionData()).thenReturn("GS1");
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.STATE_TO_MOVE_TO)).thenReturn("State to move to");
		
		model = new CompletionActionModel(action, languageService);
	}
	
	@Test
	public void testCompletionData() {
		assertEquals("GS1", model.completionData().get());
		model.completionData().set("GS3");
		assertEquals("GS3", model.completionData().get());
		verify(action, times(1)).completionData("GS3");
	}

	@Test
	public void testStateText() {
		assertEquals("State to move to", model.stateText().get());
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(action, model.getPersistenceObject());
	}
}
