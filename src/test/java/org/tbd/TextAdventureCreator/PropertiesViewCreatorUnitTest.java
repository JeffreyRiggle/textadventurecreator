package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.PropertiesViewCreator;
import javafx.scene.Node;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class PropertiesViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	
	private PropertiesViewCreator creator;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		
		creator = new PropertiesViewCreator(languageService);
	}
	
	@Test
	public void testCreate() {
		PropertyPersistenceObject prop = mock(PropertyPersistenceObject.class);
		when(prop.valueType()).thenReturn("string");
		Node node = creator.create(prop);
		assertNotNull(node);
		assertEquals(node, creator.create(prop));
	}

}
