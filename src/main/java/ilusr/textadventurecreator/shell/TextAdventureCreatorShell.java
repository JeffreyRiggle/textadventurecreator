package ilusr.textadventurecreator.shell;

import java.util.logging.Level;

import ilusr.iroshell.documentinterfaces.DocumentType;
import ilusr.iroshell.features.ExceptionDisplay;
import ilusr.iroshell.main.ApplicationFeatures;
import ilusr.iroshell.main.MainShell;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.error.ErrorProvider;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureCreatorShell extends MainShell {

	private TextAdventureCreatorComponent component;
	
	/**
	 * The application shell.
	 */
	public TextAdventureCreatorShell() {
		super.applicationSettings.setDocumentType(DocumentType.MDI);
		super.applicationSettings.applicationName("Text Adventure Creator");
		super.applicationSettings.version("Alpha 1.2");
		super.applicationSettings.applicationIcon(new Image(AssetLoader.getResourceURL("TAVCreator.png").toExternalForm()));
		super.applicationSettings.applicationFeatures(ApplicationFeatures.EXCEPTION_HANDLING | ApplicationFeatures.SPLASH_SCREEN | ApplicationFeatures.FULL_SCREEN_START);
		super.applicationSettings.exceptionOptions().displayType(ExceptionDisplay.Default);
		super.applicationSettings.splashOptions().backgroundImage(new Image(AssetLoader.getStream("splash.png")));
		component = new TextAdventureCreatorComponent();
		super.applicationSettings.components().add(component);
		
		super.applicationSettings.exceptionOptions().displayType(ExceptionDisplay.Custom);
		super.applicationSettings.exceptionOptions().errorDialogCreator(new ErrorProvider());
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		component.setFile(findInitialFile());
		super.start(stage);
	}
	
	private String findInitialFile() {
		String retVal = null;
		
		for (String parameter : super.getParameters().getUnnamed()) {
			if (parameter.matches("(?i).*\\.txaml")) {
				LogRunner.logger().log(Level.INFO, String.format("Found file %s", parameter));
				retVal = parameter;
				break;
			}
		}
		
		return retVal;
	}
}
