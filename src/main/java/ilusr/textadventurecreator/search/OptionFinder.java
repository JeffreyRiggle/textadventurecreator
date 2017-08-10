package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.OptionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionFinder extends BaseFinder<OptionPersistenceObject> {

	/**
	 * 
	 * @param model A @see OptionFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public OptionFinder(OptionFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<OptionPersistenceObject> creator() {
		return (o) -> {
			return new Label("Action: " + o.action().type() + ", Triggers: " + o.triggers().size());
		};
	}
}
