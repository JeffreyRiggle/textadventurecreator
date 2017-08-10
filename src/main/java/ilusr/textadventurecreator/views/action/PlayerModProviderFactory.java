package ilusr.textadventurecreator.views.action;

import java.util.List;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.player.AttributeViewProvider;
import ilusr.textadventurecreator.views.player.BodyPartViewProvider;
import ilusr.textadventurecreator.views.player.CharacteristicViewProvider;
import ilusr.textadventurecreator.views.player.InventoryItem;
import ilusr.textadventurecreator.views.player.InventoryItemViewProvider;
import ilusr.textadventurecreator.views.player.InventorySelectorViewProvider;
import ilusr.textadventurecreator.views.player.RemoveCharacteristicViewProvider;
import textadventurelib.core.ModificationType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModProviderFactory {

	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 */
	public PlayerModProviderFactory(IDialogService dialogService, 
									ILanguageService languageService, 
									IDialogProvider dialogProvider,
									IStyleContainerService styleService,
									InternalURLProvider urlProvider) {
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	/**
	 * 
	 * @param action The action to use to create the view provider.
	 * @return The created view provider.
	 */
	public PlayerModificationActionViewProvider create(ModifyPlayerActionPersistence action) {
		if (action.modificationType() == ModificationType.Add) {
			return buildProvidersFromAction(action);
		}
		
		TextAdventureProvider provider = ServiceManager.getInstance().get("TextAdventureProvider");
		ChangePlayerPropertyViewProvider changeProvider = ServiceManager.getInstance().get("ChangePlayerPropertyViewProvider");
		RemovePlayerPropertyViewProvider removeProvider = ServiceManager.getInstance().get("RemovePlayerPropertyViewProvider");
		AttributeViewProvider attributeProvider = ServiceManager.getInstance().get("AttributeViewProvider");
		CharacteristicViewProvider characteristicProvider = ServiceManager.getInstance().get("CharacteristicViewProvider");
		BodyPartViewProvider bodyPartProvider = ServiceManager.getInstance().get("BodyPartViewProvider");
		InventoryItemViewProvider inventoryViewProvider = ServiceManager.getInstance().get("InventoryItemViewProvider");
		RemoveCharacteristicViewProvider removeCharacteristicProvider = ServiceManager.getInstance().get("RemoveCharacteristicViewProvider");
		InventorySelectorViewProvider equipmentProvider = ServiceManager.getInstance().get("InventorySelectorViewProvider");
		
		return new PlayerModificationActionViewProvider(action, provider, changeProvider, removeProvider, attributeProvider,
				characteristicProvider, bodyPartProvider, inventoryViewProvider, removeCharacteristicProvider, equipmentProvider, languageService);
	}
	
	/**
	 * 
	 * @param action The action to use to create the view provider.
	 * @param players The players to use in the creation.
	 * @return The created view provider.
	 */
	public PlayerModificationActionViewProvider create(ModifyPlayerActionPersistence action, List<PlayerPersistenceObject> players) {
		if (action.modificationType() == ModificationType.Add) {
			return buildProvidersFromAction(action, players);
		}
		
		ChangePlayerPropertyViewProvider changeProvider = ServiceManager.getInstance().get("ChangePlayerPropertyViewProvider");
		RemovePlayerPropertyViewProvider removeProvider = ServiceManager.getInstance().get("RemovePlayerPropertyViewProvider");
		AttributeViewProvider attributeProvider = ServiceManager.getInstance().get("AttributeViewProvider");
		CharacteristicViewProvider characteristicProvider = ServiceManager.getInstance().get("CharacteristicViewProvider");
		BodyPartViewProvider bodyPartProvider = ServiceManager.getInstance().get("BodyPartViewProvider");
		InventoryItemViewProvider inventoryViewProvider = ServiceManager.getInstance().get("InventoryItemViewProvider");
		RemoveCharacteristicViewProvider removeCharacteristicProvider = ServiceManager.getInstance().get("RemoveCharacteristicViewProvider");
		InventorySelectorViewProvider equipmentProvider = ServiceManager.getInstance().get("InventorySelectorViewProvider");
		
		return new PlayerModificationActionViewProvider(action, players, changeProvider, removeProvider, attributeProvider,
				characteristicProvider, bodyPartProvider, inventoryViewProvider, removeCharacteristicProvider, equipmentProvider, languageService);
	}
	
	private PlayerModificationActionViewProvider buildProvidersFromAction(ModifyPlayerActionPersistence action) {
		TextAdventureProvider provider = ServiceManager.getInstance().get("TextAdventureProvider");
		ChangePlayerPropertyViewProvider changeProvider = ServiceManager.getInstance().get("ChangePlayerPropertyViewProvider");
		RemovePlayerPropertyViewProvider removeProvider = ServiceManager.getInstance().get("RemovePlayerPropertyViewProvider");
		RemoveCharacteristicViewProvider removeCharacteristicProvider = ServiceManager.getInstance().get("RemoveCharacteristicViewProvider");
		AttributeViewProvider attributeProvider = ServiceManager.getInstance().get("AttributeViewProvider");
		CharacteristicViewProvider characteristicProvider = ServiceManager.getInstance().get("CharacteristicViewProvider");
		BodyPartViewProvider bodyPartProvider = ServiceManager.getInstance().get("BodyPartViewProvider");
		InventoryItemViewProvider inventoryProvider = ServiceManager.getInstance().get("InventoryItemViewProvider");
		InventorySelectorViewProvider equipProvider = ServiceManager.getInstance().get("InventorySelectorViewProvider");
		
		if (action.data() instanceof AttributePersistenceObject) {
			attributeProvider = new AttributeViewProvider((AttributePersistenceObject)action.data(), languageService);
		} else if (action.data() instanceof CharacteristicPersistenceObject) {
			characteristicProvider = new CharacteristicViewProvider((CharacteristicPersistenceObject)action.data(), languageService);
		} else if (action.data() instanceof BodyPartPersistenceObject) {
			bodyPartProvider = new BodyPartViewProvider((BodyPartPersistenceObject)action.data(), dialogService, languageService, dialogProvider, styleService, urlProvider);
		} else if (action.id() instanceof ItemPersistenceObject) {
			//TODO review this.
			inventoryProvider = new InventoryItemViewProvider(dialogService, new InventoryItem((ItemPersistenceObject)action.id(), 0), languageService, dialogProvider, styleService, urlProvider);
		} else if ((action.id() instanceof BodyPartPersistenceObject) && action.data() instanceof ItemPersistenceObject) {
			String item = ((ItemPersistenceObject)action.data()).itemName();
			InventoryPersistenceObject inv = findInventory(action.playerName(), provider.getTextAdventureProject().getTextAdventure().players());
			equipProvider = new InventorySelectorViewProvider(inv, item, languageService);
		}
		
		return new PlayerModificationActionViewProvider(action, provider, changeProvider, removeProvider, attributeProvider,
				characteristicProvider, bodyPartProvider, inventoryProvider, removeCharacteristicProvider, equipProvider, languageService);
	}
	
	private PlayerModificationActionViewProvider buildProvidersFromAction(ModifyPlayerActionPersistence action, List<PlayerPersistenceObject> players) {
		ChangePlayerPropertyViewProvider changeProvider = ServiceManager.getInstance().get("ChangePlayerPropertyViewProvider");
		RemovePlayerPropertyViewProvider removeProvider = ServiceManager.getInstance().get("RemovePlayerPropertyViewProvider");
		RemoveCharacteristicViewProvider removeCharacteristicProvider = ServiceManager.getInstance().get("RemoveCharacteristicViewProvider");
		AttributeViewProvider attributeProvider = ServiceManager.getInstance().get("AttributeViewProvider");
		CharacteristicViewProvider characteristicProvider = ServiceManager.getInstance().get("CharacteristicViewProvider");
		BodyPartViewProvider bodyPartProvider = ServiceManager.getInstance().get("BodyPartViewProvider");
		InventoryItemViewProvider inventoryProvider = ServiceManager.getInstance().get("InventoryItemViewProvider");
		InventorySelectorViewProvider equipProvider = ServiceManager.getInstance().get("InventorySelectorViewProvider");
		
		if (action.data() instanceof AttributePersistenceObject) {
			attributeProvider = new AttributeViewProvider((AttributePersistenceObject)action.data(), languageService);
		} else if (action.data() instanceof CharacteristicPersistenceObject) {
			characteristicProvider = new CharacteristicViewProvider((CharacteristicPersistenceObject)action.data(), languageService);
		} else if (action.data() instanceof BodyPartPersistenceObject) {
			bodyPartProvider = new BodyPartViewProvider((BodyPartPersistenceObject)action.data(), dialogService, languageService, dialogProvider, styleService, urlProvider);
		} else if (action.id() instanceof ItemPersistenceObject) {
			//TODO review this.
			inventoryProvider = new InventoryItemViewProvider(dialogService, new InventoryItem((ItemPersistenceObject)action.id(), 0), languageService, dialogProvider, styleService, urlProvider);
		} else if ((action.id() instanceof BodyPartPersistenceObject) && action.data() instanceof ItemPersistenceObject) {
			String item = ((ItemPersistenceObject)action.data()).itemName();
			InventoryPersistenceObject inv = findInventory(action.playerName(), players);
			equipProvider = new InventorySelectorViewProvider(inv, item, languageService);
		}
		
		return new PlayerModificationActionViewProvider(action, players, changeProvider, removeProvider, attributeProvider,
				characteristicProvider, bodyPartProvider, inventoryProvider, removeCharacteristicProvider, equipProvider, languageService);
	}
	
	private InventoryPersistenceObject findInventory(String playerName, List<PlayerPersistenceObject> players) {
		InventoryPersistenceObject retVal = null;
		try {
			retVal = new InventoryPersistenceObject();
			for (PlayerPersistenceObject player : players) {
				if (player.playerName().equals(playerName)) {
					retVal = player.inventory();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
}
