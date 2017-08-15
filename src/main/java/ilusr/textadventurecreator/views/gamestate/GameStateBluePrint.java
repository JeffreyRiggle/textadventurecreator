package ilusr.textadventurecreator.views.gamestate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ilusr.core.datamanager.xml.XmlGenerator;
import ilusr.core.datamanager.xml.XmlInputReader;
import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.iroshell.services.ITabContentBluePrint;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.library.LibraryService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.macro.MacroBuilderViewFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;
import textadventurelib.persistence.GameStatePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateBluePrint implements ITabContentBluePrint {

	private final String GAME_STATE_NAME_PERSISTENCE = "GameStateName: ";
	
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final MacroBuilderViewFactory macroFactory;
	private final MediaFinder mediaFinder;
	private final TextAdventureProvider provider;
	private final TriggerViewFactory triggerFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final ActionViewFactory actionViewFactory;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to manage library items.
	 * @param macroFactory A @see MacroBuilderViewFactory to create macro views.
	 * @param mediaFinder A @see MediaFinder to find and convert media.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mods.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param actionViewFactory A @see ActionViewFactory to create action views.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public GameStateBluePrint(IDialogService dialogService,
							  LibraryService libraryService,
							  MacroBuilderViewFactory macroFactory, 
							  MediaFinder mediaFinder,
							  TextAdventureProvider provider,
							  TriggerViewFactory triggerFactory,
							  PlayerModProviderFactory playerModFactory,
							  ILanguageService languageService,
							  ActionViewFactory actionViewFactory,
							  IDialogProvider dialogProvider,
							  IStyleContainerService styleService,
							  InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.macroFactory = macroFactory;
		this.mediaFinder = mediaFinder;
		this.provider = provider;
		this.triggerFactory = triggerFactory;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.actionViewFactory = actionViewFactory;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public ITabContent create() {
		LogRunner.logger().info("Creating game state tab.");
		return ServiceManager.getInstance().<GameStateContentTab>get("GameStateContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		GameStateContentTab tab = null;
		
		if (customData.startsWith(GAME_STATE_NAME_PERSISTENCE)) {
			LogRunner.logger().info(String.format("Creating game state tab from: %s", customData));
			return findGameStateInfoTab(customData.substring(GAME_STATE_NAME_PERSISTENCE.length()));
		}
		
		try {
			LogRunner.logger().info("Creating game state tab from xml");
			InputStream stream = new ByteArrayInputStream(customData.getBytes(StandardCharsets.UTF_8));
			XmlInputReader reader = new XmlInputReader(stream);
			XmlManager manager = new XmlManager(new String(), new XmlGenerator(), reader);
			XmlConfigurationManager configurationManager = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			configurationManager.load();
			
			GameStatePersistenceObject gameState = new GameStatePersistenceObject(new String());
			gameState.convertFromPersistence((XmlConfigurationObject)configurationManager.configurationObjects().get(0));
			
			for (GameStatePersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().gameStates()) {
				if (obj.stateId().equals(gameState.stateId())) {
					provider.getTextAdventureProject().getTextAdventure().gameStates().remove(obj);
					break;
				}
			}
			
			provider.getTextAdventureProject().getTextAdventure().addGameState(gameState);
			
			GameStateModel model = new GameStateModel(gameState, dialogService, libraryService, macroFactory, mediaFinder,
					triggerFactory, playerModFactory, provider, languageService, actionViewFactory, dialogProvider,
					styleService, urlProvider);
			
			tab = new GameStateContentTab(new GameStateView(model, languageService, styleService, urlProvider), languageService);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tab;
	}

	private GameStateContentTab findGameStateInfoTab(String gameState) {
		GameStatePersistenceObject state = null;
		try {
			state = new GameStatePersistenceObject(new String());
			for (GameStatePersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().gameStates()) {
				if (obj.stateId().equals(gameState)) {
					state = obj;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GameStateModel model = new GameStateModel(state, dialogService, libraryService, macroFactory, mediaFinder,
				triggerFactory, playerModFactory, provider, languageService, actionViewFactory, dialogProvider,
				styleService, urlProvider);
		return new GameStateContentTab(new GameStateView(model, languageService, styleService, urlProvider), languageService);
	}
}
