package ilusr.textadventurecreator.views.layout;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import textadventurelib.persistence.LayoutNodePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutComponent {

	private String name;
	private Image icon;
	private LayoutNodePersistenceObject node;
	private Node displayNode;
	private SimpleObjectProperty<LayoutStyle> style;
	
	/**
	 * 
	 * @param component A LayoutCompontent to clone.
	 */
	public LayoutComponent(LayoutComponent component) {
		this(component.getName(), component.node().clone(), component.getIcon(), component.getDisplayNode(), new LayoutStyle(component.style().get()));
	}
	
	/**
	 * 
	 * @param name The name of the layout component.
	 * @param node A @see LayoutNodePersistenceObject to use.
	 * @param style A @see LayoutStyle to use.
	 */
	public LayoutComponent(String name, LayoutNodePersistenceObject node, LayoutStyle style) {
		this(name, node, null, style);
	}
	
	/**
	 * 
	 * @param name The name of the layout component.
	 * @param node A @see LayoutNodePersistenceObject to use.
	 * @param icon A @see Image to display for the component.
	 * @param style A @see LayoutStyle to use.
	 */
	public LayoutComponent(String name, LayoutNodePersistenceObject node, Image icon, LayoutStyle style) {
		this(name, node, icon, null, style);
	}
	
	/**
	 * 
	 * @param name The name of the layout component.
	 * @param node A @see LayoutNodePersistenceObject to use.
	 * @param icon A @see Image to display for the component.
	 * @param displayNode A @see Node to display.
	 * @param style A @see LayoutStyle to use.
	 */
	public LayoutComponent(String name, LayoutNodePersistenceObject node, Image icon, Node displayNode, LayoutStyle style) {
		this.name = name;
		this.node = node;
		this.icon = icon;
		this.displayNode = displayNode;
		this.style = new SimpleObjectProperty<LayoutStyle>(style);
		style.setSelector("#" + node.getId() + " *");
	}
	
	/**
	 * 
	 * @param name The new name for the component.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return The name of the component.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param icon The new image for the component.
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	/**
	 * 
	 * @return The image for the component.
	 */
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * 
	 * @param row The new row the component exists on.
	 */
	public void setRow(int row) {
		node.setRow(row);
	}
	
	/**
	 * 
	 * @param col The new column the component exists on.
	 */
	public void setCol(int col) {
		node.setColumn(col);
	}
	
	/**
	 * 
	 * @param rowSpan The number of rows the component spans.
	 */
	public void setRowSpan(int rowSpan) {
		node.setRowSpan(rowSpan);
	}
	
	/**
	 * 
	 * @param colSpan The number of columns the component spans.
	 */
	public void setColSpan(int colSpan) {
		node.setColumnSpan(colSpan);
	}
	
	/**
	 * 
	 * @return The fxml value of this component.
	 */
	public String fxmlValue() {
		return node.getLayoutValue();
	}
	
	/**
	 * 
	 * @return The compiled component. This would include row, column, colspan and rowspan.
	 */
	public String compile() {
		return node.compile();
	}
	
	/**
	 * 
	 * @param node The new node to display.
	 */
	public void setDisplayNode(Node node) {
		displayNode = node;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public LayoutNodePersistenceObject node() {
		return node;
	}
	
	/**
	 * 
	 * @return The node to display.
	 */
	public Node getDisplayNode() {
		return displayNode;
	}
	
	/**
	 * 
	 * @return The style for this layout component.
	 */
	public SimpleObjectProperty<LayoutStyle> style() {
		return style;
	}
}
