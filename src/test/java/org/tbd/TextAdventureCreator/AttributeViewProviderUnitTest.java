package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.AttributeViewProvider;
import textadventurelib.persistence.player.AttributePersistenceObject;

public class AttributeViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private AttributePersistenceObject attribute;
	private ILanguageService languageService;
	
	private AttributeViewProvider provider;
	
	@Before
	public void setup() {
		attribute = mock(AttributePersistenceObject.class);
		when(attribute.value()).thenReturn("Test");
		when(attribute.valueType()).thenReturn("string");
		
		languageService = mock(ILanguageService.class);
		
		provider = new AttributeViewProvider(attribute, languageService);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
