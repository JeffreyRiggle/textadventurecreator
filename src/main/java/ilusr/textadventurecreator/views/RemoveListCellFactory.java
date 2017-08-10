package ilusr.textadventurecreator.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of item in the list view.
 */
public class RemoveListCellFactory<T> implements Callback<ListView<T>, ListCell<T>>{

	private final T addId;
	private final EventHandler<ActionEvent> addAction;
	private final IViewCreator<T> creator;
	private final ilusr.core.interfaces.Callback<T> removeAction;
	private final Node addTemplate;
	
	/**
	 * 
	 * @param addId The unique id to be used as an add item.
	 * @param addAction A event handler to run when the add item is pressed.
	 */
	public RemoveListCellFactory(T addId, EventHandler<ActionEvent> addAction) {
		this(addId, addAction, null);
	}
	
	/**
	 * 
	 * @param addId The unique id to be used as an add item.
	 * @param addAction A event handler to run when the add item is pressed.
	 * @param creator A creator to use to create list item nodes.
	 */
	public RemoveListCellFactory(T addId, EventHandler<ActionEvent> addAction, IViewCreator<T> creator) {
		this(addId, addAction, creator, null);
	}
	
	/**
	 * 
	 * @param addId The unique id to be used as an add item.
	 * @param addAction A event handler to run when the add item is pressed.
	 * @param creator A creator to use to create list item nodes.
	 * @param removeAction An action to run when an item is removed.
	 */
	public RemoveListCellFactory(T addId, EventHandler<ActionEvent> addAction, IViewCreator<T> creator, ilusr.core.interfaces.Callback<T> removeAction) {
		this.addId = addId;
		this.addAction = addAction;
		this.creator = creator;
		this.removeAction = removeAction;
		this.addTemplate = null;
	}
	
	/**
	 * 
	 * @param addId The unique id to be used as an add item.
	 * @param addTemplate A node to be display as the add list item.
	 * @param creator A creator to use to create list item nodes.
	 * @param removeAction An action to run when an item is removed.
	 */
	public RemoveListCellFactory(T addId, Node addTemplate, IViewCreator<T> creator, ilusr.core.interfaces.Callback<T> removeAction) {
		this.addId = addId;
		this.creator = creator;
		this.removeAction = removeAction;
		this.addTemplate = addTemplate;
		this.addAction = null;
	}
	
	@Override
	public ListCell<T> call(ListView<T> param) {
		if (addTemplate != null) {
			return new RemoveListCell<T>(param, addId, addTemplate, creator, removeAction);
		}
		
		RemoveListCell<T> cell = new RemoveListCell<T>(param, addId, creator, removeAction);
		cell.setAddAction(addAction);
		
		return cell;
	}

}
