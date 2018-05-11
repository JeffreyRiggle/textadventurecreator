package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.wizard.GameSettingsModel;
import textadventurelib.persistence.DisplayType;
import textadventurelib.persistence.GameInfoPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.TransitionPersistenceObject;

public class WizardGameSettingsModelUnitTest {

	private TextAdventureProjectPersistence project;
	private GameInfoPersistenceObject gameInfo;
	private TextAdventurePersistenceObject tav;
	private TransitionPersistenceObject trans;
	private MediaFinder mediaFinder;
	private ILanguageService languageService;
	
	private GameSettingsModel model;
	
	@Before
	public void setup() {
		project = mock(TextAdventureProjectPersistence.class);
		gameInfo = mock(GameInfoPersistenceObject.class);
		when(project.getGameInfo()).thenReturn(gameInfo);
		tav = mock(TextAdventurePersistenceObject.class);
		trans = mock(TransitionPersistenceObject.class);
		when(tav.transition()).thenReturn(trans);
		when(project.getTextAdventure()).thenReturn(tav);
		
		mediaFinder = mock(MediaFinder.class);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.INLINE_PLAYER)).thenReturn("Inline player");
		when(languageService.getValue(DisplayStrings.BROWSE)).thenReturn("Browse");
		when(languageService.getValue(DisplayStrings.INLINE_GAME)).thenReturn("Inline game");
		when(languageService.getValue(DisplayStrings.INLINE_LAYOUT)).thenReturn("Inline layout");
		when(languageService.getValue(DisplayStrings.BUFFER)).thenReturn("Buffer");
		when(languageService.getValue(DisplayStrings.SIZE)).thenReturn("Size");
		when(languageService.getValue(DisplayStrings.GAME_NAME)).thenReturn("Game Name");
		when(languageService.getValue(DisplayStrings.GAME_DESCRIPTION)).thenReturn("Game Description");
		when(languageService.getValue(DisplayStrings.GAME_ICON)).thenReturn("Game Icon");
		when(languageService.getValue(DisplayStrings.CREATOR)).thenReturn("Creator");
		when(languageService.getValue(DisplayStrings.GAME_TYPE)).thenReturn("Game Type");
		when(languageService.getValue(DisplayStrings.HOSTED)).thenReturn("Hosted");
		when(languageService.getValue(DisplayStrings.STAND_ALONE)).thenReturn("Stand Alone");
		when(languageService.getValue(DisplayStrings.IS_DEV)).thenReturn("Is Dev");
		when(languageService.getValue(DisplayStrings.TRANSITION_TYPE)).thenReturn("Transition Type");
		when(languageService.getValue(DisplayStrings.MEDIA)).thenReturn("Media");
		when(languageService.getValue(DisplayStrings.PROJECT_LOCATION)).thenReturn("Project Location");
		when(languageService.getValue(DisplayStrings.APPLICATION_BACKGROUND)).thenReturn("Application Background");
		when(languageService.getValue(DisplayStrings.LANGUAGE)).thenReturn("Language");
		
		model = new GameSettingsModel(project, mediaFinder, languageService);
	}
	
	@Test
	public void testGameName() {
		assertFalse(model.gameInfoValid().get());
		model.gameName().set("Test Game");
		assertEquals("Test Game", model.gameName().get());
		verify(gameInfo, times(1)).gameName("Test Game");
		assertEquals(System.getProperty("user.home") + "/TextAdventure/Test Game", model.projectLocation().get());
		verify(tav, times(1)).gameName("Test Game");
		assertTrue(model.gameInfoValid().get());
	}

	@Test
	public void testGameDescription() {
		model.gameDescription().set("test desc");
		assertEquals("test desc", model.gameDescription().get());
		verify(gameInfo, times(1)).description("test desc");
	}
	
	@Test
	public void testIconLocation() {
		model.iconLocation().set("c:/test/file.png");
		assertEquals("c:/test/file.png", model.iconLocation().get());
		verify(project, times(1)).setIconLocation("c:/test/file.png");
	}
	
	@Test
	public void testCreator() {
		model.creator().set("Tester");
		assertEquals("Tester", model.creator().get());
		verify(gameInfo, times(1)).creator("Tester");
	}
	
	@Test
	public void testStandAlone() {
		assertFalse(model.standAlone().get());
		model.standAlone().set(true);
		assertTrue(model.standAlone().get());
		verify(project, times(1)).setIsStandAlone(true);
	}
	
	@Test
	public void testIsDev() {
		assertFalse(model.isDev().get());
		model.isDev().set(true);
		assertTrue(model.isDev().get());
		verify(project, times(1)).setIsDev(true);
	}
	
	@Test
	public void testTransitionTypes() {
		assertEquals(2, model.transitionTypes().list().size());
	}
	
	@Test
	public void testSelectedTransitionType() {
		model.transitionTypes().selected().set("SplashScreen");
		assertEquals("SplashScreen", model.transitionTypes().selected().get());
		verify(trans, times(1)).displayType(DisplayType.SplashScreen);
	}
	
	@Test
	public void testMediaLocation() {
		model.mediaLocation().set("c:/test/file2.jpg");
		assertEquals("c:/test/file2.jpg", model.mediaLocation().get());
		verify(trans, times(1)).mediaLocation("c:/test/file2.jpg");
	}
	
	@Test
	public void testSupportLanguages() {
		assertEquals(3, model.supportedLanguages().list().size());
	}
	
	@Test
	public void testSelectedSupportedLanguage() {
		model.supportedLanguages().selected().set("Electron");
		assertEquals("Electron", model.supportedLanguages().selected().get());
		verify(project, times(1)).setLanguage("Electron");
	}
	
	@Test
	public void testProjectLocation() {
		model.projectLocation().set("c:/project/game");
		assertEquals("c:/project/game", model.projectLocation().get());
		verify(project, times(1)).setProjectLocation("c:/project/game");
	}
	
	@Test
	public void testBackgroundLocation() {
		model.backgroundLocation().set("c:/images/back.png");
		assertEquals("c:/images/back.png", model.backgroundLocation().get());
		verify(project, times(1)).setBackgroundLocation("c:/images/back.png");
	}
	
	@Test
	public void testGameStatesInline() {
		assertTrue(model.gameStatesInline().get());
		model.gameStatesInline().set(false);
		assertFalse(model.gameStatesInline().get());
		verify(tav, times(1)).gameStatesInline(false);
	}
	
	@Test
	public void testGameStatesLocation() {
		model.gameStatesLocation().set("c:/game/gamestatefile.xml");
		assertEquals("c:/game/gamestatefile.xml", model.gameStatesLocation().get());
		verify(tav, times(1)).gameStatesLocation("c:/game/gamestatefile.xml");
	}
	
	@Test
	public void testPlayersInline() {
		assertTrue(model.playersInline().get());
		model.playersInline().set(false);
		assertFalse(model.playersInline().get());
		verify(tav, times(1)).playersInline(false);
	}
	
	@Test
	public void testPlayersLocation() {
		model.playersLocation().set("c:/game/playerfile.xml");
		assertEquals("c:/game/playerfile.xml", model.playersLocation().get());
		verify(tav, times(1)).playersLocation("c:/game/playerfile.xml");
	}
	
	@Test
	public void testLayoutsInline() {
		assertTrue(model.layoutInline().get());
		model.layoutInline().set(false);
		assertFalse(model.layoutInline().get());
		verify(tav, times(1)).layoutsInline(false);
	}
	
	@Test
	public void testLayoutsLocation() {
		model.layoutLocation().set("c:/game/layoutfile.xml");
		assertEquals("c:/game/layoutfile.xml", model.layoutLocation().get());
		verify(tav, times(1)).layoutsLocation("c:/game/layoutfile.xml");
	}
	
	@Test
	public void testBufferSize() {
		assertNull(model.bufferSize().get());
		model.bufferSize().set(5);
		assertEquals(new Integer(5), model.bufferSize().get());
		verify(tav, times(1)).buffer(5);
	}
	
	@Test
	public void testPersistence() {
		assertEquals(project, model.persistenceObject());
	}
	
	@Test
	public void testInlinePlayerText() {
		assertEquals("Inline player", model.inlinePlayerText().get());
	}
	
	@Test
	public void testInlineGameText() {
		assertEquals("Inline game", model.inlineGameText().get());
	}
	
	@Test
	public void testInlineLayoutText() {
		assertEquals("Inline layout", model.inlineLayoutText().get());
	}
	
	@Test
	public void testBrowseText() {
		assertEquals("Browse", model.browseText().get());
	}
	
	@Test
	public void testBufferText() {
		assertEquals("Buffer", model.bufferText().get());
	}
	
	@Test
	public void testSizeText() {
		assertEquals("Size", model.sizeText().get());
	}
	
	@Test
	public void testGameNameText() {
		assertEquals("Game Name", model.gameNameText().get());
	}
	
	@Test
	public void testGameDescriptionText() {
		assertEquals("Game Description", model.gameDescriptionText().get());
	}
	
	@Test
	public void testGameIconText() {
		assertEquals("Game Icon", model.gameIconText().get());
	}
	
	@Test
	public void testCreatorText() {
		assertEquals("Creator", model.creatorText().get());
	}
	
	@Test
	public void testGameTypeText() {
		assertEquals("Game Type", model.gameTypeText().get());
	}
	
	@Test
	public void testHostedText() {
		assertEquals("Hosted", model.hostedText().get());
	}
	
	@Test
	public void testStandAloneText() {
		assertEquals("Stand Alone", model.standAloneText().get());
	}
	
	@Test
	public void testIsDevText() {
		assertEquals("Is Dev", model.isDevText().get());
	}
	
	@Test
	public void testTransitionTypeText() {
		assertEquals("Transition Type", model.transitionTypeText().get());
	}
	
	@Test
	public void testMediaText() {
		assertEquals("Media", model.mediaText().get());
	}
	
	@Test
	public void testProjectLocationText() {
		assertEquals("Project Location", model.projectLocationText().get());
	}
	
	@Test
	public void testBackgroundText() {
		assertEquals("Application Background", model.backgroundText().get());
	}
	
	@Test
	public void testLanguageText() {
		assertEquals("Language", model.languageText().get());
	}
}
