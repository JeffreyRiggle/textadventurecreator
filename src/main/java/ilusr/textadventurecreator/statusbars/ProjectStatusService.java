package ilusr.textadventurecreator.statusbars;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ilusr.logrunner.LogRunner;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProjectStatusService {

	private final Object statusLock;
	private List<StatusItem> statusItems;
	private SimpleObjectProperty<StatusItem> currentItem;
	private Timer timer;
	
	/**
	 * 
	 */
	public ProjectStatusService() {
		statusLock = new Object();
		statusItems = new ArrayList<StatusItem>();
		currentItem = new SimpleObjectProperty<StatusItem>();
	}
	
	/**
	 * 
	 * @param item The @see StatusItem to queue up for display and execution.
	 */
	public void addStatusItemToQueue(StatusItem item) {
		synchronized(statusLock) {
			LogRunner.logger().info(String.format("Adding %s to status queue", item.displayText().get()));
			statusItems.add(item);
			processItems();
		}
	}
	
	/**
	 * 
	 * @param item The @see StatusItem to remove from queue for display and execution.
	 */
	public void removeStatusItemFromQueue(StatusItem item) {
		synchronized(statusLock) {
			LogRunner.logger().info(String.format("Removing %s from status queue", item.displayText().get()));
			statusItems.remove(item);
			processItems();
		}
	}
	
	/**
	 * 
	 * @return The currently displayed and executing @see StatusItem.
	 */
	public SimpleObjectProperty<StatusItem> currentItem() {
		return currentItem;
	}
	
	private void processItems() {
		if (statusItems.size() == 0) {
			LogRunner.logger().info("No remaining items to process.");
			return;
		}
		
		if (timer != null) {
			cancelTimer();
		}
		
		if (currentItem.get() != null) {
			return;
		}
		
		LogRunner.logger().info(String.format("Setting current item to %s", statusItems.get(0).displayText().get()));
		currentItem.set(statusItems.get(0));
		currentItem.get().finished().addListener((v, o, n) -> {
			if (n) {
				removeAndProcess();
			}
		});
		
		LogRunner.logger().info(String.format("Running item %s", statusItems.get(0).displayText().get()));
		currentItem.get().run();
	}
	
	private void removeAndProcess() {
		synchronized(statusLock) {
			LogRunner.logger().info(String.format("Removing finished item %s", currentItem.get().displayText().get()));
			statusItems.remove(currentItem.get());
			
			if (statusItems.size() != 0 || currentItem.get().postFinishLiveTime().get() == 0) {
				currentItem.set(null);
				processItems();
			} else {
				setupTimer();
			}
		}
	}
	
	private void setupTimer() {
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timerFinished();
			}
		}, currentItem.get().postFinishLiveTime().get());
	}
	
	private void timerFinished() {
		currentItem.set(null);
		processItems();
	}
	
	private void cancelTimer() {
		timer.cancel();
		timer = null;
		currentItem.set(null);
	}
}
