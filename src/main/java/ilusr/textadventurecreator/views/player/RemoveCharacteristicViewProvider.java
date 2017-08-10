package ilusr.textadventurecreator.views.player;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import textadventurelib.persistence.player.BodyPartPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class RemoveCharacteristicViewProvider implements IViewProvider<PlayerDataView> {
	
	private final ILanguageService languageService;
	private RemoveCharacteristicView view;
	private BodyPartPersistenceObject bodyPart;
	
	/**
	 * 
	 * @param bodyPart A @see BodyPartPersistenceObject to use for the remove view.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public RemoveCharacteristicViewProvider(BodyPartPersistenceObject bodyPart, ILanguageService languageService) {
		this.bodyPart = bodyPart;
		this.languageService = languageService;
	}

	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new RemoveCharacteristicView(bodyPart, languageService);
		}
		
		return view;
	}
}
