package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.trigger.MultiTriggerModel;
import ilusr.textadventurecreator.views.trigger.TriggerView;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.event.ActionEvent;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;

public class MultiTriggerModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private MultiPartTriggerPersistenceObject trigger; 
	private IDialogService service;
	private IDialogProvider dialogProvider;
	private TriggerViewFactory factory;
	private ILanguageService languageService;
	private Dialog dialog;
	
	private MultiTriggerModel model;
	
	@Before
	public void setup() {
		trigger = mock(MultiPartTriggerPersistenceObject.class);
		service = mock(IDialogService.class);
		dialog = mock(Dialog.class);
		dialogProvider = mock(IDialogProvider.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		factory = mock(TriggerViewFactory.class);
		when(factory.create(any())).thenReturn(mock(TriggerView.class));
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.TRIGGERS)).thenReturn("Triggers");
		
		model = new MultiTriggerModel(trigger, service, dialogProvider, factory, languageService);
	}
	
	@Test
	public void testAddTriggerKey() {
		assertNotNull(model.addTriggerKey());
	}

	@Test
	public void testAddTrigger() {
		ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
		model.getAddTriggerAction().handle(mock(ActionEvent.class));
		
		verify(service, times(1)).displayModal(dialog);
		verify(dialog, times(1)).setOnComplete(captor.capture());
		
		captor.getValue().run();
		assertEquals(1, model.triggers().size());
		verify(trigger, times(1)).addTrigger(any());
	}
	
	@Test
	public void testEditTrigger() {
		model.getEditTriggerAction().execute(mock(TriggerPersistenceObject.class));
		verify(service, times(1)).displayModal(dialog);
	}
	
	@Test
	public void testRemoveTrigger() {
		TriggerPersistenceObject trig = mock(TriggerPersistenceObject.class);
		model.triggers().add(trig);
		model.triggers().remove(trig);
		verify(trigger, times(1)).removeTrigger(trig);
	}
	
	@Test
	public void testPersistenceObject() {
		assertEquals(trigger, model.getPersistenceObject());
	}
	
	@Test
	public void testTriggersText() {
		assertEquals("Triggers", model.triggersText().get());
	}
}
