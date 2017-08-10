package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.ActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionFinder extends BaseFinder<ActionPersistenceObject> {

	/**
	 * 
	 * @param model A @see ActionFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public ActionFinder(ActionFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<ActionPersistenceObject> creator() {
		return (a) -> {
			return new Label(a.type());
		};
	}
}
