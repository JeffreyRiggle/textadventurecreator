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
public class NodeRelocator {

	private static final int RELOCATE_LIMIT = 10;
	
	private final Node node;
	private Region originalRegion;
	private final StackPane parent;
	private Rectangle region;
	
	private boolean canDragX;
	private boolean canDragY;
	private boolean dragging;
	private Callback<Tuple<Double, Double>> callback;
	private Pane test;
	
	/**
	 * 
	 * @param node The @see Node to relocate.
	 * @param region The @see Region to node exists in.
	 * @param parent The parent of the node.
	 */
	public NodeRelocator(Node node, Region region, StackPane parent) {
		this(node, region, parent, null);
	}
	
	/**
	 * 
	 * @param node The @see Node to relocate.
	 * @param region The @see Region to node exists in.
	 * @param parent The parent of the node.
	 * @param callback A Callback to run when the node has been moved.
	 */
	public NodeRelocator(Node node, Region region, StackPane parent, Callback<Tuple<Double, Double>> callback) {
		this.node = node;
		this.callback = callback;
		this.parent = parent;
		originalRegion = region;
		initialize();
	}
	
	/**
	 * 
	 * @param region The new region for the node.
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
	}
	
	private boolean inDraggingZone(MouseEvent event) {
		if (region == null) {
			canDragX = event.getX() > (node.getBoundsInParent().getHeight() - RELOCATE_LIMIT);
			canDragY = event.getY() > (node.getBoundsInParent().getWidth() - RELOCATE_LIMIT);
		} else {
			canDragX = event.getX() > (region.getWidth() - RELOCATE_LIMIT);
			canDragY = event.getY() > (region.getHeight() - RELOCATE_LIMIT);
		}
		
		return canDragX || canDragY;
	}
	
	private void mousePressed(MouseEvent event) {
		if (!inDraggingZone(event)) {
			return;
		}
		
		dragging = true;
		node.setCursor(Cursor.CLOSED_HAND);
		
		if (region == null) {
			final Bounds paneBounds = originalRegion.getBoundsInParent();
			Rectangle box = new Rectangle(paneBounds.getMinX(), paneBounds.getMinY(), paneBounds.getWidth(), paneBounds.getHeight());
			box.setFill(Color.TRANSPARENT);
			box.setStroke(Color.RED);
			region = box;
			test = new Pane();
			parent.getChildren().add(test);
			test.getChildren().add(box);
		}
	}
	
	private void mouseDragged(MouseEvent event) {
		if (!dragging) {
			return;
		}
		
		if (canDragY) {
			double y = event.getY();
			region.setLayoutY(y);
		}
		
		if (canDragX) {
			double x = event.getX();
			region.setLayoutX(x);
		}
	}
	
	private void mouseOver(MouseEvent event) {
		if (!inDraggingZone(event) && !dragging) {
			node.setCursor(Cursor.DEFAULT);
			return;
		}
		
		node.setCursor(Cursor.OPEN_HAND);
	}
	
	private void mouseReleased() {
		dragging = false;
		parent.getChildren().remove(test);
		region = null;
		node.setCursor(Cursor.DEFAULT);
	}
}
