package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.InventoryItem;
import ilusr.textadventurecreator.views.player.InventoryItemViewProvider;

public class InventoryItemViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService service;
	private InventoryItem item;
	private ILanguageService languageService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private InventoryItemViewProvider provider;
	private IDialogProvider dialogProvider;
	
	@Before
	public void setup() {
		service = mock(IDialogService.class);
		item = mock(InventoryItem.class);
		languageService = mock(ILanguageService.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		provider = new InventoryItemViewProvider(service, item, languageService, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
