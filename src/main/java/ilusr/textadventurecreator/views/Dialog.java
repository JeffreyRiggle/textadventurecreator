package ilusr.textadventurecreator.views;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class Dialog extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private AnchorPane pane;
	
	@FXML
	private Button ok;
	
	@FXML
	private Button close;

	private final Node content;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	private SimpleBooleanProperty valid;
	private Runnable cancel;
	private Runnable complete;
	
	/**
	 * 
	 * @param content The @see Node to display in the dialog
	 */
	public Dialog(Node content) {
		this(content, null);
	}
	
	/**
	 * 
	 * @param content The @see Node to display in the dialog
	 * @param languageService A @see LanguageService for display strings.
	 */
	public Dialog(Node content, ILanguageService languageService) {
		this(content, languageService, null, null);
	}
	
	/**
	 * 
	 * @param content The @see Node to display in the dialog
	 * @param languageService A @see LanguageService for display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public Dialog(Node content, 
				  ILanguageService languageService,
				  IStyleContainerService styleService,
				  InternalURLProvider urlProvider) {
		this.content = content;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		valid = new SimpleBooleanProperty(true);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane.getChildren().add(content);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		
		ok.setOnAction((e) -> {
			okPressed();
		});
		
		close.setOnAction((e) -> {
			closePressed();
		});
		
		valid.addListener((v, o, n) -> {
			ok.setDisable(!n);
		});
		
		setupDisplay();
		setupStyle();
	}
	
	private void setupDisplay() {
		if (languageService == null) {
			ok.setText("Ok");
			close.setText("Close");
			return;
		}
		
		ok.setText(languageService.getValue(DisplayStrings.OK));
		close.setText(languageService.getValue(DisplayStrings.CLOSE));
		
		languageService.addListener(() -> {
			ok.setText(languageService.getValue(DisplayStrings.OK));
			close.setText(languageService.getValue(DisplayStrings.CLOSE));
		});
	}
	
	private void setupStyle() {
		if (styleService == null || urlProvider == null) {
			return;
		}
		
		this.getStyleClass().add("root");
		styleUpdater = new StyleUpdater(urlProvider, "dialogstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.DIALOG), this, false);
		
		String style = styleService.get(StyledComponents.DIALOG);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	/**
	 * 
	 * @param cancelAction A @see Runnable to execute if the user presses the cancel button.
	 */
	public void setOnCancel(Runnable cancelAction) {
		cancel = cancelAction;
	}
	
	/**
	 * 
	 * @param completeAction A @see Runnable to execute if the user presses the ok button.
	 */
	public void setOnComplete(Runnable completeAction) {
		complete = completeAction;
	}
	
	/**
	 * 
	 * @return If the dialog is valid. If this value is false the ok button will be disabled.
	 */
	public SimpleBooleanProperty isValid() {
		return valid;
	}
	
	private void okPressed() {
		LogRunner.logger().info("Ok Button Pressed.");
		
		if (complete != null) {
			LogRunner.logger().info("Running complete action.");
			complete.run();
		}
		
		Stage s = (Stage)super.getScene().getWindow();
        s.close();
	}
	
	private void closePressed() {
		LogRunner.logger().info("Cancel Button Pressed.");
		
		if (cancel != null) {
			LogRunner.logger().info("Running cancel action.");
			cancel.run();
		}
		
		Stage s = (Stage)super.getScene().getWindow();
        s.close();
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}

}
