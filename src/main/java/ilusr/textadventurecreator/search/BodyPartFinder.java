package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.player.BodyPartPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartFinder extends BaseFinder<BodyPartPersistenceObject>{

	/**
	 * 
	 * @param model A @see BodyPartFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BodyPartFinder(BodyPartFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<BodyPartPersistenceObject> creator() {
		return (b) -> {
			return new Label(b.objectName());
		};
	}
}
