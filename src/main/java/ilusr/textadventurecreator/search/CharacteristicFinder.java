package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CharacteristicFinder extends BaseFinder<CharacteristicPersistenceObject> {

	/**
	 * 
	 * @param model A @see CharacteristicFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public CharacteristicFinder(CharacteristicFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<CharacteristicPersistenceObject> creator() {
		return (c) -> {
			return new Label(c.objectName());
		};
	}
}
