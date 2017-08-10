package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.ExecutionActionModel;
import textadventurelib.persistence.ExecutionActionPersistence;

public class ExecutionActionModelUnitTest {

	private ExecutionActionPersistence action;
	private ILanguageService languageService;
	
	private ExecutionActionModel model;
	
	@Before
	public void setup() {
		action = mock(ExecutionActionPersistence.class);
		when(action.executable()).thenReturn("Testing.exe");
		when(action.blocking()).thenReturn(true);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.EXECUTABLE)).thenReturn("Executable");
		when(languageService.getValue(DisplayStrings.BLOCKING)).thenReturn("Blocking");
		when(languageService.getValue(DisplayStrings.YES)).thenReturn("Yes");
		when(languageService.getValue(DisplayStrings.NO)).thenReturn("No");
		
		model = new ExecutionActionModel(action, languageService);
	}
	
	@Test
	public void testExecutable() {
		assertEquals("Testing.exe", model.executable().get());
		model.executable().set("Chrome.exe");
		assertEquals("Chrome.exe", model.executable().get());
		verify(action, times(1)).executable("Chrome.exe");
	}

	@Test
	public void testExeText() {
		assertEquals("Executable", model.exeText().get());
	}
	
	@Test
	public void testBlockingText() {
		assertEquals("Blocking", model.blockingText().get());
	}
	
	@Test
	public void testYesTest() {
		assertEquals("Yes", model.yesText().get());
	}
	
	@Test
	public void testNoText() {
		assertEquals("No", model.noText().get());
	}
	
	@Test
	public void testBlocking() {
		assertTrue(model.blocking().get());
		model.blocking().set(false);
		assertFalse(model.blocking().get());
		verify(action, times(1)).blocking(false);
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(action, model.getPersistenceObject());
	}
}
