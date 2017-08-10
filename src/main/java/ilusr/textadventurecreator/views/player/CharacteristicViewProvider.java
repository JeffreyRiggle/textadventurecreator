package ilusr.textadventurecreator.views.player;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CharacteristicViewProvider implements IViewProvider<PlayerDataView> {

	private final CharacteristicPersistenceObject characteristic;
	private final ILanguageService languageService;
	private CharacteristicViewer view;
	
	/**
	 * 
	 * @param characteristic A @see CharacteristicPersistenceObject to provide a view for.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CharacteristicViewProvider(CharacteristicPersistenceObject characteristic, ILanguageService languageService) {
		this.characteristic = characteristic;
		this.languageService = languageService;
	}

	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new CharacteristicViewer(new NamedPersistenceObjectModel(characteristic), languageService);
		}
		
		return view;
	}
}
