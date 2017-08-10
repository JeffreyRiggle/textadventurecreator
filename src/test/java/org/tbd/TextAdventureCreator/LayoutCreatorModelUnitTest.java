package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.core.interfaces.Callback;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.ISelectable;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.layout.LayoutComponent;
import ilusr.textadventurecreator.views.layout.LayoutComponentView;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.StylePersistenceObject;

public class LayoutCreatorModelUnitTest {

	private LayoutPersistenceObject layout;
	private LayoutGridPersistenceObject grid;
	private StylePersistenceObject style;
	private ILanguageService languageService;
	private IDialogService dialogService;
	private InternalURLProvider provider;
	private SelectionManager selectionManager;
	
	private LayoutCreatorModel model;
	
	@Before
	public void setup() {
		layout = mock(LayoutPersistenceObject.class);
		grid = mock(LayoutGridPersistenceObject.class);
		when(grid.getRows()).thenReturn(0);
		when(grid.getColumns()).thenReturn(0);
		when(layout.getLayout()).thenReturn(grid);
		style = mock(StylePersistenceObject.class);
		when(layout.getStyle()).thenReturn(style);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.LAYOUT_ID)).thenReturn("Layout ID");
		when(languageService.getValue(DisplayStrings.ROWS)).thenReturn("Rows");
		when(languageService.getValue(DisplayStrings.COLUMNS)).thenReturn("Columns");
		when(languageService.getValue(DisplayStrings.TOOLKIT)).thenReturn("Toolkit");
		when(languageService.getValue(DisplayStrings.PROPERTIES)).thenReturn("Properties");
		when(languageService.getValue(DisplayStrings.COMPILE)).thenReturn("Compile");
		
		dialogService = mock(IDialogService.class);
		provider = mock(InternalURLProvider.class);
		selectionManager = mock(SelectionManager.class);
		
		model = new LayoutCreatorModel(layout, languageService, dialogService, provider, selectionManager);
	}

	@Test
	public void testId() {
		assertNull(model.id().get());
		model.id().set("LayoutID");
		assertEquals("LayoutID", model.id().get());
		verify(layout, times(1)).id("LayoutID");
	}
	
	@Test
	public void testRows() {
		assertEquals(0, model.rows().get());
		model.rows().set(2);
		assertEquals(2, model.rows().get());
		verify(grid, times(1)).setRows(2);
	}
	
	@Test
	public void testCols() {
		assertEquals(0, model.columns().get());
		model.columns().set(2);
		assertEquals(2, model.columns().get());
		verify(grid, times(1)).setColumns(2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSelection() {
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Callback<ISelectable>> captor = ArgumentCaptor.forClass((Class<Callback<ISelectable>>)(Class)Callback.class);
		assertNull(model.selectedComponent().get());
		verify(selectionManager, times(1)).addSelectionListener(captor.capture());
		LayoutComponentView view = mock(LayoutComponentView.class);
		LayoutComponent component = mock(LayoutComponent.class);
		when(view.getLayoutComponent()).thenReturn(component);
		captor.getValue().execute(view);
		assertEquals(component, model.selectedComponent().get());
	}
	
	@Test
	public void testLayoutGrid() {
		assertEquals(grid, model.layoutGrid());
	}
	
	@Test
	public void testStyle() {
		assertEquals(style, model.style());
	}
	
	@Test
	public void testIdText() {
		assertEquals("Layout ID", model.idText().get());
	}
	
	@Test
	public void testRowText() {
		assertEquals("Rows", model.rowText().get());
	}
	
	@Test
	public void testColText() {
		assertEquals("Columns", model.colText().get());
	}
	
	@Test
	public void testToolkitText() {
		assertEquals("Toolkit", model.toolkitText().get());
	}
	
	@Test
	public void testPropertiesText() {
		assertEquals("Properties", model.propertiesText().get());
	}
	
	@Test
	public void testCompileText() {
		assertEquals("Compile", model.compileText().get());
	}
	
	@Test
	public void testLayout() {
		assertEquals(layout, model.persistableLayout());
	}
	
	@Test
	public void testSelectionManager() {
		assertEquals(selectionManager, model.getSelectionManager());
	}
}
