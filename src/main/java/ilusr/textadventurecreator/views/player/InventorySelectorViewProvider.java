package ilusr.textadventurecreator.views.player;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import textadventurelib.persistence.player.InventoryPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventorySelectorViewProvider implements IViewProvider<PlayerDataView> {

	private final ILanguageService languageService;
	private InventorySelector view;
	private InventoryPersistenceObject inventory;
	private String selected;
	
	/**
	 * 
	 * @param inventory The @see InventoryPersistenceObject to create a view for.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public InventorySelectorViewProvider(InventoryPersistenceObject inventory, ILanguageService languageService) {
		this(inventory, null, languageService);
	}
	
	/**
	 * 
	 * @param inventory The @see InventoryPersistenceObject to create a view for.
	 * @param selected The initially selected item.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public InventorySelectorViewProvider(InventoryPersistenceObject inventory, String selected, ILanguageService languageService) {
		this.inventory = inventory;
		this.selected = selected;
		this.languageService = languageService;
	}
	
	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new InventorySelector(inventory, selected, languageService);
		}
		
		return view;
	}
}
