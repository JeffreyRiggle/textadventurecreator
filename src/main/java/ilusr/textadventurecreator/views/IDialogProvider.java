package ilusr.textadventurecreator.views;

import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IDialogProvider {
	/**
	 * 
	 * @param node A see node to display in the dialog.
	 * @return The created dialog.
	 */
	Dialog create(Node node);
	/**
	 * 
	 * @param node A see node to display in the dialog.
	 * @param completionAction an action to run on the ok press.
	 * @return The created dialog.
	 */
	Dialog create(Node node, Runnable completionAction);
	/**
	 * 
	 * @param node A see node to display in the dialog.
	 * @param completionAction An action to run on the ok press.
	 * @param cancelAction An action to run on the cancel press.
	 * @return The created dialog.
	 */
	Dialog create(Node node, Runnable completionAction, Runnable cancelAction);
}
