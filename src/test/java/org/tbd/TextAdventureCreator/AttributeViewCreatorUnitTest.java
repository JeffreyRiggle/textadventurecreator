package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.AttributeViewCreator;
import javafx.scene.Node;
import textadventurelib.persistence.player.AttributePersistenceObject;

public class AttributeViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	
	private AttributeViewCreator creator;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		
		creator = new AttributeViewCreator(languageService);
	}
	
	@Test
	public void testCreate() {
		AttributePersistenceObject att = mock(AttributePersistenceObject.class);
		when(att.value()).thenReturn("Test");
		when(att.valueType()).thenReturn("string");
		Node node = creator.create(att);
		assertNotNull(node);
		assertEquals(node, creator.create(att));
	}

}
