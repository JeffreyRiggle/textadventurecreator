package ilusr.textadventurecreator.views;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.core.ViewSwitcher;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object to setup.
 */
public class WizardView<T> extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private Button back;
	
	@FXML
	private Button forward;
	
	@FXML
	private Button cancel;
	
	@FXML
	private Button finish;
	
	@FXML
	private ViewSwitcher<Integer> switcher;
	
	private final List<WizardPage<T>> pages;
	private final ILanguageService languageService;
	
	private int currentPage = -1;
	private T result;
	private IFinishListener<T> finishListener;
	private ICancelListener cancelListener;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param pages The pages to display.
	 * @param result The item to setup.
	 */
	public WizardView(List<WizardPage<T>> pages, T result) {
		this(pages, result, null);
	}
	
	/**
	 * 
	 * @param pages The pages to display.
	 * @param result The item to setup.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public WizardView(List<WizardPage<T>> pages, T result, ILanguageService languageService) {
		this(pages, result, languageService, null, null);
	}

	/**
	 * 
	 * @param pages The pages to display.
	 * @param result The item to setup.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public WizardView(List<WizardPage<T>> pages, 
					  T result, 
					  ILanguageService languageService,
					  IStyleContainerService styleService,
					  InternalURLProvider urlProvider) {
		this.result = result;
		this.pages = pages;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("WizardView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setupPages();
		
		forward.setOnAction((e) -> {
			next();
		});
		
		back.setOnAction((e) -> {
			last();
		});
		
		cancel.setOnAction((e) -> {
			cancel();
		});
		
		finish.setOnAction((e) -> {
			finish();
		});
		
		setupDisplay();
		next();
		back.setDisable(true);
		setupStyle();
	}
	
	private void setupDisplay() {
		if (languageService == null) {
			forward.setText("Forward &gt;");
			back.setText("&lt; Back");
			finish.setText("Finish");
			cancel.setText("Cancel");
			return;
		}
		
		forward.setText(languageService.getValue(DisplayStrings.WIZARD_FORWARD));
		back.setText(languageService.getValue(DisplayStrings.WIZARD_BACK));
		finish.setText(languageService.getValue(DisplayStrings.FINISH));
		cancel.setText(languageService.getValue(DisplayStrings.CANCEL));
		
		languageService.addListener(() -> {
			forward.setText(languageService.getValue(DisplayStrings.WIZARD_FORWARD));
			back.setText(languageService.getValue(DisplayStrings.WIZARD_BACK));
			finish.setText(languageService.getValue(DisplayStrings.FINISH));
			cancel.setText(languageService.getValue(DisplayStrings.CANCEL));
		});
	}
	
	private void setupStyle() {
		if (styleService == null || urlProvider == null) {
			return;
		}
		
		this.getStyleClass().add("root");
		styleUpdater = new StyleUpdater(urlProvider, "wizardstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.WIZARD_VIEW), this, false);
		
		String style = styleService.get(StyledComponents.WIZARD_VIEW);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	/**
	 * 
	 * @param listener The listener to notify when the wizard has finished successfully.
	 */
	protected void setOnFinish(IFinishListener<T> listener) {
		finishListener = listener;
	}
	
	/**
	 * 
	 * @param listener The listener to notify when the wizard has been canceled.
	 */
	protected void setOnClose(ICancelListener listener) {
		cancelListener = listener;
	}
	
	private void setupPages() {
		int iter = 0;
		
		for (WizardPage<T> page : pages) {
			switcher.addView(iter++, page);
		}
	}
	
	private void next() {
		currentPage++;
		
		switcher.switchView(currentPage);
		
		if (pages.size() <= currentPage+1) {
			forward.setDisable(true);
		}
		
		back.setDisable(false);
		bindCurrent();
	}
	
	private void last() {
		currentPage--;
		
		switcher.switchView(currentPage);
		
		if (0 > currentPage-1) {
			back.setDisable(true);
		}
		
		forward.setDisable(false);
		bindCurrent();
	}
	
	private void bindCurrent() {
		WizardPage<T> page = pages.get(currentPage);
		page.result().set(result);
		finish.setDisable(!page.canFinish().get());
		
		page.canFinish().addListener((v, o, n) -> {
			finish.setDisable(!n);
		});
		
		if (pages.size() <= currentPage+1) {
			forward.setDisable(true);
			return;
		}
		
		forward.setDisable(!page.valid().get());
		page.valid().addListener((v, o, n) -> {
			forward.setDisable(!n);
		});
	}
	
	private void cancel() {
		if (cancelListener != null) {
			cancelListener.cancel();
		}
	}
	
	private void finish() {
		if (finishListener != null) {
			finishListener.finish(result);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
