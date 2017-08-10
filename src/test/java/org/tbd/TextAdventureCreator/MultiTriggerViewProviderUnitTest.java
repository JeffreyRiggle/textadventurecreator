package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.trigger.MultiTriggerModel;
import ilusr.textadventurecreator.views.trigger.MultiTriggerViewProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class MultiTriggerViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private MultiTriggerModel model;
	
	private MultiTriggerViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(MultiTriggerModel.class);
		when(model.triggersText()).thenReturn(new SimpleStringProperty());
		when(model.triggers()).thenReturn(FXCollections.observableArrayList());
		
		provider = new MultiTriggerViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
