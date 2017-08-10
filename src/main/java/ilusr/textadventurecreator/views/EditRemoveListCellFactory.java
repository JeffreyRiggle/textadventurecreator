package ilusr.textadventurecreator.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

//TODO: Move this to reusable code.
/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The data assoicated with the cell.
 */
public class EditRemoveListCellFactory<T> implements Callback<ListView<T>, ListCell<T>>{

	private final T addId;
	private final Node addTemplate;
	private final ilusr.core.interfaces.Callback<T> editAction;
	private final EventHandler<ActionEvent> addAction;
	private final IViewCreator<T> creator;
	private final ilusr.core.interfaces.Callback<T> removeAction;
	
	/**
	 * 
	 * @param addId The unique item to be used as an add button.
	 * @param editAction The action to run when an item is edited.
	 * @param addAction The event handler to call when the add button is pressed.
	 */
	public EditRemoveListCellFactory(T addId, ilusr.core.interfaces.Callback<T> editAction, EventHandler<ActionEvent> addAction) {
		this(addId, editAction, addAction, null);
	}
	
	/**
	 * 
	 * @param addId The unique item to be used as an add button.
	 * @param editAction The action to run when an item is edited.
	 * @param addAction The event handler to call when the add button is pressed.
	 * @param creator A view creator to create the node for the list item.
	 */
	public EditRemoveListCellFactory(T addId, 
									 ilusr.core.interfaces.Callback<T> editAction, 
									 EventHandler<ActionEvent> addAction, 
									 IViewCreator<T> creator) {
		this(addId, editAction, addAction, creator, null);
	}
	
	/**
	 * 
	 * @param addId The unique item to be used as an add button.
	 * @param editAction The action to run when an item is edited.
	 * @param addAction The event handler to call when the add button is pressed.
	 * @param creator A view creator to create the node for the list item.
	 * @param removeAction The action to run when an item is removed.
	 */
	public EditRemoveListCellFactory(T addId, 
									 ilusr.core.interfaces.Callback<T> editAction, 
									 EventHandler<ActionEvent> addAction, 
									 IViewCreator<T> creator, 
									 ilusr.core.interfaces.Callback<T> removeAction) {
		this.addId = addId;
		this.editAction = editAction;
		this.addAction = addAction;
		this.creator = creator;
		this.removeAction = removeAction;
		this.addTemplate = null;
	}
	
	/**
	 * 
	 * @param addId The unique item to be used as an add button.
	 * @param editAction The action to run when an item is edited.
	 * @param addTemplate The node to display as the add list item.
	 * @param creator A view creator to create the node for the list item.
	 * @param removeAction The action to run when an item is removed.
	 */
	public EditRemoveListCellFactory(T addId, 
			 ilusr.core.interfaces.Callback<T> editAction, 
			 Node addTemplate, 
			 IViewCreator<T> creator, 
			 ilusr.core.interfaces.Callback<T> removeAction) {
		this.addId = addId;
		this.editAction = editAction;
		this.addTemplate = addTemplate;
		this.creator = creator;
		this.removeAction = removeAction;
		this.addAction = null;
	}
	
	@Override
	public ListCell<T> call(ListView<T> param) {
		if (addTemplate != null) {
			return new EditRemoveListCell<T>(param, addId, addTemplate, creator, removeAction, editAction);
		}
		
		EditRemoveListCell<T> cell = new EditRemoveListCell<T>(param, addId, creator, removeAction);
		
		cell.setEditAction(editAction);
		cell.setAddAction(addAction);
		
		return cell;
	}

}
