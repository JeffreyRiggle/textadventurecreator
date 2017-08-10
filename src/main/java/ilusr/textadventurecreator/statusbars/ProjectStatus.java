package ilusr.textadventurecreator.statusbars;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProjectStatus extends AnchorPane implements Initializable {

	@FXML
	private Label statusText;
	
	@FXML
	private ProgressBar progress;
	
	private ProjectStatusModel model;
	
	private String lastClass;
	
	/**
	 * 
	 * @param model A @see ProjectStatusModel to bind to.
	 */
	public ProjectStatus(ProjectStatusModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectStatus.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.getStylesheets().add(getClass().getResource("ProjectStatus.css").toExternalForm());
		toggleVisibility(false);
		
		statusText.textProperty().bind(model.statusText());
		progress.progressProperty().bind(model.progressAmount());
		
		model.statusText().addListener((v, o, n) -> {
			if (n == null) {
				toggleVisibility(false);
			} else {
				toggleVisibility(true);
			}
		});
		
		updateIndicator(model.indicator().get());
		
		model.indicator().addListener((v, o, n) -> {
			updateIndicator(n);
		});
	}
	
	private void toggleVisibility(boolean visible) {
		statusText.visibleProperty().set(visible);
		progress.visibleProperty().set(visible);
	}
	
	private void updateIndicator(StatusIndicator indicator) {
		if (indicator == null) {
			return;
		}
		
		String styleClass = null;
		
		switch (indicator) {
			case Error:
				styleClass = "error";
				break;
			case Good:
				styleClass = "good";
				break;
			case Normal:
				styleClass = "normal";
				break;
			case Warning:
				styleClass = "warning";
				break;
		}
		
		if (styleClass == null) {
			return;
		}
		
		if (lastClass != null) {
			progress.getStyleClass().remove(lastClass);
		}
		
		progress.getStyleClass().add(styleClass);
		lastClass = styleClass;
	}
}
