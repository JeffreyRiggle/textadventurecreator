package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import ilusr.textadventurecreator.views.trigger.TextTriggerModel;
import ilusr.textadventurecreator.views.trigger.TextTriggerViewProvider;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TextTriggerViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextTriggerModel model;
	
	private TextTriggerViewProvider provider;
	
	@Before
	public void setup() {
		model = mock(TextTriggerModel.class);
		when(model.caseText()).thenReturn(new SimpleStringProperty());
		when(model.textText()).thenReturn(new SimpleStringProperty());
		when(model.yesText()).thenReturn(new SimpleStringProperty());
		when(model.noText()).thenReturn(new SimpleStringProperty());
		when(model.typeText()).thenReturn(new SimpleStringProperty());
		when(model.text()).thenReturn(new SimpleStringProperty());
		when(model.caseSensitive()).thenReturn(new SimpleBooleanProperty());
		when(model.matchType()).thenReturn(new SelectionAwareObservableList<String>());
		
		provider = new TextTriggerViewProvider(model);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
