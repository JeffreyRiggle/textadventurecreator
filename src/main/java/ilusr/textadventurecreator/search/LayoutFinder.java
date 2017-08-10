package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.LayoutPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutFinder extends BaseFinder<LayoutPersistenceObject> {

	/**
	 * 
	 * @param model A @see LayoutFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public LayoutFinder(LayoutFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<LayoutPersistenceObject> creator() {
		return l -> { return new Label(l.id()); };
	}
}
