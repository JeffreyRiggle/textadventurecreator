package ilusr.textadventurecreator.statusbars;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StatusItem implements Runnable {

	private SimpleStringProperty displayText;
	private SimpleDoubleProperty progressAmount;
	private SimpleBooleanProperty finished;
	private SimpleObjectProperty<Integer> postFinishLiveTime;
	private SimpleObjectProperty<StatusIndicator> indicator;
	private Runnable onStart;
	
	/**
	 * 
	 */
	public StatusItem() {
		this(new String());
	}
	
	/**
	 * 
	 * @param displayText The text to display in the status bar.
	 */
	public StatusItem(String displayText) {
		this(displayText, null);
	}
	
	/**
	 * 
	 * @param displayText The text to display in the status bar.
	 * @param start The @see Runnable to start when the item is shown.
	 */
	public StatusItem(String displayText, Runnable start) {
		this(displayText, start, 0);
	}
	
	/**
	 * 
	 * @param displayText The text to display in the status bar.
	 * @param start The @see Runnable to start when the item is shown.
	 * @param postFinishLiveTime The additional time to show this item after it has been finished.
	 */
	public StatusItem(String displayText, Runnable start, int postFinishLiveTime) {
		this.displayText = new SimpleStringProperty(displayText);
		progressAmount = new SimpleDoubleProperty();
		finished = new SimpleBooleanProperty();
		this.postFinishLiveTime = new SimpleObjectProperty<Integer>(postFinishLiveTime);
		indicator = new SimpleObjectProperty<StatusIndicator>(StatusIndicator.Normal);
		onStart = start;
	}
	
	/**
	 * 
	 * @return The text to display in the status bar.
	 */
	public SimpleStringProperty displayText() {
		return displayText;
	}
	
	/**
	 * 
	 * @return The progress to show in the status bar. This number should be between 0 and 1.0
	 */
	public SimpleDoubleProperty progressAmount() {
		return progressAmount;
	}
	
	/**
	 * 
	 * @return If this status item has finished.
	 */
	public SimpleBooleanProperty finished() {
		return finished;
	}

	/**
	 * 
	 * @return The amount of time to show this after the item has been finished.
	 */
	public SimpleObjectProperty<Integer> postFinishLiveTime() {
		return postFinishLiveTime;
	}
	
	/**
	 * 
	 * @return How to display this status item.
	 */
	public SimpleObjectProperty<StatusIndicator> indicator() {
		return indicator;
	}
	
	/**
	 * 
	 * @param start A new @see Runnable to run when this item is started.
	 */
	public void setOnStart(Runnable start) {
		onStart = start;
	}
	
	@Override
	public void run() {
		if (onStart == null) {
			finished.set(true);
			return;
		}
		
		onStart.run();
	}
}
