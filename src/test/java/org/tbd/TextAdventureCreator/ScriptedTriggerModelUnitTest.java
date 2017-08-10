package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.trigger.ScriptedTriggerModel;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;

public class ScriptedTriggerModelUnitTest {

	private ScriptedTriggerPersistenceObject trigger;
	private ILanguageService languageService;
	
	private ScriptedTriggerModel model;
	
	@Before
	public void setup() {
		trigger = mock(ScriptedTriggerPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SCRIPT)).thenReturn("Script");
		when(languageService.getValue(DisplayStrings.VIEW_IN_EDITOR)).thenReturn("View in Editor");
		
		model = new ScriptedTriggerModel(trigger, languageService);
	}
	
	@Test
	public void testDefaultScript() {
		assertEquals("function shouldFire(triggerParameters) {\r\n\treturn true;\r\n}\r\n", model.script().get());
	}

	
	@Test
	public void testScript() {
		model.script().set("function shouldFire(triggerParameters) {\r\n\treturn false;\r\n}\r\n");
		verify(trigger, times(1)).setScript("function shouldFire(triggerParameters) {\r\n\treturn false;\r\n}\r\n");
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(trigger, model.getPersistenceObject());
	}
	
	@Test
	public void testScriptText() {
		assertEquals("Script", model.scriptText().get());
	}
	
	@Test
	public void testEditText() {
		assertEquals("View in Editor", model.editorText().get());
	}
}
