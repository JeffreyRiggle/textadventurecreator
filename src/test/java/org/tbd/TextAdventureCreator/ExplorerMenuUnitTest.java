package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.ITabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.menus.ExplorerMenuItem;
import ilusr.textadventurecreator.shell.LayoutApplicationService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.BluePrintNames;
import javafx.event.ActionEvent;

public class ExplorerMenuUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider; 
	private ILayoutService layoutService;
	private ILanguageService languageService;
	private LayoutApplicationService layoutApplicationService;
	private ITabContent tab;
	
	private ExplorerMenuItem item;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		layoutService = mock(ILayoutService.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.EXPLORER)).thenReturn("Explorer");
		
		layoutApplicationService = mock(LayoutApplicationService.class);
		when(layoutService.addTab(BluePrintNames.Explorer)).thenReturn("UID");
		tab = mock(ITabContent.class);
		
		when(layoutService.getTabContent("UID")).thenReturn(tab);
		
		item = new ExplorerMenuItem(provider, layoutService, languageService, layoutApplicationService);
	}
	
	@Test
	public void testToggleOn() {
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(layoutService, times(1)).addTab(BluePrintNames.Explorer);
		assertTrue(item.selectedProperty().get());
	}

	@Test
	public void testToggleOff() {
		item.getOnAction().handle(mock(ActionEvent.class));
		item.getOnAction().handle(mock(ActionEvent.class));
		verify(layoutService, times(1)).removeTab("UID");
		assertFalse(item.selectedProperty().get());
	}
	
	@Test
	public void testTabClosed() {
		item.getOnAction().handle(mock(ActionEvent.class));
		item.close();
		assertFalse(item.selectedProperty().get());
	}
	
	@Test
	public void testDisplay() {
		assertEquals("Explorer", item.textProperty().get());
	}
}
