package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.GameStatePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateFinder extends BaseFinder<GameStatePersistenceObject> {

	/**
	 * 
	 * @param model A @see GameStateFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public GameStateFinder(GameStateFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<GameStatePersistenceObject> creator() {
		return (g) -> {
			return new Label(g.stateId());
		};
	}
}
