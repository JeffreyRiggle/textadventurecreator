package ilusr.textadventurecreator.error;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleArea;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.iroshell.services.StyleContainerService;
import ilusr.logrunner.LogRunner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ErrorView extends GridPane implements Initializable, IStyleWatcher{

	private final String MORE_INFO = "More info";
	private final String LESS_INFO = "Less info";
	
	private final InternalURLProvider urlProvider;
	private final StyleContainerService styleService;
	private StyleUpdater styleUpdater;
	
	@FXML
	private ImageView errorImage;
	
	@FXML
	private Label info;
	
	@FXML
	private Label errorLabel;
	
	@FXML
	private Button continueBtn;
	
	@FXML
	private Button reportBtn;
	
	@FXML
	private Button exitBtn;
	
	@FXML
	private TextArea infoArea;
	
	@FXML
	private GridPane infoPane;
	
	@FXML
	private ErrorModel model;

	/**
	 * 
	 * @param model The @see ErrorDialogModel bind this view to.
	 */
	public ErrorView(ErrorModel model) {
		this.model = model;
		urlProvider = ServiceManager.getInstance().<InternalURLProvider>get("InternalURLProvider");
		styleService = ServiceManager.getInstance().<StyleContainerService>get("IStyleContainerService");

		FXMLLoader errorLoader = new FXMLLoader(getClass().getResource("ErrorView.fxml"));
		errorLoader.setRoot(this);
		errorLoader.setController(this);
		
		try {
			errorLoader.load();
		} catch (Exception exception) {
			LogRunner.logger().severe(exception);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		infoArea.textProperty().bind(model.exceptionProperty());
		collapseStack();
		infoArea.visibleProperty().set(false);
		
		errorLabel.textProperty().bind(model.errorText());
		continueBtn.textProperty().bind(model.continueText());
		reportBtn.textProperty().bind(model.reportText());
		exitBtn.textProperty().bind(model.exitText());
		
		try {
			errorImage.setImage(new Image(getClass().getResource("newerror.png").toURI().toASCIIString()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		info.setOnMouseClicked((e) -> {
			if (infoArea.visibleProperty().get()) {
				collapseStack();
				infoArea.visibleProperty().set(false);
				info.setText(MORE_INFO);
				return;
			}
			
			expandStack();
			infoArea.visibleProperty().set(true);
			info.setText(LESS_INFO);
		});
		
		styleUpdater = new StyleUpdater(urlProvider, "errorstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyleArea.ERROR), this, false);
	}
	
	@FXML 
	protected void continueClicked(ActionEvent event) {
        Stage s = (Stage)super.getScene().getWindow();
        s.close();
    }
	
	@FXML
	private void exitClicked(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	private void reportClicked(ActionEvent event) {
		model.sendReport(() -> {
			Platform.runLater(() -> {;
				Stage s = (Stage)super.getScene().getWindow();
				s.close();
			});
		});
	}
	
	private void collapseStack() {
		infoPane.getRowConstraints().get(1).setMinHeight(0);
		infoPane.getRowConstraints().get(1).setPrefHeight(0);
		infoPane.getRowConstraints().get(1).setMaxHeight(0);
	}
	
	private void expandStack() {
		infoPane.getRowConstraints().get(1).setMinHeight(0);
		infoPane.getRowConstraints().get(1).setPrefHeight(100);
		infoPane.getRowConstraints().get(1).setMaxHeight(1000);
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
