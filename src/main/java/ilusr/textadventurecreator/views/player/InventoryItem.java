package ilusr.textadventurecreator.views.player;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import textadventurelib.persistence.player.ItemPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventoryItem {

	private ItemPersistenceObject item;
	private int amount;
	private SimpleObjectProperty<ItemPersistenceObject> oItem;
	private SimpleIntegerProperty oAmount;
	private SimpleBooleanProperty valid;
	
	/**
	 * 
	 * @param item A @see ItemPersistenceObject to bind to.
	 * @param amount The amount of that item.
	 */
	public InventoryItem(ItemPersistenceObject item, int amount) {
		this.item = item;
		this.amount = amount;
		oItem = new SimpleObjectProperty<ItemPersistenceObject>(item);
		oAmount = new SimpleIntegerProperty(amount);
		valid = new SimpleBooleanProperty(item.itemName() != null && !item.itemName().isEmpty());
	}
	
	/**
	 * 
	 * @param item The new @see ItemPersistenceObject to use.
	 */
	public void setItem(ItemPersistenceObject item) {
		this.item = item;
		valid.set(item.itemName() != null && !item.itemName().isEmpty());
		oItem.set(item);
	}
	
	/**
	 * 
	 * @return The current item to use.
	 */
	public ItemPersistenceObject getItem() {
		return item;
	}
	
	/**
	 * 
	 * @param amount The new amount of the item.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
		oAmount.set(amount);
	}
	
	/**
	 * 
	 * @return The current amount of the item.
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return The item to use.
	 */
	public SimpleObjectProperty<ItemPersistenceObject> item() {
		return oItem;
	}
	
	/**
	 * 
	 * @return The amount of the item.
	 */
	public SimpleIntegerProperty amount() {
		return oAmount;
	}
	
	/**
	 * 
	 * @return If the item is valid or not.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", item, amount);
	}
}
