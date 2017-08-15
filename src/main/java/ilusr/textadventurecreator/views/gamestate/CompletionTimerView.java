package ilusr.textadventurecreator.views.gamestate;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionTimerView extends AnchorPane implements Initializable{

	@FXML
	private TextField completionData;
	
	@FXML
	private TextField duration;
	
	@FXML
	private Label stateTitle;
	
	@FXML
	private Label timeLabel;
	
	private CompletionTimerModel model;
	
	/**
	 * 
	 * @param model The @see CompletionTimerModel to bind to.
	 */
	public CompletionTimerView(CompletionTimerModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CompletionTimerView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		completionData.textProperty().bindBidirectional(model.completion());
		stateTitle.textProperty().bind(model.stateText());
		timeLabel.textProperty().bind(model.timeText());
		
		duration.textProperty().set(Long.toString(model.duration().get()));
		duration.textProperty().addListener((v, o, n) -> {
			try {
				model.duration().set(Long.parseLong(n));
			} catch (Exception e) { }
		});
	}
}
