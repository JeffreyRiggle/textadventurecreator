package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.trigger.PlayerTriggerViewProvider;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerTriggerViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private PlayerTriggerPersistenceObject trigger;
	private List<PlayerPersistenceObject> players;
	private ILanguageService languageService;
	
	private PlayerTriggerViewProvider provider;
	
	@Before
	public void setup() {
		trigger = mock(PlayerTriggerPersistenceObject.class);
		when(trigger.modificationObject()).thenReturn(ModificationObject.Attribute);
		when(trigger.condition()).thenReturn(Condition.EqualTo);
		
		players = new ArrayList<>();
		languageService = mock(ILanguageService.class);
		
		provider = new PlayerTriggerViewProvider(trigger, players, languageService);
	}
	
	@Test
	public void testGetData() {
		assertNotNull(provider.getView());
	}

}
