package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.gamestate.CompletionTimerViewCreator;
import textadventurelib.persistence.CompletionTimerPersistenceObject;

public class CompletionTimerViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	
	private CompletionTimerViewCreator creator;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		
		creator = new CompletionTimerViewCreator(languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(creator.create(mock(CompletionTimerPersistenceObject.class)));
	}

}
