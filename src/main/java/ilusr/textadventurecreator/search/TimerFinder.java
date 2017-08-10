package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.TimerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TimerFinder extends BaseFinder<TimerPersistenceObject> {

	/**
	 * 
	 * @param model A @see TimerFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public TimerFinder(TimerFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<TimerPersistenceObject> creator() {
		return (t) -> {
			return new Label(t.type());
		};
	}
}
