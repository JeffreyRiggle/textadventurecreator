package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.search.BaseFinderModel;

public class BaseFinderModelUnitTest {

	private ILanguageService languageService;
	
	private FinderImpl model;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SEARCH_HELP)).thenReturn("Help");
		
		model = new FinderImpl(Arrays.asList("Field1", "Field2"), Arrays.asList("Scope1", "Scope2"), languageService);
	}
	
	@Test
	public void testScopes() {
		assertEquals(2, model.scope().list().size());
		assertEquals("Scope1", model.scope().selected().get());
	}

	@Test
	public void testFields() {
		assertEquals(2, model.fields().list().size());
		assertEquals("Field1", model.fields().selected().get());
	}
	
	@Test
	public void testHelpText() {
		assertEquals("Help", model.helpText().get());
	}
	
	@Test
	public void testSearch() {
		model.searchText().set("Test1");
		assertEquals("Test1", model.lastSearch());
		model.searchText().set("Test2");
		assertEquals("Test2", model.lastSearch());
	}
	
	private class FinderImpl extends BaseFinderModel<String> {

		private String lastSearch;
		
		public FinderImpl(List<String> fields, List<String> scopes, ILanguageService languageService) {
			super(fields, scopes, languageService);
			lastSearch = new String();
		}

		public String lastSearch() {
			return lastSearch;
		}
		
		@Override
		protected void search(String text) {
			lastSearch = text;
		}

		@Override
		protected void inspect(String item) {
			
		}
		
	}
}
