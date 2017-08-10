package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.trigger.TextTriggerModel;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.TextTriggerPersistenceObject;

public class TextTriggerModelUnitTest {

	private TextTriggerPersistenceObject trigger;
	private ILanguageService languageService;
	
	private TextTriggerModel model;
	
	@Before
	public void setup() {
		trigger = mock(TextTriggerPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.TEXT)).thenReturn("Text");
		when(languageService.getValue(DisplayStrings.CASE_SENSITIVE)).thenReturn("Case Sensitive");
		when(languageService.getValue(DisplayStrings.TYPE)).thenReturn("Type");
		when(languageService.getValue(DisplayStrings.YES)).thenReturn("Yes");
		when(languageService.getValue(DisplayStrings.NO)).thenReturn("No");
		
		model = new TextTriggerModel(trigger, languageService);
	}
	
	@Test
	public void testText() {
		model.text().set("Test");
		assertEquals("Test", model.text().get());
		verify(trigger, times(1)).text("Test");
	}

	@Test
	public void testCaseSensitive() {
		model.caseSensitive().set(true);
		assertTrue(model.caseSensitive().get());
		verify(trigger, times(1)).caseSensitive(true);
	}
	
	@Test
	public void testContains() {
		model.matchType().selected().set("Contains");
		verify(trigger, times(1)).matchType(MatchType.Contains);
	}
	
	@Test
	public void testExact() {
		model.matchType().selected().set("Exact");
		verify(trigger, times(1)).matchType(MatchType.Exact);
	}
	
	@Test
	public void testNotContains() {
		model.matchType().selected().set("Does not Contain");
		verify(trigger, times(1)).matchType(MatchType.NotContains);
	}
	
	@Test
	public void testPostfix() {
		model.matchType().selected().set("Suffix");
		verify(trigger, times(1)).matchType(MatchType.Postfix);
	}
	
	@Test
	public void testPrefix() {
		model.matchType().selected().set("Prefix");
		verify(trigger, times(1)).matchType(MatchType.Prefix);
	}
	
	@Test
	public void testTextText() {
		assertEquals("Text", model.textText().get());
	}
	
	@Test
	public void testCaseText() {
		assertEquals("Case Sensitive", model.caseText().get());
	}
	
	@Test
	public void testTypeText() {
		assertEquals("Type", model.typeText().get());
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
