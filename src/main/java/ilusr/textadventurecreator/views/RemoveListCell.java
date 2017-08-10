package ilusr.textadventurecreator.views;

import ilusr.core.interfaces.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of list cell data.
 */
public class RemoveListCell<T> extends ListCell<T> {

	private final IViewCreator<T> provider;
	private Button remove;
	private Button add;
	private Node addTemplate;
	private ListView<T> view;
	private T addId;
	private Callback<T> removeAction;
	
	/**
	 * 
	 * @param view A list view to add list view items to.
	 * @param addId The unique item to be used as an add item.
	 */
	public RemoveListCell(ListView<T> view, T addId) {
		this(view, addId, null);
	}
	
	/**
	 * 
	 * @param view A list view to add list view items to.
	 * @param addId The unique item to be used as an add item.
	 * @param provider A view creator to be used to create nodes in list items.
	 */
	public RemoveListCell(ListView<T> view, T addId, IViewCreator<T> provider) {
		this(view, addId, provider, null);
	}
	
	/**
	 * 
	 * @param view A list view to add list view items to.
	 * @param addId The unique item to be used as an add item.
	 * @param provider A view creator to be used to create nodes in list items.
	 * @param removeAction A callback to run when a list item is removed.
	 */
	public RemoveListCell(ListView<T> view, T addId, IViewCreator<T> provider, Callback<T> removeAction) {
		super();
		this.provider = provider;
		this.view = view;
		this.addId = addId;
		this.removeAction = removeAction;
		
		remove = new Button();
		remove.getStylesheets().add(getClass().getResource("RemoveButton.css").toExternalForm());
		add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
	}
	
	/**
	 * 
	 * @param view A list view to add list view items to.
	 * @param addId The unique item to be used as an add item.
	 * @param addTemplate A node to be used as the add list item.
	 * @param provider A view creator to be used to create nodes in list items.
	 * @param removeAction A callback to run when a list item is removed.
	 */
	public RemoveListCell(ListView<T> view, T addId, Node addTemplate, IViewCreator<T> provider, Callback<T> removeAction) {
		super();
		this.provider = provider;
		this.view = view;
		this.addId = addId;
		this.removeAction = removeAction;
		
		remove = new Button();
		remove.getStylesheets().add(getClass().getResource("RemoveButton.css").toExternalForm());
		this.addTemplate = addTemplate;
	}
	
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty || item == null) {
			setText("");
			setGraphic(null);
			return;
		}
		
		if (item == addId) {
			setGraphic();
			return;
		}
		
		setStandardGraphic(item);
	}
	
	private void setGraphic() {
		if (add != null) {
			setGraphic(add);
		} else if (addTemplate != null) {
			setGraphic(addTemplate);
		}
	}
	
	private void setStandardGraphic(T item) {
		Node label = createLabel(item);
		
		remove.setOnAction((e) -> {
			view.getItems().remove(item);
			
			if (removeAction != null) {
				removeAction.execute(item);
			}
		});
		
		HBox layout = new HBox(5);
		layout.getChildren().add(remove);
		layout.getChildren().add(label);
		setGraphic(layout);
	}
	
	private Node createLabel(T item) {
		
		if (provider != null) {
			return provider.create(item);
		}
		
		Label retVal = new Label();
		
		if (item instanceof String) {
			retVal.setText((String)item);
			return retVal;
		}

		retVal.setText(item.toString());
		return retVal;
	}
	
	/**
	 * 
	 * @param value The event handler to run when the add item is pressed.
	 */
	public void setAddAction(EventHandler<ActionEvent> value) {
		add.setOnAction(value);
	}
}
