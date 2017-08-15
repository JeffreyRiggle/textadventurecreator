package ilusr.textadventurecreator.about;

import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AboutView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ImageView image;
	
	@FXML
	private Label product;
	
	@FXML
	private Label build;
	
	@FXML
	private Label info;
	
	private final ILanguageService languageService;
	private IStyleContainerService styleService;
	private InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	public AboutView(ILanguageService languageService, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		image.setImage(new Image(AssetLoader.getStream("splash.png")));
		product.setText(languageService.getValue(DisplayStrings.TEXTADVENTURECREATOR));
		build.setText(getVersion());
		info.setText("(c) this does not have a copyright :(.\nFor more information see the site I have not made yet.");
		
		this.getStyleClass().add("root");
		styleUpdater = new StyleUpdater(urlProvider, "aboutstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.ABOUT), this, false);
		
		String style = styleService.get(StyledComponents.ABOUT);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	private String getVersion() {
		StringBuilder builder = new StringBuilder();
		builder.append("Version: ");
		
		try {
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream("project.properties"));
			builder.append(props.getProperty("version"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
