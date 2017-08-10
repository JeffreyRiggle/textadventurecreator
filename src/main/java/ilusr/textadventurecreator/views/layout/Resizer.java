package ilusr.textadventurecreator.views.layout;

import ilusr.core.data.Tuple;
import ilusr.core.interfaces.Callback;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class Resizer {

	private static final int RESIZE_LIMIT = 10;
	
	private final Node node;
	private Region originalRegion;
	private final StackPane parent;
	private Rectangle region;
	
	private double yLocation;
	private double xLocation;
	private boolean canDragX;
	private boolean canDragY;
	private boolean dragging;
	private Cursor dragCursor;
	private Callback<Tuple<Double, Double>> callback;
	private Pane overlay;
	
	/**
	 * 
	 * @param node The @see Node to relocate.
	 * @param region The @see Region to node exists in.
	 * @param parent The parent of the node.
	 */
	public Resizer(Node node, Region region, StackPane parent) {
		this(node, region, parent, null);
	}
	
	/**
	 * 
	 * @param node The @see Node to relocate.
	 * @param region The @see Region to node exists in.
	 * @param parent The parent of the node.
	 * @param cursor The cursor being used.
	 */
	public Resizer(Node node, Region region, StackPane parent, Cursor cursor) {
		this(node, cursor, region, parent, null);
	}
	
	/**
	 * 
	 * @param node The @see Node to relocate.
	 * @param cursor The cursor being used.
	 * @param region The @see Region to node exists in.
	 * @param parent The parent of the node.
	 * @param callback A Callback to run when the node has been resized.
	 */
	public Resizer(Node node, Cursor cursor, Region region, StackPane parent, Callback<Tuple<Double, Double>> callback) {
		this.node = node;
		this.callback = callback;
		this.parent = parent;
		originalRegion = region;
		dragCursor = cursor;
		initialize();
	}
	
	/**
	 * 
	 * @param cursor The new Cursor.
	 */
	public void setDragCursor(Cursor cursor) {
		dragCursor = cursor;
	}
	
	/**
	 * 
	 * @param region The new region.
	 */
	public void updateRegion(Region region) {
		originalRegion = region;
	}
	
	private void initialize() {
		node.setOnMousePressed(e -> {
			mousePressed(e);
		});
		
		node.setOnMouseDragged(e -> {
			mouseDragged(e);
		});
		
		node.setOnMouseMoved(e -> {
			mouseOver(e);
		});
		
		node.setOnMouseReleased(e -> {
			mouseReleased();
			
			if (callback != null) {
				callback.execute(new Tuple<Double, Double>(e.getSceneX(), e.getSceneY()));
			}
		});
		
		overlay = new Pane();
	}
	
	private boolean inDraggingZone(MouseEvent event) {
		if (region == null) {
			canDragX = event.getX() > (node.getBoundsInParent().getHeight() - RESIZE_LIMIT);
			canDragY = event.getY() > (node.getBoundsInParent().getWidth() - RESIZE_LIMIT);
		} else {
			canDragX = event.getX() > (region.getWidth() - RESIZE_LIMIT);
			canDragY = event.getY() > (region.getHeight() - RESIZE_LIMIT);
		}
		
		return canDragX || canDragY;
	}
	
	private boolean isYDrag() {
		return dragCursor == Cursor.N_RESIZE || dragCursor == Cursor.NE_RESIZE 
				|| dragCursor == Cursor.NW_RESIZE || dragCursor == Cursor.S_RESIZE 
				|| dragCursor == Cursor.SE_RESIZE || dragCursor == Cursor.SW_RESIZE;
	}
	
	private boolean isXDrag() {
		return dragCursor == Cursor.E_RESIZE || dragCursor == Cursor.NE_RESIZE 
				|| dragCursor == Cursor.SE_RESIZE || dragCursor == Cursor.W_RESIZE 
				|| dragCursor == Cursor.NW_RESIZE || dragCursor == Cursor.SW_RESIZE;
	}
	
	private void mousePressed(MouseEvent event) {
		if (!inDraggingZone(event)) {
			return;
		}
		
		dragging = true;
		
		if (region == null) {
			Bounds paneBounds = originalRegion.getBoundsInParent();
			Rectangle box = new Rectangle(paneBounds.getMinX(), paneBounds.getMinY(), paneBounds.getWidth(), paneBounds.getHeight());
			box.setFill(Color.TRANSPARENT);
			box.setStroke(Color.RED);
			region = box;
			parent.getChildren().add(overlay);
			overlay.getChildren().add(box);
		}
		
		xLocation = event.getX();
	}
	
	private void mouseDragged(MouseEvent event) {
		if (!dragging) {
			return;
		}
		
		Bounds originalBounds = originalRegion.getBoundsInLocal();
		originalBounds = originalRegion.localToScreen(originalBounds);
		
		if (canDragY && isYDrag()) {
			dragY(event, originalBounds);
		}
		
		if (canDragX && isXDrag()) {
			dragX(event, originalBounds);
		}
	}
	
	private void dragY(MouseEvent event, Bounds originalBounds) {
		double y = event.getY();
		
		if (originalBounds.contains(originalBounds.getMinX(), event.getScreenY())) {
			yLocation = y;
			return;
		}
		
		double diff = y - yLocation;
		System.out.printf("Initial diff %s\n", diff);
		
		if (event.getScreenY() < originalBounds.getMinY()) {
			diff = yLocation - y;
			region.setLayoutY(region.getLayoutY() - diff);
		}
		
		double height = region.getHeight() + diff;
		region.setHeight(height);
		yLocation = y;
	}
	
	private void dragX(MouseEvent event, Bounds originalBounds) {
		double x = event.getX();
		
		if (originalBounds.contains(event.getScreenX(), originalBounds.getMinY())) {
			xLocation = x;
			return;
		}
		
		double diff = x - xLocation;
		
		if (event.getScreenX() < originalBounds.getMinX()) {
			diff = xLocation - x;
			region.setLayoutX(region.getLayoutX() - diff);
		}
		
		double width = region.getWidth() + diff;
		region.setWidth(width);
		xLocation = x;
	}
	
	private void mouseOver(MouseEvent event) {
		if (!inDraggingZone(event) && !dragging) {
			node.setCursor(Cursor.DEFAULT);
			return;
		}
		
		if (dragCursor != null) {
			node.setCursor(dragCursor);
			return;
		}
		
		if (canDragY) {
			node.setCursor(Cursor.S_RESIZE);
		} else if (canDragX) {
			node.setCursor(Cursor.E_RESIZE);
		}
	}
	
	private void mouseReleased() {
		dragging = false;
		parent.getChildren().remove(overlay);
		overlay.getChildren().clear();
		node.setCursor(Cursor.DEFAULT);
		region = null;
	}
}
