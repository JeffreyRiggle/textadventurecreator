package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.gamestate.OptionViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

public class OptionViewCreatorUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private OptionPersistenceObject option;
	
	private OptionViewCreator creator;
	
	@Before
	public void setup() {
		option = mock(OptionPersistenceObject.class);
		
		creator = new OptionViewCreator();
	}
	
	@Test
	public void testAppendOption() {
		AppendTextActionPersistence act = mock(AppendTextActionPersistence.class);
		when(act.appendText()).thenReturn("Test");
		when(option.action()).thenReturn(act);
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Append: Test", output.getText());
	}

	@Test
	public void testCompleteOption() {
		CompletionActionPersistence act = mock(CompletionActionPersistence.class);
		when(act.completionData()).thenReturn("Test");
		when(option.action()).thenReturn(act);
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Complete: Test", output.getText());
	}
	
	@Test
	public void testExecuteOption() {
		ExecutionActionPersistence act = mock(ExecutionActionPersistence.class);
		when(act.executable()).thenReturn("Test.exe");
		when(option.action()).thenReturn(act);
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Execute: Test.exe", output.getText());
	}
	
	@Test
	public void testModPlayerOption() {
		ModifyPlayerActionPersistence act = mock(ModifyPlayerActionPersistence.class);
		when(act.playerName()).thenReturn("Test");
		when(option.action()).thenReturn(act);
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Mod Player Test", output.getText());
	}
	
	@Test
	public void testSaveOption() {
		when(option.action()).thenReturn(mock(SaveActionPersistenceObject.class));
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Save Action", output.getText());
	}
	
	@Test
	public void testScriptOption() {
		when(option.action()).thenReturn(mock(ScriptedActionPersistenceObject.class));
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Script Action", output.getText());
	}
	
	@Test
	public void testFinishOption() {
		when(option.action()).thenReturn(mock(FinishActionPersistenceObject.class));
		
		Label output = (Label)creator.create(option);
		assertEquals("Option: Finish Action", output.getText());
	}
}
