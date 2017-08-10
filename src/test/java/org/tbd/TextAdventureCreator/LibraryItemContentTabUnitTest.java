package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryItemContentTab;
import ilusr.textadventurecreator.library.LibraryItemModel;
import ilusr.textadventurecreator.library.LibraryItemView;
import javafx.beans.property.SimpleStringProperty;

public class LibraryItemContentTabUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private LibraryItemView item;
	private LibraryItemModel model;
	private SimpleStringProperty author;
	private SimpleStringProperty name;
	
	private ILanguageService languageService;
	
	private LibraryItemContentTab tab;
	
	@Before
	public void setup() {
		author = new SimpleStringProperty();
		name = new SimpleStringProperty();
		
		model = mock(LibraryItemModel.class);
		when(model.author()).thenReturn(author);
		when(model.name()).thenReturn(name);
		
		item = mock(LibraryItemView.class);
		when(item.model()).thenReturn(model);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LIBRARY_ITEM)).thenReturn("Library Item");
		
		tab = new LibraryItemContentTab(item, languageService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(tab);
	}
	
	@Test
	public void testToolTip() {
		assertEquals("Library Item", tab.toolTip().get());
	}

	@Test
	public void testClose() {
		assertTrue(tab.canClose().get());
	}
	
	@Test
	public void testCustomData() {
		assertNull(tab.customData().get());
		
		author.set("tester");
		assertNull(tab.customData().get());
		
		name.set("test");
		assertEquals("LibraryItemName: test;=;tester", tab.customData().get());
	}
}
