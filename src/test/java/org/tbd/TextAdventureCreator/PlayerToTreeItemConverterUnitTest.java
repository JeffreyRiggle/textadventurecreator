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
import ilusr.textadventurecreator.views.converters.PlayerToTreeItemConverter;
import ilusr.textadventurecreator.views.player.PlayerModel;
import javafx.beans.property.SimpleStringProperty;

public class PlayerToTreeItemConverterUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameExplorerModel model;
	private ILayoutService layoutService;
	
	private PlayerToTreeItemConverter converter;
	
	@Before
	public void setup() {
		model = mock(GameExplorerModel.class);
		layoutService = mock(ILayoutService.class);
		
		converter = new PlayerToTreeItemConverter(model, layoutService);
	}
	
	@Test
	public void testConvert() {
		PlayerModel mod = mock(PlayerModel.class);
		when(mod.playerID()).thenReturn(new SimpleStringProperty("Player1"));
		assertNotNull(converter.convert(mod));
	}
}
