package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import ilusr.textadventurecreator.views.macro.MacroBuilderModel;
import ilusr.textadventurecreator.views.macro.MacroBuilderView;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import javafx.beans.property.SimpleBooleanProperty;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class GameStateModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private GameStatePersistenceObject gameState;
	private LayoutInfoPersistenceObject layout;
	private IDialogService dialogService;
	private LibraryService libraryService;
	private MacroBuilderViewFactory macroFactory; 
	private MediaFinder mediaFinder;
	private TriggerViewFactory triggerFactory;
	private PlayerModProviderFactory playerModFactory;
	private List<PlayerPersistenceObject> players;
	private List<LayoutPersistenceObject> gameLayouts;
	private ILanguageService languageService;
	private ActionViewFactory actionViewFactory;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private GameStateModel model;
	
	@Before
	public void setup() {
		gameState = mock(GameStatePersistenceObject.class);
		layout = mock(LayoutInfoPersistenceObject.class);
		when(gameState.layout()).thenReturn(layout);
		dialogService = mock(IDialogService.class);
		libraryService = mock(LibraryService.class);
		macroFactory = mock(MacroBuilderViewFactory.class);
		mediaFinder = mock(MediaFinder.class);
		triggerFactory = mock(TriggerViewFactory.class);
		playerModFactory = mock(PlayerModProviderFactory.class);
		players = new ArrayList<>();
		LayoutPersistenceObject testlayout = mock(LayoutPersistenceObject.class);
		when(testlayout.id()).thenReturn("Test");
		when(testlayout.hasContentArea()).thenReturn(false);
		LayoutPersistenceObject testlayout2 = mock(LayoutPersistenceObject.class);
		when(testlayout2.id()).thenReturn("Test2");
		when(testlayout2.hasContentArea()).thenReturn(true);
		gameLayouts = Arrays.asList(testlayout, testlayout2);
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.STATE_ID)).thenReturn("State Id");
		when(languageService.getValue(DisplayStrings.TEXT_LOG)).thenReturn("Text Log");
		when(languageService.getValue(DisplayStrings.MACRO)).thenReturn("Macro");
		when(languageService.getValue(DisplayStrings.LAYOUT)).thenReturn("Layout");
		when(languageService.getValue(DisplayStrings.CONTENT)).thenReturn("Content");
		when(languageService.getValue(DisplayStrings.BROWSE)).thenReturn("Browse");
		when(languageService.getValue(DisplayStrings.OPTIONS)).thenReturn("Options");
		when(languageService.getValue(DisplayStrings.OPTION)).thenReturn("Option");
		when(languageService.getValue(DisplayStrings.TIMERS)).thenReturn("Timers");
		
		actionViewFactory = mock(ActionViewFactory.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		model = new GameStateModel(gameState, dialogService, libraryService, macroFactory, mediaFinder, triggerFactory, playerModFactory,
				players, gameLayouts, languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testCreateWithData() {
		assertNotNull(model);
	}

	@Test
	public void testUpdateStateIdToNull() {
		model.stateId().set(null);
		assertFalse(model.valid().get());
		assertNull(model.stateId().get());
	}
	
	@Test
	public void testUpdateStateId() {
		model.stateId().set("GS1");
		verify(gameState, times(1)).stateId("GS1");
		assertTrue(model.valid().get());
		assertEquals("GS1", model.stateId().get());
	}
	
	@Test
	public void testUpdateTextLog() {
		model.textLog().set("This is a text log");
		verify(gameState, times(1)).textLog("This is a text log");
		assertEquals("This is a text log", model.textLog().get());
	}
	
	@Test
	public void testUpdateLayoutTWT() {
		model.layouts().selected().set("TextWithTextInput");
		verify(layout, times(1)).setLayoutType(LayoutType.TextWithTextInput);
	}
	
	@Test
	public void testUpdateLayoutTWB() {
		model.layouts().selected().set("TextWithButtonInput");
		verify(layout, times(1)).setLayoutType(LayoutType.TextWithButtonInput);
	}
	
	@Test
	public void testUpdateLayoutCO() {
		model.layouts().selected().set("ContentOnly");
		verify(layout, times(1)).setLayoutType(LayoutType.ContentOnly);
	}
	
	
	@Test
	public void testUpdateLayoutTCWT() {
		model.layouts().selected().set("TextAndContentWithTextInput");
		verify(layout, times(1)).setLayoutType(LayoutType.TextAndContentWithTextInput);
	}
	
	@Test
	public void testUpdateLayoutTCWB() {
		model.layouts().selected().set("TextAndContentWithButtonInput");
		verify(layout, times(1)).setLayoutType(LayoutType.TextAndContentWithButtonInput);
	}
	@Test
	public void testUpdateToCustomLayout() {
		model.layouts().selected().set("Super Cool Layout");
		verify(layout, times(1)).setLayoutType(LayoutType.Custom);
		verify(layout, times(1)).setLayoutId("Super Cool Layout");
	}
	
	@Test
	public void testContentLocation() {
		model.contentLocation().set("c:/test/test.mp4");
		verify(layout, times(1)).setLayoutContent("c:/test/test.mp4");
		assertEquals("c:/test/test.mp4", model.contentLocation().get());
	}
	
	@Test
	public void testPersistence() {
		assertEquals(gameState, model.persistableGameState());
	}
	
	@Test
	public void testAddOptionKey() {
		assertNotNull(model.addOptionKey());
	}
	
	@Test
	public void testIdText() {
		assertEquals("State Id", model.idText().get());
	}
	
	@Test
	public void testTextLogText() {
		assertEquals("Text Log", model.textLogText().get());
	}
	
	@Test
	public void testMacroText() {
		assertEquals("Macro", model.macroText().get());
	}
	
	@Test
	public void testLayoutText() {
		assertEquals("Layout", model.layoutText().get());
	}
	
	@Test
	public void testContentText() {
		assertEquals("Content", model.contentText().get());
	}
	
	@Test
	public void testBrowseText() {
		assertEquals("Browse", model.browseText().get());
	}
	
	@Test
	public void testOptionText() {
		assertEquals("Options", model.optionText().get());
	}
	
	@Test
	public void testTimerText() {
		assertEquals("Timers", model.timerText().get());
	}
	
	@Test
	public void testAddOptionFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addOptionFromLibrary();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(dialog);
	}
	
	@Test
	public void testAddTimerKey() {
		assertNotNull(model.addTimerKey());
	}
	
	@Test
	public void testAddTimer() {
		model.addTimer();
		verify(gameState, times(1)).addTimer(any());
		assertEquals(1, model.timers().size());
	}
	
	@Test
	public void testAddTimerFromLibrary() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		model.addTimerFromLibrary();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class));
	}
	
	@Test
	public void testHasContentWithNoLoayout() {
		assertFalse(model.hasContent());
	}
	
	@Test
	public void testHasContentWithStaticContentLayout() {
		when(layout.getLayoutType()).thenReturn(LayoutType.ContentOnly);
		model.layouts().selected().set("ContentOnly");
		assertTrue(model.hasContent());
	}
	
	@Test
	public void testHasContentWithStaticNonContentLayout() {
		when(layout.getLayoutType()).thenReturn(LayoutType.TextWithTextInput);
		model.layouts().selected().set("TextWithTextInput");
		assertFalse(model.hasContent());
	}
	
	@Test
	public void testHasContentWithCustomNonContentLayout() {
		when(layout.getLayoutType()).thenReturn(LayoutType.Custom);
		model.layouts().selected().set("Test");
		assertFalse(model.hasContent());
	}
	
	@Test
	public void testHasContentWithCustomContentLayout() {
		when(layout.getLayoutType()).thenReturn(LayoutType.Custom);
		model.layouts().selected().set("Test2");
		assertTrue(model.hasContent());
	}
	
	@Test
	public void testAllowLibraryAdd() {
		assertTrue(model.allowLibraryAdd());
	}
	
	@Test
	public void testAllowLibraryAddWithoutLibrary() {
		GameStateModel mod = new GameStateModel(gameState, dialogService, null, macroFactory, mediaFinder, triggerFactory, playerModFactory,
				players, gameLayouts, languageService, actionViewFactory, dialogProvider, styleService, urlProvider);
		
		assertFalse(mod.allowLibraryAdd());
	}
	
	@Test
	public void testBuildMacro() {
		//TODO Finish
		Dialog dialog = mock(Dialog.class);
		when(dialog.isValid()).thenReturn(new SimpleBooleanProperty());
		when(dialogProvider.create(any())).thenReturn(dialog);
		MacroBuilderView view = mock(MacroBuilderView.class);
		MacroBuilderModel m = mock(MacroBuilderModel.class);
		when(m.valid()).thenReturn(new SimpleBooleanProperty());
		when(view.model()).thenReturn(m);
		when(macroFactory.build()).thenReturn(view);
		model.buildMacro();
		
		verify(macroFactory, times(1)).build();
		verify(dialogProvider, times(1)).create(any());
		verify(dialogService, times(1)).displayModal(any(Dialog.class), eq("Macro"));
	}
}
