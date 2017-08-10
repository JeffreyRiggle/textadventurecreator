package ilusr.textadventurecreator.views.layout;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ToolKitModel {

	private final LayoutComponentProvider provider;
	
	private ObservableList<LayoutComponent> components;
	
	/**
	 * 
	 * @param provider A @see LayoutComponentProvider to provide components.
	 */
	public ToolKitModel(LayoutComponentProvider provider) {
		this.provider = provider;
		components = FXCollections.observableArrayList();
		
		initialize();
	}
	
	private void initialize() {
		LogRunner.logger().log(Level.INFO, "Adding components to toolkit.");
		components.addAll(provider.getComponents());
	}
	
	/**
	 * 
	 * @return The components to use.
	 */
	public ObservableList<LayoutComponent> components() {
		return components;
	}
}
