package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.LanguageSettingsModel;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;

public class LanguageSettingsModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	private IDialogService dialogService;
	private IDialogProvider dialogProvider;
	
	private LanguageSettingsModel model;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.APPLY)).thenReturn("Apply");
		when(languageService.getValue(DisplayStrings.ADD)).thenReturn("Add");
		when(languageService.getValue(DisplayStrings.LANGUAGE_SETTINGS)).thenReturn("Language Settings");
		when(languageService.getValue(DisplayStrings.LANGUAGES)).thenReturn("Languages");
		when(languageService.getValue(DisplayStrings.EDIT)).thenReturn("Edit");
		Map<String, String> langMap = new HashMap<String, String>();
		langMap.put("en-US", "English");
		langMap.put("ja-JP", "Japanese");
		when(languageService.getLanguages()).thenReturn(langMap);
		
		dialogService = mock(IDialogService.class);
		dialogProvider = mock(IDialogProvider.class);
		
		model = new LanguageSettingsModel(languageService, dialogService, dialogProvider);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
		assertTrue(model.languages().list().contains("English"));
		assertTrue(model.languages().list().contains("Japanese"));
	}

	@Test
	public void testLanguages() {
		assertNull(model.languages().selected().get());
		model.languages().selected().set("English");
		assertEquals(model.languages().selected().get(), "English");
	}
	
	@Test
	public void testApplyText() {
		assertEquals("Apply", model.applyText().get());
	}
	
	@Test
	public void testAddText() {
		assertEquals("Add", model.addText().get());
	}
	
	@Test
	public void testTitleText() {
		assertEquals("Language Settings", model.titleText().get());
	}
	
	@Test
	public void testLangText() {
		assertEquals("Languages", model.langText().get());
	}
	
	@Test
	public void testEditText() {
		assertEquals("Edit", model.editText().get());
	}
	
	@Test
	public void testApply() {
		model.languages().selected().set("Japanese");
		model.apply();
		verify(languageService, times(1)).setLanguage("ja-JP");
	}
	
	@Test
	public void testAdd() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.add();
		verify(dialog, times(1)).setOnComplete(captor.capture());
		verify(dialogService, times(1)).displayModeless(dialog);
	}
	
	@Test
	public void testEdit() {
		File f = null;
		try {
			f = new File(System.getProperty("user.home") + "/ilusr/languages/ja-JPlanguage.txt");
			f.createNewFile();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.languages().selected().set("Japanese");
		model.edit();
		verify(dialog, times(1)).setOnComplete(captor.capture());
		verify(dialogService, times(1)).displayModeless(dialog);
		
		try {
			f.delete();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testEditUS() {
		ArgumentCaptor<Dialog> captor = ArgumentCaptor.forClass(Dialog.class);
		model.languages().selected().set("English");
		model.edit();
		verify(dialogService, times(0)).displayModeless(captor.capture());
	}
}
