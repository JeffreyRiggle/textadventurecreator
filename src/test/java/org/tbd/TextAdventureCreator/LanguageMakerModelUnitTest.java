package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.core.io.FileUtilities;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.language.LanguageItem;
import ilusr.textadventurecreator.language.LanguageMakerModel;

public class LanguageMakerModelUnitTest {

	private final static String SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TCC/ja.txt";

	private ILanguageService languageService;

	private LanguageMakerModel model;

	@Before
	public void setup() {
		languageService = Mockito.mock(ILanguageService.class);
		Mockito.when(languageService.getValue(DisplayStrings.LANGUAGE_CODE)).thenReturn("Language Code");
		Mockito.when(languageService.getValue(DisplayStrings.LANGUAGE_NAME)).thenReturn("Language Name");
		Mockito.when(languageService.getValue(DisplayStrings.KEYWORD)).thenReturn("Keyword");
		Mockito.when(languageService.getValue(DisplayStrings.US_ENGLISH_VALUE)).thenReturn("US English");
		Mockito.when(languageService.getValue(DisplayStrings.NEW_VALUE)).thenReturn("New Value");

		model = new LanguageMakerModel(languageService);
	}

	@AfterClass
	public static void teardown() {
		try {
			File createdFile = new File(SAVE_LOCATION);
			if (createdFile.exists()) {
				createdFile.delete();
			}
		} catch (Exception e) { }
	}

	@Test
	public void testCode() {
		assertEquals(null, model.code().get());
		model.code().set("ja-JP");
		assertEquals("ja-JP", model.code().get());
	}

	@Test
	public void testName() {
		assertEquals(null, model.name().get());
		model.name().set("日本語");
		assertEquals("日本語", model.name().get());
	}

	@Test
	public void testCodeText() {
		assertEquals("Language Code", model.codeText().get());
	}

	@Test
	public void testNameText() {
		assertEquals("Language Name", model.nameText().get());
	}

	@Test
	public void testKeywordText() {
		assertEquals("Keyword", model.keywordText().get());
	}

	@Test
	public void testUsEnglishText() {
		assertEquals("US English", model.usEnglishText().get());
	}

	@Test
	public void testValueText() {
		assertEquals("New Value", model.valueText().get());
	}

	@Test
	public void testItems() {
		assertTrue(model.items().size() > 1);
	}

	@Test
	public void testBuild() {
		model.code().set("ja-JP");
		model.name().set("日本語");

		for (LanguageItem item : model.items()) {
			item.setNewValue("テスト");
		}

		model.buildFile(SAVE_LOCATION);
		File savedFile = new File(SAVE_LOCATION);
		assertTrue(savedFile.exists());
		try {
			assertTrue(FileUtilities.getFileContentWithReturns(savedFile).contains(";=;テスト"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateFromFile() {
		model.code().set("ja-JP");
		model.name().set("日本語");

		for (LanguageItem item : model.items()) {
			item.setNewValue("テスト");
		}

		model.buildFile(SAVE_LOCATION);
		File savedFile = new File(SAVE_LOCATION);
		assertTrue(savedFile.exists());

		LanguageMakerModel newModel = new LanguageMakerModel(savedFile, languageService);
		assertEquals("ja-JP", newModel.code().get());
		assertEquals("日本語", newModel.name().get());
		for (LanguageItem item : newModel.items()) {
			assertEquals("テスト", item.getNewValue());
		}
	}
}
