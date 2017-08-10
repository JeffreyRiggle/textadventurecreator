package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.ILayoutService;
import ilusr.textadventurecreator.views.GameExplorerModel;
import ilusr.textadventurecreator.views.converters.GameStateToTreeItemConverter;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import javafx.beans.property.SimpleStringProperty;

public class GameStateToTreeItemConverterUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameExplorerModel model;
	private ILayoutService layoutService;
	
	private GameStateToTreeItemConverter converter;
	
	@Before
	public void setup() {
		model = mock(GameExplorerModel.class);
		layoutService = mock(ILayoutService.class);
		
		converter = new GameStateToTreeItemConverter(model, layoutService);
	}
	
	@Test
	public void testConvert() {
		GameStateModel mod = mock(GameStateModel.class);
		when(mod.stateId()).thenReturn(new SimpleStringProperty("GS2"));
		assertNotNull(converter.convert(mod));
	}

}
