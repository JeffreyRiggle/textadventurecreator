package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.SaveActionModel;
import ilusr.textadventurecreator.views.action.SaveActionViewProvider;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SaveActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private SaveActionModel model;
	
	private SaveActionViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(SaveActionModel.class);
		when(model.blocking()).thenReturn(new SimpleBooleanProperty());
		when(model.blockingText()).thenReturn(new SimpleStringProperty());
		when(model.saveLocation()).thenReturn(new SimpleStringProperty());
		when(model.saveLocationText()).thenReturn(new SimpleStringProperty());
		when(model.yesText()).thenReturn(new SimpleStringProperty());
		when(model.noText()).thenReturn(new SimpleStringProperty());
		
		provider = new SaveActionViewProvider(model);
	}
	
	@Test
	public void testProvide() {
		assertNotNull(provider.getView());
	}

}
