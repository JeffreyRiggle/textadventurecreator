package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import ilusr.core.interfaces.Callback;
import ilusr.core.test.FXThread;
import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.library.LibraryViewModel;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class LibraryViewModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private LibraryService libraryService; 
	private ILayoutService layoutService; 
	private IDialogService dialogService;
	private TriggerViewFactory triggerFactory;
	private ActionViewFactory actionFactory;
	private PlayerModProviderFactory playerModFactory;
	private ILanguageService languageService;
	private InternalURLProvider urlProvider;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	
	@Captor
	private ArgumentCaptor<Callback<LibraryItem>> cbackCaptor;
	
	private LibraryViewModel model;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		libraryService = mock(LibraryService.class);
		layoutService = mock(ILayoutService.class);
		dialogService = mock(IDialogService.class);
		triggerFactory = mock(TriggerViewFactory.class);
		actionFactory = mock(ActionViewFactory.class);
		playerModFactory = mock(PlayerModProviderFactory.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARIES)).thenReturn("Libraries");
		when(languageService.getValue(DisplayStrings.IMPORT)).thenReturn("Import");
		when(languageService.getValue(DisplayStrings.EXPORT)).thenReturn("Export");
		
		urlProvider = mock(InternalURLProvider.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		
		model = new LibraryViewModel(libraryService, layoutService, dialogService, triggerFactory, actionFactory, playerModFactory, languageService, urlProvider, dialogProvider, styleService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}
	
	@Test
	public void testRemoveItem() {
		model.getAddAction().handle(mock(ActionEvent.class));
		
		assertTrue(model.items().size() > 0);
		LibraryItem item = model.items().get(0);
		model.removeLibraryItem(item);
		verify(libraryService, times(1)).removeLibraryItem(item);
		assertFalse(model.items().contains(item));
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testImport() {
		LibraryItem item = mock(LibraryItem.class);
		
		model.importLibrary("c:/test/test.talim");
		verify(libraryService, times(1)).importLibrary(eq("c:/test/test.talim"), cbackCaptor.capture());
		cbackCaptor.getValue().execute(item);
		
		waitForPlatform();
		assertTrue(model.items().contains(item));
	}
	
	@Test
	public void testExport() {
		LibraryItem item = mock(LibraryItem.class);
		
		model.exportLibrary("c:/test/test.talim", item);
		verify(libraryService, times(1)).exportLibrary("c:/test/test.talim", item, null);
	}
	
	@Test
	public void testAddKey() {
		assertNotNull(model.addItemKey());
	}
	
	@Test
	public void testEditItem() {
		LibraryItem item = mock(LibraryItem.class);
		when(item.getLibraryName()).thenReturn("Test");
		when(item.getAuthor()).thenReturn("Tester");
		
		model.getEditAction().execute(item);
		verify(layoutService, times(1)).addTab(BluePrintNames.LibraryItem, "LibraryItemName: Test;=;Tester");
	}
	
	@Test
	public void testAddItemAction() {
		model.getAddAction().handle(mock(ActionEvent.class));
		
		verify(libraryService, times(1)).importLibrary(any());
		verify(layoutService, times(1)).addTab(any(ITabContent.class));
		assertTrue(model.items().size() > 0);
	}
	
	@Test
	public void testLibraryText() {
		assertEquals("Libraries", model.libraryText().get());
	}
	
	@Test
	public void testImportText() {
		assertEquals("Import", model.importText().get());
	}
	
	@Test
	public void testExportText() {
		assertEquals("Export", model.exportText().get());
	}
	
	private void waitForPlatform() {
		final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	latch.countDown();
        });
        
        try {
        	latch.await();
        } catch (Exception e) { }
	}
}
