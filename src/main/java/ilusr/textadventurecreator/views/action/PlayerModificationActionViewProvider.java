package ilusr.textadventurecreator.views.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.player.AttributeViewProvider;
import ilusr.textadventurecreator.views.player.BodyPartViewProvider;
import ilusr.textadventurecreator.views.player.CharacteristicViewProvider;
import ilusr.textadventurecreator.views.player.InventoryItemViewProvider;
import ilusr.textadventurecreator.views.player.InventorySelectorViewProvider;
import ilusr.textadventurecreator.views.player.RemoveCharacteristicViewProvider;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationActionViewProvider implements IViewProvider<BaseActionView> {

	private final ModifyPlayerActionPersistence action;
	private final List<PlayerPersistenceObject> players;
	private final ILanguageService languageService;
	
	private PlayerModificationActionView view;
	private Map<String, IViewProvider<PlayerDataView>> viewProviders;
	
	/**
	 * 
	 * @param action The action to use to create the view.
	 * @param provider The provider for the text adventure.
	 * @param changeProvider A view provider for changes to player properties.
	 * @param removeProvider A view provider for removes of player properties.
	 * @param attributeProvider A view provider for attributes.
	 * @param characteristicProvider A view provider for characteristics.
	 * @param bodyPartProvider A view provider for body parts.
	 * @param inventoryViewProvider A view provider for items.
	 * @param removeCharacteristicProvider A view provider for removing characteristics.
	 * @param equipmentProvider A view provider for equipment.
	 * @param languageService A @see LanaguageService to provide display strings.
	 */
	public PlayerModificationActionViewProvider(ModifyPlayerActionPersistence action, 
			TextAdventureProvider provider,
			ChangePlayerPropertyViewProvider changeProvider,
			RemovePlayerPropertyViewProvider removeProvider,
			AttributeViewProvider attributeProvider,
			CharacteristicViewProvider characteristicProvider,
			BodyPartViewProvider bodyPartProvider,
			InventoryItemViewProvider inventoryViewProvider,
			RemoveCharacteristicViewProvider removeCharacteristicProvider,
			InventorySelectorViewProvider equipmentProvider,
			ILanguageService languageService) {
		this(action, provider.getTextAdventureProject().getTextAdventure().players(),
			 changeProvider, removeProvider,
			 attributeProvider, characteristicProvider,
			 bodyPartProvider, inventoryViewProvider,
			 removeCharacteristicProvider, equipmentProvider, languageService);
	}
	
	/**
	 * 
	 * @param action The action to use to create the view.
	 * @param players The players to use.
	 * @param provider The provider for the text adventure.
	 * @param changeProvider A view provider for changes to player properties.
	 * @param removeProvider A view provider for removes of player properties.
	 * @param attributeProvider A view provider for attributes.
	 * @param characteristicProvider A view provider for characteristics.
	 * @param bodyPartProvider A view provider for body parts.
	 * @param inventoryViewProvider A view provider for items.
	 * @param removeCharacteristicProvider A view provider for removing characteristics.
	 * @param equipmentProvider A view provider for equipment.
	 * @param languageService A @see LanaguageService to provide display strings.
	 */
	public PlayerModificationActionViewProvider(ModifyPlayerActionPersistence action, 
												List<PlayerPersistenceObject> players,
												ChangePlayerPropertyViewProvider changeProvider,
												RemovePlayerPropertyViewProvider removeProvider,
												AttributeViewProvider attributeProvider,
												CharacteristicViewProvider characteristicProvider,
												BodyPartViewProvider bodyPartProvider,
												InventoryItemViewProvider inventoryViewProvider,
												RemoveCharacteristicViewProvider removeCharacteristicProvider,
												InventorySelectorViewProvider equipmentProvider,
												ILanguageService languageService) {
		this.action = action;
		this.players = players;
		this.languageService = languageService;
		
		viewProviders = new HashMap<String, IViewProvider<PlayerDataView>>();
		viewProviders.put("Change", changeProvider);
		viewProviders.put("Remove", removeProvider);
		viewProviders.put("Attribute", attributeProvider);
		viewProviders.put("Characteristic", characteristicProvider);
		viewProviders.put("BodyPart", bodyPartProvider);
		viewProviders.put("Inventory", inventoryViewProvider);
		viewProviders.put("RemoveCharacteristic", removeCharacteristicProvider);
		viewProviders.put("Equipment", equipmentProvider);
	}
	
	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new PlayerModificationActionView(new PlayerModificationActionModel(action, players, languageService), viewProviders);
		}
		
		return view;
	}

}
