package ilusr.textadventurecreator.views;

import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type needed to create the view.
 */
public interface IViewCreator<T> {
	/**
	 * 
	 * @param input The data to use to create the view.
	 * @return A @see Node to display.
	 */
	Node create(T input);
}
