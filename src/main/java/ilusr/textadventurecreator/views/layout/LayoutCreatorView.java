package ilusr.textadventurecreator.views.layout;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutCreatorView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private LayoutDesignArea layout;
	
	@FXML
	private TextField id;
	
	@FXML
	private Label idLabel;
	
	@FXML
	private SplitPane designPane;
	
	@FXML
	private Tab toolkit;
	
	@FXML
	private Tab properties;
	
	@FXML
	private LayoutPropertiesView props;
	
	@FXML
	private Button compile;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private LayoutCreatorModel model;
	
	/**
	 * 
	 * @param model The @see LayoutCreatorModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public LayoutCreatorView(LayoutCreatorModel model, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutCreatorView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The associated layout creator model.
	 */
	public LayoutCreatorModel model() {
		return model;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		id.textProperty().bindBidirectional(model.id());
		idLabel.textProperty().bind(model.idText());
		designPane.setDividerPosition(0, 0.8);
		
		toolkit.textProperty().bind(model.toolkitText());
		toolkit.setClosable(false);
		
		properties.textProperty().bind(model.propertiesText());
		properties.setClosable(false);
		props.setModel(model);
		layout.setModel(model);
		
		compile.textProperty().bind(model.compileText());
		compile.setOnAction(e -> {
			model.compile();
		});
		
		setupStyles();
	}

	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "layoutstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.LAYOUT), this, false);
		
		String style = styleService.get(StyledComponents.LAYOUT);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
