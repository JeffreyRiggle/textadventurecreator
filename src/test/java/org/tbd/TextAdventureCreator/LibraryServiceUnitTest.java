package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.iroshell.services.ApplicationClosingManager;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItem;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class LibraryServiceUnitTest {

	private final static String SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TCC/lib.talim";
	private final static String LIB_SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TCC/mlib.talml";
	private final static String LIB_SAVE_LOCATION_1 = System.getProperty("user.home") + "/ilusr/UnitTests/TCC/mlib1.talml";

	private ProjectStatusService statusService;
	private ISettingsManager settingsManager;
	private ApplicationClosingManager closingManager;
	private TextAdventureProvider textAdventureProvider;
	private ILanguageService languageService;

	private LibraryService service;

	@Before
	public void setup() {
		statusService = mock(ProjectStatusService.class);
		settingsManager = mock(ISettingsManager.class);
		when(settingsManager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true)).thenReturn(true);

		closingManager = mock(ApplicationClosingManager.class);
		textAdventureProvider = mock(TextAdventureProvider.class);
		languageService = mock(ILanguageService.class);

		service = new LibraryService(statusService, settingsManager, closingManager, textAdventureProvider, languageService, LIB_SAVE_LOCATION);
		service.initialize();
	}

	@AfterClass
	public static void cleanup() {
		File file = new File(SAVE_LOCATION);
		if (file.exists()) {
			file.delete();
		}

		File file1 = new File(LIB_SAVE_LOCATION);
		if (file1.exists()) {
			file1.delete();
		}

		File file2 = new File(LIB_SAVE_LOCATION_1);
		if (file2.exists()) {
			file2.delete();
		}
	}

	@Test
	public void testCreate() {
		assertNotNull(service);
	}

	@Test
	public void testImportFromPath() {

	}

	@Test
	public void testImportLibrary() {
		LibraryItem item = mock(LibraryItem.class);
		assertFalse(service.getItems().contains(item));
		service.importLibrary(item);
		assertTrue(service.getItems().contains(item));
		service.importLibrary(item);
		assertTrue(service.getItems().contains(item));
	}

	@Test
	public void testExport() {
		ArgumentCaptor<StatusItem> sItem = ArgumentCaptor.forClass(StatusItem.class);
		LibraryItem item = new LibraryItem();
		item.setAuthor("Tester");
		item.setLibraryName("Test");
		Waiter waiter = new Waiter();
		service.exportLibrary(SAVE_LOCATION, item, waiter);
		verify(statusService, times(1)).addStatusItemToQueue(sItem.capture());
		sItem.getValue().run();
		while(!waiter.ran()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { }
		}

		File file = new File(SAVE_LOCATION);
		assertTrue(file.exists());
	}

	@Test
	public void testExportFromId() {
		ArgumentCaptor<StatusItem> sItem = ArgumentCaptor.forClass(StatusItem.class);
		LibraryItem item = new LibraryItem();
		item.setAuthor("Tester");
		item.setLibraryName("Test");
		Waiter waiter = new Waiter();
		service.importLibrary(item);
		service.exportLibrary(SAVE_LOCATION, "Test", waiter);
		verify(statusService, times(1)).addStatusItemToQueue(sItem.capture());
		sItem.getValue().run();
		while(!waiter.ran()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { }
		}

		File file = new File(SAVE_LOCATION);
		assertTrue(file.exists());
	}

	@Test
	public void testRemoveItem() {
		LibraryItem item = mock(LibraryItem.class);
		service.importLibrary(item);
		assertTrue(service.getItems().contains(item));
		service.removeLibraryItem(item);
		assertFalse(service.getItems().contains(item));
		service.removeLibraryItem(item);
		assertFalse(service.getItems().contains(item));
	}

	@Test
	public void testRemoveItemWithId() {
		LibraryItem item = mock(LibraryItem.class);
		when(item.getLibraryName()).thenReturn("TestLib");
		service.importLibrary(item);
		assertTrue(service.getItems().contains(item));
		service.removeLibraryItem("TestLib");
		assertFalse(service.getItems().contains(item));
		service.removeLibraryItem("TestLib");
		assertFalse(service.getItems().contains(item));
	}

	@Test
	public void testLoadLibraryFromPath() {
		service = new LibraryService(statusService, settingsManager, closingManager, textAdventureProvider, languageService, LIB_SAVE_LOCATION_1);
		service.initialize();

		LibraryItem item = new LibraryItem();
		item.setAuthor("Tester");
		item.setLibraryName("Test");
		service.importLibrary(item);
		service.onClose();

		when(settingsManager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true)).thenReturn(false);
		LibraryService service2 = new LibraryService(statusService, settingsManager, closingManager, textAdventureProvider, languageService);
		service2.initialize();

		service2.loadLibrary(LIB_SAVE_LOCATION_1);
		assertEquals(1, service2.getItems().size());
	}

	@Test
	public void testAddChangeListener() {
		Waiter waiter = new Waiter();
		service.addLibraryChangedListener(waiter);
		service.importLibrary(mock(LibraryItem.class));
		assertTrue(waiter.ran());
	}

	@Test
	public void testRemoveChangeListener() {
		Waiter waiter = new Waiter();
		service.addLibraryChangedListener(waiter);
		service.removeLibraryChangedListener(waiter);
		service.importLibrary(mock(LibraryItem.class));
		assertFalse(waiter.ran());
	}

	@Test
	public void testClose() {
		LibraryItem item = new LibraryItem();
		item.setAuthor("Tester");
		item.setLibraryName("Test");
		service.importLibrary(item);
		service.onClose();

		assertTrue(new File(LIB_SAVE_LOCATION).exists());
	}

	private class Waiter implements Runnable {

		private boolean ran;

		public Waiter() {
			ran = false;
		}

		@Override
		public void run() {
			this.ran = true;
		}

		public boolean ran() {
			return ran;
		}
	}
}
