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

//TODO: Move this to reusable code.
/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The data associated with the cell.
 */
public class EditRemoveListCell<T> extends ListCell<T> {

	private final IViewCreator<T> provider;
	private Button edit;
	private Button remove;
	private Button add;
	private Node addTemplate;
	private ListView<T> view;
	private T addId;
	private Callback<T> removeAction;
	private Callback<T> editAction;
	
	/**
	 * 
	 * @param view The @see ListView to apply this to.
	 * @param addId A unique id for the add button.
	 */
	public EditRemoveListCell(ListView<T> view, T addId) {
		this(view, addId, null);
	}
	
	/**
	 * 
	 * @param view The @see ListView to apply this to.
	 * @param addId A unique id for the add button.
	 * @param provider A @see IViewCreator to provide the cells node.
	 */
	public EditRemoveListCell(ListView<T> view, T addId, IViewCreator<T> provider) {
		this(view, addId, provider, null);
	}
	
	/**
	 * 
	 * @param view The @see ListView to apply this to.
	 * @param addId A unique id for the add button.
	 * @param provider A @see IViewCreator to provide the cells node.
	 * @param removeAction A @see Callback to run when an item is removed.
	 */
	public EditRemoveListCell(ListView<T> view, T addId, IViewCreator<T> provider, Callback<T> removeAction) {
		super();
		this.provider = provider;
		this.view = view;
		this.addId = addId;
		this.removeAction = removeAction;
		
		edit = new Button();
		edit.getStylesheets().add(getClass().getResource("EditButton.css").toExternalForm());
		remove = new Button();
		remove.getStylesheets().add(getClass().getResource("RemoveButton.css").toExternalForm());
		add = new Button();
		add.getStylesheets().add(getClass().getResource("AddButton.css").toExternalForm());
		add.setId("listCellAdd");
	}
	
	/**
	 * 
	 * @param view The @see ListView to apply this to.
	 * @param addId A unique id for the add button.
	 * @param provider A @see IViewCreator to provide the cells node.
	 * @param removeAction A @see Callback to run when an item is removed.
	 * @param editAction A @see Callback to run when an item is edited.
	 */
	public EditRemoveListCell(ListView<T> view, 
							  T addId, 
							  Node addTemplate,
							  IViewCreator<T> provider, 
							  Callback<T> removeAction,
							  Callback<T> editAction) {
		super();
		this.provider = provider;
		this.view = view;
		this.addId = addId;
		this.removeAction = removeAction;
		this.editAction = editAction;
		
		edit = new Button();
		edit.getStylesheets().add(getClass().getResource("EditButton.css").toExternalForm());
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
			setAddGraphic();
			return;
		}
		
		setStandardGraphic(item);
	}
	
	private void setAddGraphic() {
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
		
		edit.setOnAction((e) -> {
			if (editAction != null) {
				editAction.execute(item);
			}
		});
		
		HBox layout = new HBox(5);
		layout.getChildren().add(edit);
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
	 * @param value A @see Callback to run when an item is edited.
	 */
	public void setEditAction(Callback<T> value) {
		editAction = value;
	}
	
	/**
	 * 
	 * @param value A @see EventHandler to use when the add button is pressed.
	 */
	public void setAddAction(EventHandler<ActionEvent> value) {
		add.setOnAction(value);
	}
}
