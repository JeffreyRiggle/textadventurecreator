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
import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.ReportIssueMenuItem;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;

public class ReportMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ILanguageService languageService; 
	private IDialogService dialogService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	private IDialogProvider dialogProvider;
	private IReportIssueService reportIssueService;
	
	private ReportIssueMenuItem item;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.REPORT_ISSUE)).thenReturn("Report Issue");
		dialogService = mock(IDialogService.class);
		urlProvider = mock(InternalURLProvider.class);
		dialogProvider = mock(IDialogProvider.class);
		reportIssueService = mock(IReportIssueService.class);
		
		item = new ReportIssueMenuItem(languageService, dialogService, styleService, urlProvider, dialogProvider, reportIssueService);
	}
	
	@Test
	public void testPress() {
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		
		when(dialogProvider.create(any())).thenReturn(dialog);
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModeless(any(Dialog.class), eq("Report Issue"));
	}

	@Test
	public void testDisplay() {
		assertEquals("Report Issue", item.textProperty().get());
	}
}
