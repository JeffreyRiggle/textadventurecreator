package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.core.i18n.LanguageManager;
import ilusr.core.io.FileUtilities;
import ilusr.textadventurecreator.language.LanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;

public class LanguageServiceUnitTest {

	private final static String UNIT_TEST_LANGUAGE = System.getProperty("user.home") + "/ilusr/UnitTests/TCC/utLang.txt";
	private final String LANG_TEXT = "LanguageCode;=;ut-LangTest\nLanguageDisplay;=;Unit Test Language\nTest;=;test";

	private LanguageManager languageManager;
	private ISettingsManager settingsManager;

	private LanguageService languageService;

	@Before
	public void setup() {
		languageManager = Mockito.mock(LanguageManager.class);
		settingsManager = Mockito.mock(ISettingsManager.class);
		Mockito.when(settingsManager.getStringSetting(SettingNames.LANGUAGE, "en-US")).thenReturn("en-US");

		languageService = new LanguageService(settingsManager, languageManager);

		try {
			FileUtilities.saveToFile(UNIT_TEST_LANGUAGE, LANG_TEXT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void teardown() {
		try {
			File testFile = new File(UNIT_TEST_LANGUAGE);

			if (testFile.exists()) {
				testFile.delete();
			}
		} catch (Exception e) { }
	}

	@Test
	public void testInitialize() {
		assertNotNull(languageService);
		Mockito.verify(settingsManager, Mockito.times(1)).getStringSetting(SettingNames.LANGUAGE, "en-US");
		Mockito.verify(languageManager, Mockito.times(1)).setLanguage("en-US");
	}

	@Test
	public void testInitializeWithSettingValue() {
		Mockito.when(settingsManager.getStringSetting(SettingNames.LANGUAGE, "en-US")).thenReturn("ja-JP");
		LanguageService newLanguageService = new LanguageService(settingsManager, languageManager);
		assertNotNull(newLanguageService);
		Mockito.verify(settingsManager, Mockito.times(2)).getStringSetting(SettingNames.LANGUAGE, "en-US");
		Mockito.verify(languageManager, Mockito.times(1)).setLanguage("ja-JP");
	}

	@Test
	public void testAddLanguage() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		try {
			Mockito.verify(languageManager).addLanguagePack(langFile);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRemoveLanguage() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		languageService.removeLanguage(langFile);
		try {
			Mockito.verify(languageManager).removeLanguagePack("ut-LangTest");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetLanguages() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		assertTrue(languageService.getLanguages().size() > 0);
	}

	@Test
	public void testSetLanguage() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		languageService.setLanguage("ut-LangTest");
		Mockito.verify(languageManager, Mockito.times(1)).setLanguage("ut-LangTest");
		Mockito.verify(settingsManager, Mockito.times(1)).setStringSetting(SettingNames.LANGUAGE, "ut-LangTest");
	}

	@Test
	public void testGetValue() {
		Mockito.when(languageManager.getValue("Test")).thenReturn("test");
		assertEquals("test", languageService.getValue("Test"));
		Mockito.verify(languageManager, Mockito.times(1)).getValue("Test");
	}

	@Test
	public void testAddListener() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		TestRan ran = new TestRan();
		languageService.addListener(ran);
		languageService.setLanguage("ut-LangTest");
		assertTrue(ran.ran());
	}

	@Test
	public void testRemoveListener() {
		File langFile = new File(UNIT_TEST_LANGUAGE);
		languageService.addLanguage(langFile);
		TestRan ran = new TestRan();
		languageService.addListener(ran);
		languageService.removeListener(ran);
		languageService.setLanguage("ut-LangTest");
		assertFalse(ran.ran());
	}

	private class TestRan implements Runnable {

		private boolean ran;

		public TestRan() {
			ran = false;
		}

		@Override
		public void run() {
			ran = true;
		}

		public boolean ran() {
			return ran;
		}
	}
}
