package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.ExecutableActionViewProvider;
import ilusr.textadventurecreator.views.action.ExecutionActionModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExecutableActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ExecutionActionModel model;
	
	private ExecutableActionViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(ExecutionActionModel.class);
		when(model.blocking()).thenReturn(new SimpleBooleanProperty());
		when(model.blockingText()).thenReturn(new SimpleStringProperty());
		when(model.executable()).thenReturn(new SimpleStringProperty());
		when(model.exeText()).thenReturn(new SimpleStringProperty());
		when(model.yesText()).thenReturn(new SimpleStringProperty());
		when(model.noText()).thenReturn(new SimpleStringProperty());
		
		provider = new ExecutableActionViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
