package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.CharacteristicViewCreator;
import javafx.scene.Node;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

public class CharacteristicViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	
	private CharacteristicViewCreator creator;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		
		creator = new CharacteristicViewCreator(languageService);
	}
	
	@Test
	public void testCreate() {
		CharacteristicPersistenceObject chr = mock(CharacteristicPersistenceObject.class);
		when(chr.valueType()).thenReturn("string");
		when(chr.value()).thenReturn("Test");
		Node node = creator.create(chr);
		assertNotNull(node);
		assertEquals(node, creator.create(chr));
	}

}
