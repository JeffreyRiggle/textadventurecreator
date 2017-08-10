package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.ILayoutService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.LibrariesMenuItem;
import ilusr.textadventurecreator.views.BluePrintNames;
import javafx.event.ActionEvent;

public class LibrariesMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILayoutService layoutService;
	private ILanguageService languageService;
	
	private LibrariesMenuItem item;
	
	@Before
	public void setup() {
		layoutService = mock(ILayoutService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARIES)).thenReturn("Libraries");
		
		item = new LibrariesMenuItem(layoutService, languageService);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(layoutService, times(1)).addTab(BluePrintNames.Library);
	}

	@Test
	public void testDisplay() {
		assertEquals("Libraries", item.textProperty().get());
	}
}
