package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.ScriptedActionModel;
import ilusr.textadventurecreator.views.action.ScriptedActionViewProvider;
import javafx.beans.property.SimpleStringProperty;

public class ScriptedActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ScriptedActionModel model;
	
	private ScriptedActionViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(ScriptedActionModel.class);
		when(model.actionText()).thenReturn(new SimpleStringProperty());
		when(model.script()).thenReturn(new SimpleStringProperty());
		when(model.viewText()).thenReturn(new SimpleStringProperty());
		
		provider = new ScriptedActionViewProvider(model);
	}
	
	@Test
	public void testProvide() {
		assertNotNull(provider.getView());
	}

}
