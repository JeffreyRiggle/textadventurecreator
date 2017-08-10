package ilusr.textadventurecreator.views.player;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import textadventurelib.persistence.player.AttributePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AttributeViewProvider implements IViewProvider<PlayerDataView> {

	private final AttributePersistenceObject attribute;
	private final ILanguageService languageService;
	private AttributeViewer view;
	
	/**
	 * 
	 * @param attribute The @see AttributePersistenceObject to provide a view for.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public AttributeViewProvider(AttributePersistenceObject attribute, ILanguageService languageService) {
		this.attribute = attribute;
		this.languageService = languageService;
	}

	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new AttributeViewer(new NamedPersistenceObjectModel(attribute), languageService);
		}
		
		return view;
	}
}
