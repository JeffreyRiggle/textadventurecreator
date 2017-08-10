package ilusr.textadventurecreator.search;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.Label;
import textadventurelib.persistence.player.ItemPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ItemFinder extends BaseFinder<ItemPersistenceObject> {

	/**
	 * 
	 * @param model The @see ItemFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public ItemFinder(ItemFinderModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		super(model, styleService, urlProvider);
	}

	@Override
	protected IViewCreator<ItemPersistenceObject> creator() {
		return (i) -> {
			return new Label(i.itemName());
		};
	}
}
