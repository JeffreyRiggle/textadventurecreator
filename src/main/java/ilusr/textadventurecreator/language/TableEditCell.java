package ilusr.textadventurecreator.language;

import ilusr.core.data.Tuple;
import ilusr.core.interfaces.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The model to associate with this cell.
 */
public class TableEditCell<T> extends TableCell<T, String>{

	private TextField text;
	private Callback<Tuple<Tuple<Integer, String>, TableColumn<T, String>>> editAction;
	
	/**
	 * 
	 * @param editAction The callback for when the item is edited.
	 */
	public TableEditCell(Callback<Tuple<Tuple<Integer, String>, TableColumn<T, String>>> editAction) {
		this.editAction = editAction;
	}
	
	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
			text = null;
			return;
		}
		
		if (text != null && item.equals(text.getText())) {
			return;
		}
		
		text = new TextField();
		text.textProperty().set(getItem() == null ? "" : getItem().toString());
		text.textProperty().addListener((v, o, n) -> {
			editAction.execute(new Tuple<Tuple<Integer, String>, TableColumn<T, String>>(new Tuple<Integer, String>(super.getIndex(), n), super.tableColumnProperty().get()));
		});
		
		setText(null);
		setGraphic(text);
	}
	
	@Override
	public void updateIndex(int i) {
		cancelEdit();
        super.updateIndex(i);
	}
}
