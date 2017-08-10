package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.services.IDialogService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.mod.ModManager;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingsViewRepository;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.style.ThemeService;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.scene.Node;

public class SettingsViewRepositoryUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ISettingsManager settingsManager;
	private ILanguageService languageService; 
	private IDialogService dialogService;
	private TextAdventureProvider provider;
	private ModManager modManager;
	private ThemeService themeService;
	private IDialogProvider dialogProvider;
	
	private SettingsViewRepository repository;
	
	@Before
	public void setup() {
		settingsManager = mock(ISettingsManager.class);
		languageService = mock(ILanguageService.class);
		dialogService = mock(IDialogService.class);
		provider = mock(TextAdventureProvider.class);
		modManager = mock(ModManager.class);
		themeService = mock(ThemeService.class);
		dialogProvider = mock(IDialogProvider.class);
		
		repository = new SettingsViewRepository(settingsManager, languageService, dialogService, provider, modManager, themeService, dialogProvider);
	}
	
	@Test
	public void testInitialViews() {
		assertEquals(0, repository.getRegisteredViews().size());
		repository.initialize();
		assertEquals(6, repository.getRegisteredViews().size());
	}

	@Test
	public void testRegisterView() {
		Node node = mock(Node.class);
		repository.register("TestView", node);
		assertTrue(repository.getRegisteredViews().keySet().contains("TestView"));
		assertTrue(repository.getRegisteredViews().values().contains(node));
	}
	
	@Test
	public void testUnRegisterView() {
		Node node = mock(Node.class);
		repository.register("TestView", node);
		repository.unRegister("TestView");
		assertFalse(repository.getRegisteredViews().keySet().contains("TestView"));
		assertFalse(repository.getRegisteredViews().values().contains(node));
	}
}
