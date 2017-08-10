package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.CompletionActionModel;
import ilusr.textadventurecreator.views.action.CompletionActionViewProvider;
import javafx.beans.property.SimpleStringProperty;

public class CompletionActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private CompletionActionModel model;
	
	private CompletionActionViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(CompletionActionModel.class);
		when(model.completionData()).thenReturn(new SimpleStringProperty());
		when(model.stateText()).thenReturn(new SimpleStringProperty());
		
		provider = new CompletionActionViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
