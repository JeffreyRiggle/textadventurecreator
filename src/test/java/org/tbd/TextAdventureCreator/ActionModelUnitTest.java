package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.ActionModel;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

public class ActionModelUnitTest {

	private ILanguageService languageService;
	
	private ActionModel model;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.ACTION)).thenReturn("Action");
		
		model = new ActionModel(languageService);
	}
	
	@Test
	public void testCreateWithoutAction() {
		assertNotNull(model);
		assertEquals(7, model.types().list().size());
		assertNull(model.types().selected().get());
	}

	@Test
	public void testCreateWithAppendAction() {
		AppendTextActionPersistence act = mock(AppendTextActionPersistence.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.appendAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithCompletionAction() {
		CompletionActionPersistence act = mock(CompletionActionPersistence.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.completionAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithExecuteAction() {
		ExecutionActionPersistence act = mock(ExecutionActionPersistence.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.executionAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithSaveAction() {
		SaveActionPersistenceObject act = mock(SaveActionPersistenceObject.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.saveAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithPlayerAction() {
		ModifyPlayerActionPersistence act = mock(ModifyPlayerActionPersistence.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.playerAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithScriptAction() {
		ScriptedActionPersistenceObject act = mock(ScriptedActionPersistenceObject.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.scriptAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testCreateWithFinishAction() {
		FinishActionPersistenceObject act = mock(FinishActionPersistenceObject.class);
		ActionModel mod = new ActionModel(act, languageService);
		
		assertEquals(mod.finishAction(), mod.types().selected().get());
		assertEquals(act, mod.persistenceObject().get());
	}
	
	@Test
	public void testAppendText() {
		assertEquals("Append Text", model.appendAction());
	}
	
	@Test
	public void testCompletionActionText() {
		assertEquals("Complete", model.completionAction());
	}
	
	@Test
	public void testExecutionActionText() {
		assertEquals("Execute", model.executionAction());
	}
	
	@Test
	public void testSaveActionText() {
		assertEquals("Save action", model.saveAction());
	}
	
	@Test
	public void testPlayerActionText() {
		assertEquals("Modify Player", model.playerAction());
	}
	
	@Test
	public void testScriptActionText() {
		assertEquals("Script Action", model.scriptAction());
	}
	
	@Test
	public void testFinishActionText() {
		assertEquals("Finish Action", model.finishAction());
	}
	
	@Test
	public void testActionText() {
		assertEquals("Action", model.actionText().get());
	}
	
	@Test
	public void testPersistenceObject() {
		assertNull(model.persistenceObject().get());
	}
}
