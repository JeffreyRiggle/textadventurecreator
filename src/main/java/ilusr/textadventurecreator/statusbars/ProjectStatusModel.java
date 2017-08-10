package ilusr.textadventurecreator.statusbars;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProjectStatusModel {

	private final ProjectStatusService statusService;
	private SimpleStringProperty statusText;
	private SimpleDoubleProperty progressAmount;
	private SimpleObjectProperty<StatusIndicator> indicator;
	
	/**
	 * 
	 * @param statusService A @see ProjectStatusService to provide status items.
	 */
	public ProjectStatusModel(ProjectStatusService statusService) {
		this.statusService = statusService;
		statusText = new SimpleStringProperty();
		progressAmount = new SimpleDoubleProperty();
		indicator = new SimpleObjectProperty<StatusIndicator>();
		initialize();
	}
	
	private void initialize() {
		statusService.currentItem().addListener((v, o, n) -> {
			Platform.runLater(() -> {
				if (n != null) {
					setupBindings(n);
					return;
				}
				
				tearDownBindings();
			});
		});
	}
	
	private void setupBindings(StatusItem item) {
		LogRunner.logger().log(Level.INFO, "Setting up bindings for status item.");
		statusText.set(item.displayText().get());
		item.displayText().addListener((v, o, n) -> {
			Platform.runLater(() -> {
				statusText.set(n);
			});
		});
		
		progressAmount.set(item.progressAmount().get());
		item.progressAmount().addListener((v, o, n) -> {
			Platform.runLater(() -> {
				progressAmount.set((Double)n);
			});
		});
		
		indicator.set(item.indicator().get());
		item.indicator().addListener((v, o, n) -> {
			Platform.runLater(() -> {
				indicator.set(n);
			});
		});
	}
	
	private void tearDownBindings() {
		LogRunner.logger().log(Level.INFO, "Tearing down bindings for status item.");
		statusText.unbind();
		progressAmount.unbind();
		statusText.set(null);
		progressAmount.set(0.0);
	}
	
	/**
	 * 
	 * @return The text to display in the status bar.
	 */
	public SimpleStringProperty statusText() {
		return statusText;
	}
	
	/**
	 * 
	 * @return The amount of progress to show for the status item. This number should be between 0 and 1.0
	 */
	public SimpleDoubleProperty progressAmount() {
		return progressAmount;
	}
	
	/**
	 * 
	 * @return The type of indicator to display.
	 */
	public SimpleObjectProperty<StatusIndicator> indicator() {
		return indicator;
	}
}
