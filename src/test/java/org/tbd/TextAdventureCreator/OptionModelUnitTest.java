package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.interfaces.Callback;
import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.gamestate.OptionModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class OptionModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IDialogService dialogService;
	private LibraryService libraryService;
	private OptionPersistenceObject option; 
	private TriggerViewFactory factory;
	private List<PlayerPersistenceObject> players;
	private ILanguageService languageService;
	private ActionViewFactory actionViewFactory;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private OptionModel model;
	   
	@Before
	public void setup() {
		dialogService = mock(IDialogService.class);
		libraryService = mock(LibraryService.class);
		option = mock(OptionPersistenceObject.class);
		factory = mock(TriggerViewFactory.class);
		players = new ArrayList<>();
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.TRIGGERS)).thenReturn("Triggers");
		when(languageService.getValue(DisplayStrings.ACTION)).thenReturn("Action");
		
		actionViewFactory = mock(ActionViewFactory.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new OptionModel(dialogService, libraryService, option, factory, players, languageService,
				actionViewFactory, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testCreateWithData() {
		OptionPersistenceObject opt = mock(OptionPersistenceObject.class);
		when(opt.action()).thenReturn(mock(AppendTextActionPersistence.class));
		when(opt.triggers()).thenReturn(Arrays.asList(mock(TextTriggerPersistenceObject.class), mock(TextTriggerPersistenceObject.class)));
		OptionModel mod = new OptionModel(dialogService, libraryService, opt, factory, players, languageService,
				actionViewFactory, dialogProvider, styleService, urlProvider);
		
		assertNotNull(mod.action().get());
		assertEquals(mod.appendAction(), mod.types().selected().get());
		assertEquals(2, mod.triggers().size());
	}
	
	@Test
	public void testActionTypes() {
		assertEquals(7, model.types().list().size());
	}
	
	@Test
	public void testAllowLibraryAddWithLibrary() {
		assertTrue(model.allowLibraryAdd());
	}

	@Test
	public void testAllowLibraryAddWithoutLibrary() {
		OptionModel mod = new OptionModel(dialogService, null, option, factory, players, languageService,
				actionViewFactory, dialogProvider, styleService, urlProvider);
		assertFalse(mod.allowLibraryAdd());
	}
	
	@Test
	public void testAddTriggerKey() {
		assertNotNull(model.addTriggerKey());
	}
	
	@Test
	public void testEditTrigger() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		when(factory.create(any(), any())).thenReturn(mock(TriggerView.class));
		model.getEditTriggerAction().execute(mock(TriggerPersistenceObject.class));
		
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testAddTrigger() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		when(factory.create(any(), any())).thenReturn(mock(TriggerView.class));
		
		model.addTrigger();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testAddTriggerFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		
		model.addTriggerFromLibrary();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testRemoveTrigger() {
		TriggerPersistenceObject trig = mock(TriggerPersistenceObject.class);
		model.triggers().add(trig);
		model.triggers().remove(trig);
		verify(option, times(1)).removeTrigger(trig);
	}
	
	@Test
	public void testPlayers() {
		assertEquals(players, model.players());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddActionFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addActionFromLibrary(mock(Callback.class));
		
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testPersistence() {
		assertEquals(option, model.persistenceObject());
	}
	
	@Test
	public void testAction() {
		ActionPersistenceObject act = mock(ActionPersistenceObject.class);
		
		assertNull(model.action().get());
		model.action().set(act);
		assertEquals(act, model.action().get());
	}
	
	@Test
	public void testTriggerText() {
		assertEquals("Triggers", model.triggerText().get());
	}
	
	@Test
	public void testActionText() {
		assertEquals("Action", model.actionText().get());
	}
	
	@Test
	public void testCompletionAction() {
		assertEquals("Complete", model.completionAction());
	}
	
	@Test
	public void testExecutionAction() {
		assertEquals("Execute", model.executionAction());
	}
	
	@Test
	public void testSaveAction() {
		assertEquals("Save action", model.saveAction());
	}
	
	@Test
	public void testPlayerModAction() {
		assertEquals("Modify Player", model.playerAction());
	}
	
	@Test
	public void testScriptAction() {
		assertEquals("Script Action", model.scriptAction());
	}
	
	@Test
	public void testFinishAction() {
		assertEquals("Finish Action", model.finishAction());
	}
}
