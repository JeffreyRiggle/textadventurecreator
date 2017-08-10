package ilusr.textadventurecreator.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of item to select from.
 */
public class SelectionAwareObservableList<T> {

	private SimpleObjectProperty<T> selected;
	private ObservableList<T> list;
	
	/**
	 * Creates an empty list with no selected item.
	 */
	public SelectionAwareObservableList() {
		this(FXCollections.observableArrayList());
	}
	
	/**
	 * 
	 * @param list A list of items to select from.
	 */
	public SelectionAwareObservableList(ObservableList<T> list) {
		this(list, null);
	}
	
	/**
	 * 
	 * @param list A list of items to select from.
	 * @param selected The currently selected item.
	 */
	public SelectionAwareObservableList(ObservableList<T> list, T selected) {
		this.list = list;
		this.selected = new SimpleObjectProperty<T>(selected);
	}
	
	/**
	 * 
	 * @return The list of items to select from.
	 */
	public ObservableList<T> list() {
		return list;
	}
	
	/**
	 * 
	 * @return The selected item.
	 */
	public SimpleObjectProperty<T> selected() {
		return selected;
	}
}
