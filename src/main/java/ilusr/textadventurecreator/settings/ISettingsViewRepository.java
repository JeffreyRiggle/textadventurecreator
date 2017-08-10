package ilusr.textadventurecreator.settings;

import java.util.Map;

import ilusr.textadventurecreator.shell.IInitialize;
import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ISettingsViewRepository extends IInitialize {
	/**
	 * 
	 * @param name The name of the setting view (this should be unique)
	 * @param view The node to display.
	 */
	void register(String name, Node view);
	/**
	 * 
	 * @param name The name of the view to remove.
	 */
	void unRegister(String name);
	/**
	 * 
	 * @return All registered settings views.
	 */
	Map<String, Node> getRegisteredViews();
}
