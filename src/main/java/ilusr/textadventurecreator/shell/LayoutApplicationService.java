package ilusr.textadventurecreator.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutApplicationService {

	private List<Runnable> listeners;
	
	/**
	 * Creates the layout application service.
	 */
	public LayoutApplicationService() {
		listeners = new ArrayList<Runnable>();
	}
	
	/**
	 * 
	 * @param listener A @see Runnable to execute when the layout is applied.
	 */
	public void addListener(Runnable listener) {
		listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener A @see Runnable to execute when the layout is applied.
	 */
	public void removeListener(Runnable listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Notifies listeners of an applied layout.
	 */
	public void applyLayout() {
		LogRunner.logger().log(Level.INFO, "Layout has been applied notifying listeners");
		notifyListeners();
	}
	
	private void notifyListeners() {
		for (Runnable listener : listeners) {
			listener.run();
		}
	}
}
