package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.ScriptedActionModel;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

public class ScriptedActionModelUnitTest {

	private ScriptedActionPersistenceObject action;
	private ILanguageService languageService;
	
	private ScriptedActionModel model;
	
	@Before
	public void setup() {
		action = mock(ScriptedActionPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.ACTION_SCRIPT)).thenReturn("Action Script");
		when(languageService.getValue(DisplayStrings.VIEW_IN_EDITOR)).thenReturn("View in editor");
		
		model = new ScriptedActionModel(action, languageService);
	}
	
	@Test
	public void testCreateWithData() {
		ScriptedActionPersistenceObject act = mock(ScriptedActionPersistenceObject.class);
		when(act.getScript()).thenReturn("function execute(executionParameters) {\n //test \n}");
		
		ScriptedActionModel mod = new ScriptedActionModel(act, languageService);
		assertEquals("function execute(executionParameters) {\n //test \n}", mod.script().get());
	}

	@Test
	public void testScript() {
		assertEquals("function execute(executionParameters) {\r\n  \r\n}\r\n", model.script().get());
		model.script().set("function execute(executionParameters) {\n //test \n}");
		assertEquals("function execute(executionParameters) {\n //test \n}", model.script().get());
		verify(action, times(1)).setScript("function execute(executionParameters) {\n //test \n}");
	}
	
	@Test
	public void testActionText() {
		assertEquals("Action Script", model.actionText().get());
	}
	
	@Test
	public void testViewText() {
		assertEquals("View in editor", model.viewText().get());
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(action, model.getPersistenceObject());
	}
}
