package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryItemBluePrint;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;

public class LibraryItemBluePrintUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService;
    private TriggerViewFactory triggerFactory; 
    private ActionViewFactory actionFactory;
    private PlayerModProviderFactory playerModFactory;
    private ILanguageService languageService;
    private LibraryService libraryService;
    private InternalURLProvider urlProvider;
    private IDialogProvider dialogProvider;
    private IStyleContainerService styleService;
    
	private LibraryItemBluePrint bluePrint;
	private LibraryItem item;
	
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		triggerFactory = mock(TriggerViewFactory.class);
		actionFactory = mock(ActionViewFactory.class);
		playerModFactory = mock(PlayerModProviderFactory.class);
		languageService = mock(ILanguageService.class);
		libraryService = mock(LibraryService.class);
		urlProvider = mock(InternalURLProvider.class);
		item = mock(LibraryItem.class);
		when(item.getAuthor()).thenReturn("tester");
		when(item.getLibraryName()).thenReturn("test");
		
		when(libraryService.getItems()).thenReturn(Arrays.asList(item));
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		
		bluePrint = new LibraryItemBluePrint(dialogService, triggerFactory, actionFactory,
				playerModFactory, languageService, libraryService, urlProvider, dialogProvider, styleService);
	}

	@Test
	public void testCreateWithIdentifier() {
		ITabContent tab = bluePrint.create("LibraryItemName: test;=;tester");
		assertNotNull(tab);
	}
}
