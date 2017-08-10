package ilusr.textadventurecreator.language;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LanguageItem {

	private SimpleStringProperty keyword;
	private SimpleStringProperty enValue;
	private SimpleStringProperty newValue;
	
	/**
	 * 
	 * @param keyword The key for the language item.
	 * @param enValue The English value for the item.
	 * @param newValue The new value for the item.
	 */
	public LanguageItem(String keyword, String enValue, String newValue) {
		this.keyword = new SimpleStringProperty(keyword);
		this.enValue = new SimpleStringProperty(enValue);
		this.newValue = new SimpleStringProperty(newValue);
	}
	
	/**
	 * 
	 * @return The key for the language item.
	 */
	public String getKeyword() {
		return keyword.get();
	}
	
	/**
	 * 
	 * @param value The new key for the language item.
	 */
	public void setKeyword(String value) {
		keyword.set(value);
	}
	
	/**
	 * 
	 * @return The English value for the item.
	 */
	public String getEnValue() {
		return enValue.get();
	}
	
	/**
	 * 
	 * @param value The new English value for the item.
	 */
	public void setEnValue(String value) {
		enValue.set(value);
	}
	
	/**
	 * 
	 * @return The new value for the item (localized value)
	 */
	public String getNewValue() {
		return newValue.get();
	}
	
	/**
	 * 
	 * @param value A new value for the item (localized value).
	 */
	public void setNewValue(String value) {
		newValue.set(value);
	}
	
	/**
	 * 
	 * @return The new value for the item (localized value).
	 */
	public SimpleStringProperty newValueProperty() {
		return newValue;
	}
}
