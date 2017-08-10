package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.AppendTextModel;
import ilusr.textadventurecreator.views.action.AppendTextViewProvider;
import javafx.beans.property.SimpleStringProperty;

public class AppendTextViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private AppendTextModel model;
	
	private AppendTextViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(AppendTextModel.class);
		when(model.appendText()).thenReturn(new SimpleStringProperty());
		when(model.labelText()).thenReturn(new SimpleStringProperty());
		
		provider = new AppendTextViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
