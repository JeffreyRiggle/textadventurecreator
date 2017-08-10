package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.trigger.TriggerViewCreator;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;

public class TriggerViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TriggerViewCreator creator;
	
	@Before
	public void setup() {
		creator = new TriggerViewCreator();
	}
	
	@Test
	public void testCreateTextTrigger() {
		TextTriggerPersistenceObject trigger = mock(TextTriggerPersistenceObject.class);
		when(trigger.text()).thenReturn("Test");
		Node retVal = creator.create(trigger);
		assertNotNull(retVal);
		assertEquals("Text Trigger: Test", ((Label)retVal).textProperty().get());
	}

	@Test
	public void testCreatePlayerTrigger() {
		PlayerTriggerPersistenceObject trigger = mock(PlayerTriggerPersistenceObject.class);
		when(trigger.playerName()).thenReturn("Player1");
		Node retVal = creator.create(trigger);
		assertNotNull(retVal);
		assertEquals("Player Trigger: Player1", ((Label)retVal).textProperty().get());
	}
	
	@Test
	public void testCreateScriptTrigger() {
		ScriptedTriggerPersistenceObject trigger = mock(ScriptedTriggerPersistenceObject.class);
		Node retVal = creator.create(trigger);
		assertNotNull(retVal);
		assertEquals("Scripted Trigger", ((Label)retVal).textProperty().get());
	}
	
	@Test
	public void testCreateMultiPartTrigger() {
		MultiPartTriggerPersistenceObject trigger = mock(MultiPartTriggerPersistenceObject.class);
		Node retVal = creator.create(trigger);
		assertNotNull(retVal);
		assertTrue(retVal instanceof TitledPane);
	}
}
