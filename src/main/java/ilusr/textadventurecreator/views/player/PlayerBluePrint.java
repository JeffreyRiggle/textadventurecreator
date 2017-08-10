package ilusr.textadventurecreator.views.player;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;

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
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerBluePrint implements ITabContentBluePrint{
	
	private final String PLAYER_NAME_PERSISTENCE = "PlayerName: ";
	
	private final IDialogService dialogService;
	private final LibraryService libraryService;
	private final TextAdventureProvider provider;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param libraryService A @see LibraryService to get library items.
	 * @param provider A @see TextAdventureProvider to get the current text adventure.
	 * @param languageService A @see LanguageService to get display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public PlayerBluePrint(IDialogService dialogService, 
						   LibraryService libraryService, 
						   TextAdventureProvider provider,
						   ILanguageService languageService,
						   IDialogProvider dialogProvider,
						   IStyleContainerService styleService,
						   InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.libraryService = libraryService;
		this.provider = provider;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public ITabContent create() {
		LogRunner.logger().log(Level.INFO, "Creating player tab.");
		return ServiceManager.getInstance().<PlayerContentTab>get("PlayerContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		PlayerContentTab tab = null;
		
		if (customData.startsWith(PLAYER_NAME_PERSISTENCE)) {
			LogRunner.logger().log(Level.INFO, String.format("Creating player tab with player data %s.", customData));
			return findPlayerInfoTab(customData.substring(PLAYER_NAME_PERSISTENCE.length()));
		}
		
		try {
			LogRunner.logger().log(Level.INFO, "Creating player tab from persistence object.");
			InputStream stream = new ByteArrayInputStream(customData.getBytes(StandardCharsets.UTF_8));
			XmlInputReader reader = new XmlInputReader(stream);
			XmlManager manager = new XmlManager(new String(), new XmlGenerator(), reader);
			XmlConfigurationManager configurationManager = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			configurationManager.load();
			
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.convertFromPersistence((XmlConfigurationObject)configurationManager.configurationObjects().get(0));
			
			for (PlayerPersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().players()) {
				if (obj.playerName().equals(player.playerName())) {
					provider.getTextAdventureProject().getTextAdventure().players().remove(obj);
					break;
				}
			}
			
			provider.getTextAdventureProject().getTextAdventure().players().add(player);
			
			tab = new PlayerContentTab(new PlayerView(new PlayerModel(dialogService, libraryService, player, languageService,
					dialogProvider, styleService, urlProvider), languageService, styleService, urlProvider));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tab;
	}

	private PlayerContentTab findPlayerInfoTab(String playerName) {
		PlayerPersistenceObject player = null;
		
		try {
			player = new PlayerPersistenceObject();
			for (PlayerPersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().players()) {
				if (obj.playerName().equals(playerName)) {
					player = obj;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new PlayerContentTab(new PlayerView(new PlayerModel(dialogService, libraryService, player, languageService,
				dialogProvider, styleService, urlProvider), languageService, styleService, urlProvider));
	}
}
