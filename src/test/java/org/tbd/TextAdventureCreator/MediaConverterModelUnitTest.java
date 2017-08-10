package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.gamestate.MediaConverterModel;

public class MediaConverterModelUnitTest {

	private File originalFile;
	private ILanguageService languageService;
	
	private MediaConverterModel model;
	
	@Before
	public void setup() {
		originalFile = mock(File.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.UNABLE_TO_CONVERT)).thenReturn("Unable to convert");
		when(languageService.getValue(DisplayStrings.CONVERSION_SUCCESS)).thenReturn("Sucess");
		when(languageService.getValue(DisplayStrings.COULD_NOT_USE_FILE)).thenReturn("Could not use file");
		when(languageService.getValue(DisplayStrings.FILE_TYPE)).thenReturn("File Type");
		when(languageService.getValue(DisplayStrings.NEW_FILE_NAME)).thenReturn("New File name");
		when(languageService.getValue(DisplayStrings.CONVERT)).thenReturn("Convert");
		
		model = new MediaConverterModel(originalFile, languageService);
	}
	
	@Test
	public void testTypes() {
		assertTrue(model.types().list().contains("Audio"));
		assertTrue(model.types().list().contains("Video"));
		assertTrue(model.types().list().contains("Image"));
	}

	@Test
	public void testNewFile() {
		assertNull(model.newFile().get());
		model.newFile().set("c:/test/testfile.txt");
		assertEquals(model.newFile().get(), "c:/test/testfile.txt");
	}
	
	@Test
	public void testErrorText() {
		assertEquals("Unable to convert", model.errorText().get());
	}
	
	@Test
	public void testOkText() {
		assertEquals("Sucess", model.okText().get());
	}
	
	@Test
	public void testTitleText() {
		assertEquals("Could not use file", model.titleText().get());
	}
	
	@Test
	public void testTypeText() {
		assertEquals("File Type", model.typeText().get());
	}
	
	@Test
	public void testNewFileText() {
		assertEquals("New File name", model.newFileText().get());
	}
	
	@Test
	public void testConvertText() {
		assertEquals("Convert", model.convertText().get());
	}
	
	@Test
	public void testValid() {
		assertFalse(model.valid().get());
	}
	
	@Test
	public void testConverting() {
		assertFalse(model.converting().get());
	}
	
	@Test
	public void testNewFileLocation() {
		when(originalFile.getAbsolutePath()).thenReturn("C:\\test\\ofile.avi");
		model.newFile().set("testfile");
		model.types().selected().set("Video");
		assertEquals("C:\\test\\testfile.mp4", model.newFileLocation());
	}
}
