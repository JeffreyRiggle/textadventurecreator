package ilusr.textadventurecreator.search;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.javafx.ObservableListBinder;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The Type of object to find.
 */
public abstract class BaseFinder<T> extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private TextField search;
	
	@FXML
	private ComboBox<String> searchField;
	
	@FXML
	private ComboBox<String> searchScope;
	
	@FXML
	private ListView<T> searchArea;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private BaseFinderModel<T> model;

	/**
	 * 
	 * @param model A @see BaseFinderModel to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BaseFinder(BaseFinderModel<T> model,
					  IStyleContainerService styleService,
					  InternalURLProvider urlProvider) {
		this.model = model;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BaseFinder.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	/**
	 * 
	 * @return A @see IViewCreator to provide list item displays.
	 */
	protected abstract IViewCreator<T> creator();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		search.textProperty().set(model.helpText().get());
		
		search.focusedProperty().addListener((v, o, n) -> {
			if (n && search.textProperty().get().equals(model.helpText().get())) {
				search.textProperty().set(new String());
			}
			
			if (!n && search.textProperty().get().isEmpty()) {
				search.textProperty().set(model.helpText().get());
			}
		});
		
		model.searchText().bind(search.textProperty());
		
		searchField.getItems().addAll(model.fields().list());
		searchField.setValue(model.fields().selected().get());
		model.fields().selected().bind(searchField.valueProperty());
		
		searchScope.getItems().addAll(model.scope().list());
		searchScope.setValue(model.scope().selected().get());
		model.scope().selected().bind(searchScope.valueProperty());
		
		ObservableListBinder<T> binder = new ObservableListBinder<T>(model.results().list(), searchArea.getItems());
		binder.bindSourceToTarget();
		
		searchArea.setCellFactory(new SearchListCellFactory<T>(creator(), (i) -> { model.inspect(i); }));
		searchArea.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
			model.results().selected().set(n);
		});
		
		setupStyles();
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "finderstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.FINDER), this, false);
		
		String style = styleService.get(StyledComponents.FINDER);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
