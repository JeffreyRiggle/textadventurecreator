package ilusr.textadventurecreator.views.action;

import java.util.List;

import ilusr.core.ioc.ServiceManager;
import ilusr.textadventurecreator.language.ILanguageService;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionViewFactory {

	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param playerModFactory A view provider of player mod views.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ActionViewFactory(PlayerModProviderFactory playerModFactory, ILanguageService languageService) {
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
	}
	
	/**
	 * 
	 * @param action A @see ActionModel to use when creating the view.
	 * @return A @see ActionView to display.
	 */
	public ActionView create(ActionModel action) {
		AppendTextViewProvider appendProvider = ServiceManager.getInstance().get("AppendTextViewProvider");
		CompletionActionViewProvider completeProvider = ServiceManager.getInstance().get("CompletionActionViewProvider");
		ExecutableActionViewProvider exeProvider = ServiceManager.getInstance().get("ExecutableActionViewProvider");
		SaveActionViewProvider saveProvider = ServiceManager.getInstance().get("SaveActionViewProvider");
		PlayerModificationActionViewProvider playerProvider = ServiceManager.getInstance().get("PlayerModificationActionViewProvider");
		ScriptedActionViewProvider scriptProvider = ServiceManager.getInstance().get("ScriptedActionViewProvider");
		FinishActionViewProvider finishProvider = ServiceManager.getInstance().get("FinishActionViewProvider");
		
		return new ActionView(action, appendProvider, completeProvider, exeProvider, saveProvider, playerProvider, scriptProvider, finishProvider, playerModFactory, languageService);
	}
	
	/**
	 * 
	 * @param action A @see ActionModel to use when creating the view.
	 * @param players A list of players to use.
	 * @return A @see ActionView to display.
	 */
	public ActionView create(ActionModel action, List<PlayerPersistenceObject> players) {
		ModifyPlayerActionPersistence model = null;
		if (action.persistenceObject().get() instanceof ModifyPlayerActionPersistence) {
			model = (ModifyPlayerActionPersistence)action.persistenceObject().get();
		} else {
			try {
				model = new ModifyPlayerActionPersistence();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		AppendTextViewProvider appendProvider = ServiceManager.getInstance().get("AppendTextViewProvider");
		CompletionActionViewProvider completeProvider = ServiceManager.getInstance().get("CompletionActionViewProvider");
		ExecutableActionViewProvider exeProvider = ServiceManager.getInstance().get("ExecutableActionViewProvider");
		SaveActionViewProvider saveProvider = ServiceManager.getInstance().get("SaveActionViewProvider");
		PlayerModificationActionViewProvider playerProvider = playerModFactory.create(model, players);
		ScriptedActionViewProvider scriptProvider = ServiceManager.getInstance().get("ScriptedActionViewProvider");
		FinishActionViewProvider finishProvider = ServiceManager.getInstance().get("FinishActionViewProvider");
		
		return new ActionView(action, appendProvider, completeProvider, exeProvider, saveProvider, playerProvider, scriptProvider, finishProvider, playerModFactory, players, languageService);
	}
}
