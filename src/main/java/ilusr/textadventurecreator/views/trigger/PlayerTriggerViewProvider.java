package ilusr.textadventurecreator.views.trigger;

import java.util.List;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerTriggerViewProvider implements IViewProvider<BaseTriggerView> {

	private final PlayerTriggerPersistenceObject trigger;
	private final List<PlayerPersistenceObject> players;
	private final ILanguageService languageService;
	private PlayerTriggerView view;
	
	/**
	 * 
	 * @param trigger The trigger to create a view for.
	 * @param provider A @see TextAdventureProvider to provide the curent text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayerTriggerViewProvider(PlayerTriggerPersistenceObject trigger, 
									 TextAdventureProvider provider,
									 ILanguageService languageService) {
		this(trigger, provider.getTextAdventureProject().getTextAdventure().players(), languageService);
	}
	
	/**
	 * 
	 * @param trigger The trigger to create a view for.
	 * @param players The players to use for the trigger.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayerTriggerViewProvider(PlayerTriggerPersistenceObject trigger, 
									 List<PlayerPersistenceObject> players,
									 ILanguageService languageService) {
		this.trigger = trigger;
		this.players = players;
		this.languageService = languageService;
	}
	
	@Override
	public BaseTriggerView getView() {
		if (view == null) {
			view = new PlayerTriggerView(new PlayerTriggerModel(trigger, players, languageService));
		}
		
		return view;
	}

}
