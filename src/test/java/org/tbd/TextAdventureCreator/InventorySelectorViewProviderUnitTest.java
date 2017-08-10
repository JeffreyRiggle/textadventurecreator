package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.player.InventorySelectorViewProvider;
import textadventurelib.persistence.player.InventoryPersistenceObject;

public class InventorySelectorViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private InventoryPersistenceObject inventory;
	private String selected;
	private ILanguageService languageService;
	
	private InventorySelectorViewProvider provider;
	
	@Before
	public void setup() {
		inventory = mock(InventoryPersistenceObject.class);
		selected = "Test";
		languageService = mock(ILanguageService.class);
		
		provider = new InventorySelectorViewProvider(inventory, selected, languageService);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
