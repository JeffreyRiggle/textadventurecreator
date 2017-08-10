package ilusr.textadventurecreator.views;

import ilusr.iroshell.core.IViewProvider;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object that will be modified in this page.
 */
public class WizardPage<T> implements IViewProvider<Node> {

	private SimpleObjectProperty<T> result;
	private SimpleObjectProperty<Node> content;
	private SimpleBooleanProperty canFinish;
	private SimpleBooleanProperty valid;
	
	/**
	 * Creates a wizard page with no content.
	 */
	public WizardPage() {
		this(null, false);
	}
	
	/**
	 * 
	 * @param content The content to display on this page.
	 * @param canFinish If this page can result in a finish on the wizard.
	 */
	public WizardPage(Node content, boolean canFinish) {
		this.content = new SimpleObjectProperty<Node>(content);
		this.canFinish = new SimpleBooleanProperty(canFinish);
		this.valid = new SimpleBooleanProperty(true);
		result = new SimpleObjectProperty<T>();
	}
	
	/**
	 * 
	 * @return The content to display on this page.
	 */
	public SimpleObjectProperty<Node> content() {
		return content;
	}
	
	/**
	 * 
	 * @return If this page can result in a finish on the wizard.
	 */
	public SimpleBooleanProperty canFinish() {
		return canFinish;
	}

	/**
	 * 
	 * @return The item this page is modifying.
	 */
	public SimpleObjectProperty<T> result() {
		return result;
	}
	
	/**
	 * 
	 * @return If the page is valid and the next page can be selected.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	@Override
	public Node getView() {
		return content.get();
	}
}
