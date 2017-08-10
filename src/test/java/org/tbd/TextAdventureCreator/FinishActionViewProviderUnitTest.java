package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.FinishActionModel;
import ilusr.textadventurecreator.views.action.FinishActionViewProvider;
import javafx.beans.property.SimpleStringProperty;

public class FinishActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private FinishActionModel model;
	
	private FinishActionViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(FinishActionModel.class);
		when(model.finishText()).thenReturn(new SimpleStringProperty());
		
		provider = new FinishActionViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
