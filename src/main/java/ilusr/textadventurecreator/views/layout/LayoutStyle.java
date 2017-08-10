package ilusr.textadventurecreator.views.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutStyle {

	private String displayName;
	private StyleSelectorPersistenceObject style;
	
	private Map<String, StylePropertyPersistenceObject> propertyMap;
	private List<LayoutStyle> children;
	
	/**
	 * 
	 * @param style A LayoutStyle to clone.
	 */
	public LayoutStyle(LayoutStyle style) {
		this.displayName = style.displayName();
		propertyMap = new HashMap<String, StylePropertyPersistenceObject>();
		children = new ArrayList<LayoutStyle>();
		
		try {
			this.style = new StyleSelectorPersistenceObject();
			this.style.setSelector(style.getPersistenceObject().getSelector());
			
			for (Entry<String, StylePropertyPersistenceObject> entry : style.getProperties().entrySet()) {
				StylePropertyPersistenceObject prop = new StylePropertyPersistenceObject(entry.getValue().getPropertyType(), entry.getValue().getPropertyValue());
				propertyMap.put(entry.getKey(), prop);
				this.style.addStyleProperty(prop);
			}
			
			for (LayoutStyle child : style.getChildren()) {
				children.add(new LayoutStyle(child));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param displayName The display name of the style.
	 * @param style A @see StyleSelectorPersistenceObject to use.
	 */
	public LayoutStyle(String displayName, StyleSelectorPersistenceObject style) {
		this.displayName = displayName;
		this.style = style;
		
		propertyMap = new HashMap<String, StylePropertyPersistenceObject>();
		children = new ArrayList<LayoutStyle>();
	}
	
	/**
	 * 
	 * @return The disply name for the style.
	 */
	public String displayName() {
		return displayName;
	}
	
	/**
	 * 
	 * @return The selector for the style.
	 */
	public String getSelector() {
		return style.getSelector();
	}
	
	/**
	 * 
	 * @param selector The new selector for the style.
	 */
	public void setSelector(String selector) {
		style.setSelector(selector);
	}
	
	/**
	 * 
	 * @param displayName The name of the property.
	 * @param value A @see StylePropertyPersistenceObject associated with the name.
	 */
	public void addProperty(String displayName, StylePropertyPersistenceObject value) {
		propertyMap.put(displayName, value);
		style.addStyleProperty(value);
	}
	
	/**
	 * 
	 * @param displayName The name of the property to remove.
	 */
	public void removeProperty(String displayName) {
		if (!propertyMap.containsKey(displayName)) {
			return;
		}
		
		style.removeStyleProperty(propertyMap.remove(displayName));
	}
	
	/**
	 * 
	 * @param displayName The name of the property to get.
	 * @return The associated @see StylePropertyPersistenceObject.
	 */
	public StylePropertyPersistenceObject getPropertyValue(String displayName) {
		if (!propertyMap.containsKey(displayName)) {
			return null;
		}
		
		return propertyMap.get(displayName);
	}
	
	/**
	 * 
	 * @param displayName The name of the property to update.
	 * @param value The new @see StylePropertyPersistenceObject to use.
	 */
	public void updateProperty(String displayName, StylePropertyPersistenceObject value) {
		propertyMap.put(displayName, value);
		style.addStyleProperty(value);
	}
	
	/**
	 * 
	 * @return All known properties for this style.
	 */
	public Map<String, StylePropertyPersistenceObject> getProperties() {
		return propertyMap;
	}
	
	/**
	 * 
	 * @param child A @see LayoutStyle to add to this style.
	 */
	public void addChild(LayoutStyle child) {
		children.add(child);
	}
	
	/**
	 * 
	 * @param child A @see LayoutStyle to remove to this style.
	 */
	public void removeChild(LayoutStyle child) {
		children.remove(child);
	}
	
	/**
	 * 
	 * @return All LayoutStyles associated with this style.
	 */
	public List<LayoutStyle> getChildren() {
		return children;
	}
	
	/**
	 * 
	 * @return The compiled style.
	 */
	public String compile() {
		return style.compile();
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public StyleSelectorPersistenceObject getPersistenceObject() {
		return style;
	}
}
