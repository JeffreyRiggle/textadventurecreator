package ilusr.textadventurecreator.error;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.RemoveListCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ReportIssueView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private Label typeLabel;
	
	@FXML
	private ComboBox<String> type;
	
	@FXML
	private Label descriptionLabel;
	
	@FXML
	private TextArea description;
	
	@FXML
	private Label attachmentLabel;
	
	@FXML
	private ListView<String> attachments;
	
	@FXML
	private Label replyLabel;
	
	@FXML
	private Label replyToLabel;
	
	@FXML
	private TextField replyAddress;
	
	@FXML
	private CheckBox replyEnabled;
	
	private ReportIssueModel model;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param model A @see ReportIssueModel to associate with this view.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public ReportIssueView(ReportIssueModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportIssueView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		typeLabel.textProperty().bind(model.typeText());
		descriptionLabel.textProperty().bind(model.descriptionText());
		attachmentLabel.textProperty().bind(model.attachmentText());
		replyLabel.textProperty().bind(model.replyText());
		replyToLabel.textProperty().bind(model.replyAddressText());
		replyEnabled.selectedProperty().bindBidirectional(model.includeReplyTo());
		
		replyEnabled.selectedProperty().addListener((v, o, n) -> {
			replyAddress.setDisable(!n);
			replyToLabel.setDisable(!n);
		});
		
		replyAddress.setDisable(!model.includeReplyTo().get());
		replyToLabel.setDisable(!model.includeReplyTo().get());
		replyAddress.textProperty().bindBidirectional(model.replyToAddress());
		type.setItems(model.problemType().list());
		type.valueProperty().bindBidirectional(model.problemType().selected());
		
		description.textProperty().bindBidirectional(model.problemDescription());
		

		
		attachments.setCellFactory(new RemoveListCellFactory<String>(model.addId(),
					(e) -> { addAttachment(); },
					(s) -> { return new Label(s); },
				    (m) -> {  model.attachments().remove(m); }));
		
		attachments.itemsProperty().get().add(model.addId());
		ObservableListBinder<String> binder = new ObservableListBinder<String>(model.attachments(), attachments.getItems());
		binder.bindSourceToTarget();
		
		setupStyle();
	}

	private void addAttachment() {
		FileChooser chooser = new FileChooser();
		File content = chooser.showOpenDialog(super.sceneProperty().get().windowProperty().get());
		model.attachments().add(content.getAbsolutePath());
	}
	
	private void setupStyle() {
		this.getStyleClass().add("root");
		styleUpdater = new StyleUpdater(urlProvider, "reportissuestylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.REPORT), this, false);
		
		String style = styleService.get(StyledComponents.REPORT);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
