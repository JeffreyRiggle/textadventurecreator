package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.trigger.ScriptTriggerViewProvider;
import ilusr.textadventurecreator.views.trigger.ScriptedTriggerModel;
import javafx.beans.property.SimpleStringProperty;

public class ScriptedTriggerViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ScriptedTriggerModel model;
	
	private ScriptTriggerViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(ScriptedTriggerModel.class);
		when(model.script()).thenReturn(new SimpleStringProperty());
		when(model.scriptText()).thenReturn(new SimpleStringProperty());
		when(model.editorText()).thenReturn(new SimpleStringProperty());
		
		provider = new ScriptTriggerViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
