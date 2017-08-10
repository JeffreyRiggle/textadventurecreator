package ilusr.textadventurecreator.toolbars;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureToolBar extends ToolBar {

	/**
	 * 
	 * @param commands A list of @see Node to display.
	 */
	public TextAdventureToolBar(List<Node> commands) {
		super();
		super.getItems().addAll(commands);
	}
}
