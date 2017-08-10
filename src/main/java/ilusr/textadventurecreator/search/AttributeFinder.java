package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.player.AttributePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AttributeFinder extends BaseFinder<AttributePersistenceObject> {
	
	/**
	 * 
	 * @param model A @see AttributeFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public AttributeFinder(AttributeFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<AttributePersistenceObject> creator() {
		return (a) -> {
			return new Label(a.objectName());
		};
	}
}
