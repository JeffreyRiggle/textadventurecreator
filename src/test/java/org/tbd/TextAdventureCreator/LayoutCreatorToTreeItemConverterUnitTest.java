package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.ILayoutService;
import ilusr.textadventurecreator.views.GameExplorerModel;
import ilusr.textadventurecreator.views.converters.LayoutCreatorToTreeItemConverter;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import javafx.beans.property.SimpleStringProperty;

public class LayoutCreatorToTreeItemConverterUnitTest {
	
	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameExplorerModel model;
	private ILayoutService layoutService;
	
	private LayoutCreatorToTreeItemConverter converter;
	
	@Before
	public void setup() {
		model = mock(GameExplorerModel.class);
		layoutService = mock(ILayoutService.class);
		
		converter = new LayoutCreatorToTreeItemConverter(model, layoutService);
	}
	
	@Test
	public void testConvert() {
		LayoutCreatorModel mod = mock(LayoutCreatorModel.class);
		when(mod.id()).thenReturn(new SimpleStringProperty("Layout1"));
		assertNotNull(converter.convert(mod));
	}

}
