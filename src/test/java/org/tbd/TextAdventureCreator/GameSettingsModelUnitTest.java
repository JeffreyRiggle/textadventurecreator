package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.GameSettingsModel;
import ilusr.textadventurecreator.shell.IProviderListener;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public class GameSettingsModelUnitTest {

	private TextAdventureProvider provider;
	private ILanguageService languageService;
	
	private TextAdventureProjectPersistence project1;
	private TextAdventureProjectPersistence project2;	
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.NO_GAME_LOADED)).thenReturn("No Game Loaded");
		when(languageService.getValue(DisplayStrings.GAME_SETTINGS)).thenReturn("Game Settings");
		when(languageService.getValue(DisplayStrings.START_GAME_STATE)).thenReturn("Start Game State");
		
		project1 = mock(TextAdventureProjectPersistence.class);
		TextAdventurePersistenceObject tav1 = mock(TextAdventurePersistenceObject.class);
		GameStatePersistenceObject gs1 = mock(GameStatePersistenceObject.class);
		when(gs1.stateId()).thenReturn("gs1");
		GameStatePersistenceObject gs2 = mock(GameStatePersistenceObject.class);
		when(gs2.stateId()).thenReturn("gs2");
		GameStatePersistenceObject gs3 = mock(GameStatePersistenceObject.class);
		when(gs3.stateId()).thenReturn("gs3");
		when(tav1.gameStates()).thenReturn(Arrays.asList(gs1, gs2, gs3));
		when(tav1.currentGameState()).thenReturn("gs1");
		when(project1.getTextAdventure()).thenReturn(tav1);
		
		project2 = mock(TextAdventureProjectPersistence.class);
		TextAdventurePersistenceObject tav2 = mock(TextAdventurePersistenceObject.class);
		when(tav2.gameStates()).thenReturn(Arrays.asList(gs1, gs2, gs3));
		when(tav2.currentGameState()).thenReturn(null);
		when(project2.getTextAdventure()).thenReturn(tav2);
	}
	
	@Test
	public void testCreateWithNoTextAdventure() {
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertFalse(model.gameLoaded().get());
		assertNull(model.firstGameState().selected().get());
	}

	@Test
	public void testCreateWithTextAdventure() {
		when(provider.getTextAdventureProject()).thenReturn(project1);
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertTrue(model.gameLoaded().get());
		assertEquals(model.firstGameState().selected().get(), "gs1");
		assertTrue(model.firstGameState().list().contains("gs1"));
		assertTrue(model.firstGameState().list().contains("gs2"));
		assertTrue(model.firstGameState().list().contains("gs3"));
	}
	
	@Test
	public void testCreateWithTextAdventureAndNoCurrentGameState() {
		when(provider.getTextAdventureProject()).thenReturn(project2);
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertTrue(model.gameLoaded().get());
		assertEquals(model.firstGameState().selected().get(), null);
		assertTrue(model.firstGameState().list().contains("gs1"));
		assertTrue(model.firstGameState().list().contains("gs2"));
		assertTrue(model.firstGameState().list().contains("gs3"));
	}
	
	@Test
	public void testChangeToValidTextAdventure() {
		ArgumentCaptor<IProviderListener> listener = ArgumentCaptor.forClass(IProviderListener.class);
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertFalse(model.gameLoaded().get());
		verify(provider, times(1)).addListener(listener.capture());
		
		when(provider.getTextAdventureProject()).thenReturn(project1);
		listener.getValue().provided(project1);
		assertTrue(model.gameLoaded().get());
		assertEquals(model.firstGameState().selected().get(), "gs1");
		assertTrue(model.firstGameState().list().contains("gs1"));
		assertTrue(model.firstGameState().list().contains("gs2"));
		assertTrue(model.firstGameState().list().contains("gs3"));
	}
	
	@Test
	public void testFirstGameState() {
		when(provider.getTextAdventureProject()).thenReturn(project1);
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertEquals(model.firstGameState().selected().get(), "gs1");
		model.firstGameState().selected().set("gs2");
		assertEquals(model.firstGameState().selected().get(), "gs2");
		verify(project1.getTextAdventure(), times(1)).currentGameState("gs2");
	}
	
	@Test
	public void testFirstGameStateWithEmpty() {
		when(provider.getTextAdventureProject()).thenReturn(project1);
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertEquals(model.firstGameState().selected().get(), "gs1");

		model.firstGameState().selected().set("");
		verify(project1.getTextAdventure(), times(0)).currentGameState("");
		
		model.firstGameState().selected().set(null);
		verify(project1.getTextAdventure(), times(0)).currentGameState(null);
	}
	
	@Test
	public void testNoSettingText() {
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertEquals("No Game Loaded", model.noSettingsText().get());
	}
	
	@Test
	public void testSettingText() {
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertEquals("Game Settings", model.settingText().get());
	}
	
	@Test
	public void testStartGameStateText() {
		GameSettingsModel model = new GameSettingsModel(provider, languageService);
		assertEquals("Start Game State", model.startGameStateText().get());
	}
}
