package ilusr.textadventurecreator.views.layout;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.javafx.LocalDragboard;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.RegisteredServices;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import textadventurelib.persistence.LayoutNodePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutDesignArea extends AnchorPane implements Initializable {

	@FXML
	private GridPane root;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private SimpleIntegerProperty rows;
	private SimpleIntegerProperty columns;
	private LayoutCreatorModel model;
	private SelectionManager manager;
	private IDialogService dialogService;
	private IDialogProvider dialogProvider;
	private List<LayoutComponent> components;
	private LayoutComponentProvider componentProvider;
	
	public LayoutDesignArea() {
		rows = new SimpleIntegerProperty(1);
		columns = new SimpleIntegerProperty(1);
		components = new ArrayList<LayoutComponent>();
		//TODO IoC?
		dialogService = ServiceManager.getInstance().<IDialogService>get(RegisteredServices.DialogService);
		componentProvider = ServiceManager.getInstance().<LayoutComponentProvider>get("LayoutComponentProvider");
		styleService = ServiceManager.getInstance().<IStyleContainerService>get(RegisteredServices.StyleService);
		urlProvider = ServiceManager.getInstance().<InternalURLProvider>get("InternalURLProvider");
		dialogProvider = ServiceManager.getInstance().<IDialogProvider>get("IDialogProvider");
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutDesignArea.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.setOnDragEntered((e) -> {
			if (!LocalDragboard.getInstance().hasType(LayoutComponent.class)) {
				return;
			}
			
			e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			e.consume();
		});
		super.setOnDragOver((e) -> {
			if (!LocalDragboard.getInstance().hasType(LayoutComponent.class)) {
				return;
			}
			
			e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			e.consume();
		});
		
		super.setOnDragDropped((e) -> {
			if (!LocalDragboard.getInstance().hasType(LayoutComponent.class)) {
				return;
			}
			
			LayoutComponent comp = LocalDragboard.getInstance().getValue(LayoutComponent.class);
			AnchorPane pane = findPane(new Point2D(e.getScreenX(), e.getScreenY()));
			if (pane != null) {
				pane.getChildren().clear();
				pane.getChildren().add(createDroppedElement(comp, pane));
			}
		});
		
		setModel(model);
		super.getStylesheets().add(getClass().getResource("layoutdesign.css").toExternalForm());
		super.applyCss();
	}

	/**
	 * 
	 * @param model The new @see LayoutCreatorModel to use.
	 */
	public void setModel(LayoutCreatorModel model) {
		if (this.model != null) {
			tearDown();
		}
		
		this.model = model;
		
		if (this.model != null) {
			setup();
		}
	}
	
	private void tearDown() {
		rows.unbind();
		columns.unbind();
	}
	
	private void setup() {
		manager = model.getSelectionManager();
		rows.set(model.rows().get());
		columns.set(model.columns().get());
		
		setupGrid(model.rows().get(), model.columns().get());
		rows.bind(model.rows());
		columns.bind(model.columns());
		
		rows.addListener((v, o, n) -> {
			setupGrid(n.intValue(), columns.get());
		});
		columns.addListener((v, o, n) -> {
			setupGrid(rows.get(), n.intValue());
		});
		
		for (LayoutNodePersistenceObject node : model.layoutGrid().getNodes()) {
			List<AnchorPane> host = findChild(node.getRow(), node.getColumn());
			if (host.size() <= 0) {
				continue;
			}
				
			host.get(0).getChildren().add(createDroppedElement(componentProvider.getLayoutFromPersistence(node), host.get(0)));
		}
	}
	
	private void setupGrid(int rows, int cols) {
		root.getChildren().clear();
		root.getRowConstraints().clear();
		root.getColumnConstraints().clear();
		
		for (int i = 0; i < rows; i++) {
			root.getRowConstraints().add(createRow());
		}
		
		for (int i = 0; i < cols; i++) {
			root.getColumnConstraints().add(createColumn());
		}
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				root.add(new AnchorPane(), j, i);
			}
		}
	}
	
	private RowConstraints createRow() {
		RowConstraints retVal = new RowConstraints();
		retVal.setFillHeight(true);
		retVal.setPercentHeight(100 / model.rows().get());
		retVal.setVgrow(Priority.NEVER);
		return retVal;
	}
	
	private ColumnConstraints createColumn() {
		ColumnConstraints retVal = new ColumnConstraints();
		retVal.setFillWidth(true);
		retVal.setPercentWidth(100 / model.columns().get());
		retVal.setHgrow(Priority.NEVER);
		return retVal;
	}
	
	private LayoutComponentView createDroppedElement(LayoutComponent comp, AnchorPane parent) {
		final LayoutComponent component = new LayoutComponent(comp);
		component.setCol(GridPane.getColumnIndex(parent));
		component.setRow(GridPane.getRowIndex(parent));
		
		LayoutComponentView retVal = new LayoutComponentView(component, null, (StackPane)this.getParent(), manager, dialogService, dialogProvider, styleService, urlProvider);
		retVal.updateCloseAction(() -> {
			parent.getChildren().remove(retVal);
			components.remove(component);
			model.layoutGrid().removeNode(component.node());
			removeStyles(component);
		});
		
		retVal.setResizeAction(c -> {
			Point2D point = new Point2D(c.key(), c.value());
			AnchorPane dragLocation = findPane(point);
			
			if (dragLocation == null) {
				return;
			}
			
			int dragRow = GridPane.getRowIndex(dragLocation);
			int dragCol = GridPane.getColumnIndex(dragLocation);
			int originalRow = GridPane.getRowIndex(retVal.getParent());
			int originalCol = GridPane.getColumnIndex(retVal.getParent());
			int originalRowSpan = GridPane.getRowSpan(retVal.getParent()) == null ? 1 : GridPane.getRowSpan(retVal.getParent());
			int originalColSpan = GridPane.getColumnSpan(retVal.getParent()) == null ? 1 : GridPane.getColumnSpan(retVal.getParent());
			
			int rowDifference = dragRow - originalRow;
			if (rowDifference < 0) {
				if (!(originalRow < dragRow) || !(originalRow + originalRowSpan - 1  <= dragRow)) {
					rowDifference = Math.abs(rowDifference);
				}
			} else if (rowDifference == 0 && originalRowSpan > 1) {
				rowDifference = -1 * (originalRowSpan - 1);
			}
			
			int colDifference = dragCol - originalCol;
			if (colDifference < 0) {
				if (!(originalCol < dragCol) || !(originalCol + originalColSpan - 1  <= dragCol)) {
					colDifference = Math.abs(colDifference);
				}
			} else if (colDifference == 0 && originalColSpan > 1) {
				colDifference = -1 * (originalColSpan - 1);
			}
			
			int colSpan = originalColSpan + colDifference;
			int rowSpan = originalRowSpan + rowDifference;
			
			removeNodesInArea(Math.min(dragRow, originalRow), Math.max(dragRow, originalRow + originalRowSpan - 1),
					Math.min(dragCol, originalCol), Math.max(dragCol, originalCol));
			
			AnchorPane newParent = new AnchorPane();
			
			GridPane.setColumnIndex(newParent, Math.min(dragCol, originalCol));
			component.setCol(Math.min(dragCol, originalCol));
			GridPane.setRowIndex(newParent, Math.min(dragRow, originalRow));
			component.setRow(Math.min(dragRow, originalRow));
			
			GridPane.setColumnSpan(newParent, colSpan);
			component.setColSpan(colSpan);
			GridPane.setRowSpan(newParent, rowSpan);
			component.setRowSpan(rowSpan);
			root.getChildren().add(newParent);
			
			retVal.resize(newParent.getWidth(), newParent.getHeight());
			newParent.getChildren().add(retVal);
			
			retVal.updateCloseAction(() -> {
				newParent.getChildren().clear();
				root.getChildren().remove(newParent);
				components.remove(component);
				model.layoutGrid().removeNode(component.node());
				removeStyles(component);
			});
		});
		
		retVal.setRelocateAction(c -> {
			Point2D point = new Point2D(c.key(), c.value());
			AnchorPane dragLocation = findPane(point);
			
			if (dragLocation == null) {
				return;
			}
			
			int dragRow = GridPane.getRowIndex(dragLocation);
			int dragCol = GridPane.getColumnIndex(dragLocation);
			GridPane.setColumnIndex(retVal.getParent(), dragCol);
			component.setCol(dragCol);
			GridPane.setRowIndex(retVal.getParent(), dragRow);
			component.setRow(dragRow);
		});
		
		/*retVal.setUpdateAction(() -> {
			addStyles(component);
		});*/
		addStyles(component);
		components.add(component);
		model.layoutGrid().addNode(component.node());
		
		return retVal;
	}
	
	private void addStyles(LayoutComponent component) {
		model.style().addSelector(component.style().get().getPersistenceObject());
		
		for (LayoutStyle child : component.style().get().getChildren()) {
			model.style().addSelector(child.getPersistenceObject());
		}
		
		component.style().addListener((v, o, n) -> {
			model.style().removeSelector(o.getPersistenceObject());
			model.style().addSelector(n.getPersistenceObject());
		});
	}
	
	private void removeStyles(LayoutComponent component) {
		model.style().removeSelector(component.style().get().getPersistenceObject());
		
		for (LayoutStyle child : component.style().get().getChildren()) {
			model.style().removeSelector(child.getPersistenceObject());
		}
		
		component.style().unbind();
	}
	
	private AnchorPane findPane(Point2D point) {
		for (Node child : root.getChildren()) {
			if (!(child instanceof AnchorPane)) {
				continue;
			}
			
			AnchorPane pane = (AnchorPane)child;
			
			Bounds paneBounds = pane.getBoundsInLocal();
			paneBounds = pane.localToScreen(paneBounds);
			
			if (!paneBounds.contains(point)) {
				continue;
			}
			
			return pane;
		}
		
		return null;
	}
	
	private void removeNodesInArea(int minRow, int maxRow, int minCol, int maxCol) {
		List<AnchorPane> removeItems = new ArrayList<AnchorPane>();
		
		int currRow = maxRow;
		while (currRow >= minRow) {
			int currCol = maxCol;
			while (currCol >= minCol) {
				removeItems.addAll(findChild(currRow, currCol));
				currCol--;
			}
			currRow--;
		}
		
		for (AnchorPane pane : removeItems) {
			pane.getChildren().clear();
			
			int rowS = GridPane.getRowSpan(pane) == null ? 1 : GridPane.getRowSpan(pane);
			int colS = GridPane.getColumnSpan(pane) == null ? 1 : GridPane.getColumnSpan(pane);
			if (rowS > 1 || colS > 1) {
				root.getChildren().remove(pane);
			}
		}
	}
	
	private List<AnchorPane> findChild(int row, int col) {
		List<AnchorPane> retVal = new ArrayList<AnchorPane>();
		
		for (Node node : root.getChildren()) {
			if (!(node instanceof AnchorPane)) {
				continue;
			}
			
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				retVal.add((AnchorPane)node);
			}
		}
		
		return retVal;
	}
}
