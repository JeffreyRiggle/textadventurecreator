package ilusr.textadventurecreator.settings;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.documentinterfaces.sdi.SingleDocumentInterface;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SettingsView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private SingleDocumentInterface sdi;
	
	private final ISettingsViewRepository settingsViewRepository;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param settingsManager A @see SettingsManager to provide settings.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param modManager A @see ModManager to manage mods.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public SettingsView(ISettingsViewRepository settingsViewRepository, 
						ILanguageService languageService,
						IStyleContainerService styleService,
						InternalURLProvider urlProvider) {
		this.settingsViewRepository = settingsViewRepository;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsView.fxml"));
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
		sdi.setSelectorPane(new SDISelector(settingsViewRepository.getRegisteredViews(), languageService));
		
		this.getStyleClass().add("root");
		styleUpdater = new StyleUpdater(urlProvider, "settingsstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.SETTINGS), this, false);
		
		String style = styleService.get(StyledComponents.SETTINGS);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
