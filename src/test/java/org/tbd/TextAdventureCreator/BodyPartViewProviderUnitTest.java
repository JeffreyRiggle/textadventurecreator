package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.BodyPartViewProvider;
import textadventurelib.persistence.player.BodyPartPersistenceObject;

public class BodyPartViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private BodyPartPersistenceObject bodyPart;
	private IDialogService dialogService;
	private ILanguageService languageService;
	private IDialogProvider dialogProvider;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	
	private BodyPartViewProvider provider;
	
	@Before
	public void setup() {
		bodyPart = mock(BodyPartPersistenceObject.class);
		dialogService = mock(IDialogService.class);
		languageService = mock(ILanguageService.class);
		dialogProvider = mock(IDialogProvider.class);
		styleService = mock(IStyleContainerService.class);
		urlProvider = mock(InternalURLProvider.class);
		
		provider = new BodyPartViewProvider(bodyPart, dialogService, languageService, dialogProvider, styleService, urlProvider);
	}
	
	@Test
	public void testGetView() {
		assertNotNull(provider.getView());
	}

}
