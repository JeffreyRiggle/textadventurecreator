package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.gamestate.CompletionTimerModel;
import textadventurelib.persistence.CompletionTimerPersistenceObject;

public class CompletionTimerModelUnitTest {

	private CompletionTimerPersistenceObject timer;
	private ILanguageService languageService;
	
	private CompletionTimerModel model;
	
	@Before
	public void setup() {
		timer = mock(CompletionTimerPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.STATE_TO_MOVE_TO)).thenReturn("State to move to");
		when(languageService.getValue(DisplayStrings.TIME_IN_MS)).thenReturn("Time in ms");
		
		model = new CompletionTimerModel(timer, languageService);
	}
	
	@Test
	public void testCreateWithData() {
		CompletionTimerPersistenceObject tim = mock(CompletionTimerPersistenceObject.class);
		when(tim.duration()).thenReturn(1000l);
		when(tim.completionData()).thenReturn("GS1");
		
		CompletionTimerModel mod = new CompletionTimerModel(tim, languageService);
		assertEquals(1000, mod.duration().get().longValue());
		assertEquals("GS1", mod.completion().get());
	}
	
	@Test
	public void testCompletion() {
		assertNull(model.completion().get());
		model.completion().set("GS2");
		assertEquals("GS2", model.completion().get());
		verify(timer, times(1)).completionData("GS2");
	}

	@Test
	public void testDuration() {
		assertEquals(0, model.duration().get().longValue());
		model.duration().set(1500l);
		assertEquals(1500, model.duration().get().longValue());
		verify(timer, times(1)).duration(1500);
	}
	
	@Test
	public void testStateText() {
		assertEquals("State to move to", model.stateText().get());
	}
	
	@Test
	public void testTimeText() {
		assertEquals("Time in ms", model.timeText().get());
	}
}
