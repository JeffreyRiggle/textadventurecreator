package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.layout.LayoutCreatorContentTab;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import ilusr.textadventurecreator.views.layout.LayoutCreatorView;
import javafx.beans.property.SimpleStringProperty;

public class LayoutCreatorContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private LayoutCreatorView view;
	private LayoutCreatorModel model;
	private SimpleStringProperty id;
	private ILanguageService languageService;
	
	private LayoutCreatorContentTab tab;
	
	@Before
	public void setup() {
		id = new SimpleStringProperty("LayoutID");
		model = mock(LayoutCreatorModel.class);
		when(model.id()).thenReturn(id);
		view = mock(LayoutCreatorView.class);
		when(view.model()).thenReturn(model);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LAYOUT)).thenReturn("Layout");
		when(languageService.getValue(DisplayStrings.LAYOUT_INFO)).thenReturn("Layout Info");
		
		tab = new LayoutCreatorContentTab(view, languageService);
	}
	
	@Test
	public void testContent() {
		assertEquals(view, tab.content().get());
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Layout Info LayoutID", tab.toolTip().get());
	}

	@Test
	public void testClose() {
		assertTrue(tab.canClose().get());
	}
	
	@Test
	public void testCustomData() {
		assertEquals("LayoutName: LayoutID", tab.customData().get());
	}
}
