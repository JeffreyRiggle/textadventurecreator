package ilusr.textadventurecreator.search;

import ilusr.core.interfaces.Callback;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object to display.
 */
public class SearchListCell<T> extends ListCell<T> {

	private IViewCreator<T> creator;
	private Button searchBtn;
	
	/**
	 * 
	 * @param creator A @see IViewCreator to use to create display nodes.
	 * @param inspectAction A @see Callback to run when this cell is inspected.
	 */
	public SearchListCell(IViewCreator<T> creator, Callback<T> inspectAction) {
		this.creator = creator;
		
		searchBtn = new Button();
		searchBtn.getStylesheets().add(getClass().getResource("InspectButton.css").toExternalForm());
		searchBtn.setOnAction((e) -> {
			if (inspectAction != null) {
				inspectAction.execute(super.getItem());;
			}
		});
	}
	
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty || item == null) {
			setText("");
			setGraphic(null);
			return;
		}
		
		setStandardGraphic(item);
	}
	
	private void setStandardGraphic(T item) {
		Node label = createLabel(item);

		AnchorPane root = new AnchorPane();
		GridPane grid = new GridPane();
		AnchorPane.setBottomAnchor(grid, 0.0);
		AnchorPane.setTopAnchor(grid, 0.0);
		AnchorPane.setLeftAnchor(grid, 0.0);
		AnchorPane.setRightAnchor(grid, 0.0);
		grid.getColumnConstraints().add(new ColumnConstraints(50, 50, Integer.MAX_VALUE, Priority.ALWAYS, HPos.LEFT, true));
		grid.getColumnConstraints().add(new ColumnConstraints(30, 50, Integer.MAX_VALUE, Priority.NEVER, HPos.CENTER, true));
		grid.add(label, 0, 0);
		grid.add(searchBtn, 1, 0);
		root.getChildren().add(grid);
		
		setGraphic(root);
	}
	
	private Node createLabel(T item) {
		
		if (creator != null) {
			return creator.create(item);
		}
		
		Label retVal = new Label();
		
		if (item instanceof String) {
			retVal.setText((String)item);
			return retVal;
		}

		retVal.setText(item.toString());
		return retVal;
	}
}
