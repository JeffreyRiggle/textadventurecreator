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
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.SettingsMenuItem;
import ilusr.textadventurecreator.settings.ISettingsViewRepository;
import javafx.event.ActionEvent;
import javafx.scene.Scene;

public class SettingsMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService;
	private ILanguageService languageService;
	private ISettingsViewRepository repository;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private SettingsMenuItem item;
	
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.SETTINGS)).thenReturn("Settings");
		repository = mock(ISettingsViewRepository.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		item = new SettingsMenuItem(languageService, repository, dialogService, styleService, urlProvider);
	}
	
	@Test
	public void testPress() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(dialogService, times(1)).displayModal(any(Scene.class), eq("Settings"));
	}

	@Test
	public void testDisplay() {
		assertEquals("Settings", item.textProperty().get());
	}
}
