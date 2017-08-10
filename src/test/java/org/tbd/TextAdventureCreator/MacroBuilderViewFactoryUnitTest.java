package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class MacroBuilderViewFactoryUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private TextAdventureProvider provider;
	private ILanguageService languageService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private MacroBuilderViewFactory factory;
	
	@Before
	public void setup() {
		provider = mock(TextAdventureProvider.class);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		TextAdventurePersistenceObject tav = mock(TextAdventurePersistenceObject.class);
		when(tav.players()).thenReturn(Arrays.asList(mock(PlayerPersistenceObject.class)));
		when(proj.getTextAdventure()).thenReturn(tav);
		when(provider.getTextAdventureProject()).thenReturn(proj);
		languageService = mock(ILanguageService.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		factory = new MacroBuilderViewFactory(provider, languageService, styleService, urlProvider);
	}
	
	@Test
	public void testBuild() {
		assertNotNull(factory.build());
	}

}
