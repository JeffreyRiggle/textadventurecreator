package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.CharacteristicViewProvider;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

public class CharacteristicViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private CharacteristicPersistenceObject characteristic;
	private ILanguageService languageService;
	
	private CharacteristicViewProvider provider;
	
	@Before
	public void setup() {
		characteristic = mock(CharacteristicPersistenceObject.class);
		when(characteristic.valueType()).thenReturn("string");
		when(characteristic.value()).thenReturn("Test");
		
		languageService = mock(ILanguageService.class);
		
		provider = new CharacteristicViewProvider(characteristic, languageService);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
