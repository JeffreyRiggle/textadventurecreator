package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.trigger.TriggerModel;
import textadventurelib.persistence.TriggerPersistenceObject;

public class TriggerModelUnitTest {

	private TriggerPersistenceObject trigger;
	private ILanguageService languageService;
	
	private TriggerModel model;
	
	@Before
	public void setup() {
		trigger = mock(TriggerPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.TYPE)).thenReturn("Type");
		
		model = new TriggerModel(trigger, languageService);
	}
	
	@Test
	public void testTypes() {
		assertEquals(4, model.types().list().size());
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(trigger, model.persistenceObject().get());
	}

	@Test
	public void testTypeText() {
		assertEquals("Type", model.typeText().get());
	}
	
	@Test
	public void testLanguageService() {
		assertEquals(languageService, model.getLanguageService());
	}
	
	@Test
	public void testMultiPartId() {
		assertEquals("Multi-Part", model.multiPartId());
	}
	
	@Test
	public void testTextId() {
		assertEquals("Text", model.textId());
	}
	
	@Test
	public void testPlayerId() {
		assertEquals("Player", model.playerId());
	}
	
	@Test
	public void testScriptId() {
		assertEquals("Script", model.scriptId());
	}
}
