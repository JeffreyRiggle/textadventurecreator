package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.RemovePlayerPropertyViewProvider;

public class RemovePlayerPropertyViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	
	private RemovePlayerPropertyViewProvider provider;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		
		provider = new RemovePlayerPropertyViewProvider(languageService);
	}
	
	@Test
	public void testProvide() {
		assertNotNull(provider.getView());
	}

}
