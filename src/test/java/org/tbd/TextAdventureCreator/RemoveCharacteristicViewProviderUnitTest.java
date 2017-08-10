package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.RemoveCharacteristicViewProvider;
import textadventurelib.persistence.player.BodyPartPersistenceObject;

public class RemoveCharacteristicViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private BodyPartPersistenceObject bodyPart; 
	private ILanguageService languageService;
	
	private RemoveCharacteristicViewProvider provider;
	
	@Before
	public void setup() {
		bodyPart = mock(BodyPartPersistenceObject.class);
		languageService = mock(ILanguageService.class);
		
		provider = new RemoveCharacteristicViewProvider(bodyPart, languageService);
	}
	
	@Test
	public void testProvide() {
		assertNotNull(provider.getView());
	}

}
