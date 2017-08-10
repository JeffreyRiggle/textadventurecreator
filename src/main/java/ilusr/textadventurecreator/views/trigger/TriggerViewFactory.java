package ilusr.textadventurecreator.views.trigger;

import java.util.List;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.IDialogProvider;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerViewFactory {

	private final TextAdventureProvider provider;
	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public TriggerViewFactory(TextAdventureProvider provider, 
							  IDialogService dialogService,
							  IDialogProvider dialogProvider,
							  ILanguageService languageService,
							  IStyleContainerService styleService,
							  InternalURLProvider urlProvider) {
		this.provider = provider;
		this.dialogService = dialogService;
		this.dialogProvider = dialogProvider;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	/**
	 * 
	 * @param trigger The trigger to use.
	 * @return A trigger view associated to the provide trigger.
	 */
	public TriggerView create(TriggerModel trigger) {
		TextTriggerViewProvider textProvider = ServiceManager.getInstance().<TextTriggerViewProvider>get("TextTriggerViewProvider");
		PlayerTriggerViewProvider playerProvider = ServiceManager.getInstance().<PlayerTriggerViewProvider>get("PlayerTriggerViewProvider");
		ScriptTriggerViewProvider scriptProvider = ServiceManager.getInstance().<ScriptTriggerViewProvider>get("ScriptTriggerViewProvider");
		MultiTriggerViewProvider multiProvider = ServiceManager.getInstance().<MultiTriggerViewProvider>get("MultiTriggerViewProvider");
		return new TriggerView(trigger, textProvider, playerProvider, scriptProvider, multiProvider, provider,
				dialogService, dialogProvider, this, languageService, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param trigger The trigger to use.
	 * @param players The available players.
	 * @return A trigger view associated to the provide trigger.
	 */
	public TriggerView create(TriggerModel trigger, List<PlayerPersistenceObject> players) {
		PlayerTriggerPersistenceObject playerTrigger = null;
		
		try {
			playerTrigger = new PlayerTriggerPersistenceObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TextTriggerViewProvider textProvider = ServiceManager.getInstance().<TextTriggerViewProvider>get("TextTriggerViewProvider");
		PlayerTriggerViewProvider playerProvider = new PlayerTriggerViewProvider(playerTrigger, players, languageService);
		ScriptTriggerViewProvider scriptProvider = ServiceManager.getInstance().<ScriptTriggerViewProvider>get("ScriptTriggerViewProvider");
		MultiTriggerViewProvider multiProvider = ServiceManager.getInstance().<MultiTriggerViewProvider>get("MultiTriggerViewProvider");
		return new TriggerView(trigger, textProvider, playerProvider, scriptProvider, multiProvider, players,
				dialogService, dialogProvider, this, languageService, styleService, urlProvider);
	}
}
