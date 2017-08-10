package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerFinder extends BaseFinder<TriggerPersistenceObject> {

	/**
	 * 
	 * @param model A @see TriggerFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public TriggerFinder(TriggerFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<TriggerPersistenceObject> creator() {
		return (t) -> {
			return new Label(t.type());
		};
	}
}
