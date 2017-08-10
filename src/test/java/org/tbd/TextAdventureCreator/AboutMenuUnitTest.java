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
import ilusr.textadventurecreator.about.AboutView;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.AboutMenuItem;
import javafx.event.ActionEvent;

public class AboutMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService;
	private IDialogService dialogService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private AboutMenuItem item;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.ABOUT)).thenReturn("About");
		
		dialogService = mock(IDialogService.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		item = new AboutMenuItem(languageService, dialogService, styleService, urlProvider);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(dialogService, times(1)).displayModal(any(AboutView.class), eq("About"));
	}

	@Test
	public void testDisplay() {
		assertEquals("About", item.textProperty().get());
	}
}
