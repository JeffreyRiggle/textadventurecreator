package ilusr.textadventurecreator.views.gamestate;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import ilusr.core.interfaces.Callback;
import ilusr.core.io.ProcessHelpers;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MediaConverterModel {

	private final String AUDIO = "Audio";
	private final String VIDEO = "Video";
	private final String IMAGE = "Image";
	
	private final String VIDEO_COMMAND = "ffmpeg -i \"%s\" -vcodec h264 -acodec aac -strict -2 \"%s.mp4\"";
	private final String AUDIO_COMMAND = "ffmpeg -i \"%s\" -f mp2 \"%s.mp3\"";
	private final String IMAGE_COMMAND = "ffmpeg -i \"%s\" \"%s.png\"";
	
	private File originalFile;
	private SelectionAwareObservableList<String> types;
	private SimpleStringProperty newFile;
	private SimpleStringProperty errorText;
	private SimpleStringProperty okText;
	private LanguageAwareString titleText;
	private LanguageAwareString typeText;
	private LanguageAwareString newFileText;
	private LanguageAwareString convertText;
	private SimpleBooleanProperty valid;
	private SimpleBooleanProperty converting;
	
	/**
	 * 
	 * @param originalFile The original file to convert.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MediaConverterModel(File originalFile, ILanguageService languageService) {
		this.originalFile = originalFile;
		
		types = new SelectionAwareObservableList<String>();
		newFile = new SimpleStringProperty();
		errorText = new SimpleStringProperty(languageService.getValue(DisplayStrings.UNABLE_TO_CONVERT));
		okText = new SimpleStringProperty(languageService.getValue(DisplayStrings.CONVERSION_SUCCESS));
		titleText = new LanguageAwareString(languageService, DisplayStrings.COULD_NOT_USE_FILE);
		typeText = new LanguageAwareString(languageService, DisplayStrings.FILE_TYPE);
		newFileText = new LanguageAwareString(languageService, DisplayStrings.NEW_FILE_NAME);
		convertText = new LanguageAwareString(languageService, DisplayStrings.CONVERT);
		valid = new SimpleBooleanProperty();
		converting = new SimpleBooleanProperty();
		
		initialize();
	}
	
	private void initialize() {
		types.list().add(VIDEO);
		types.list().add(IMAGE);
		types.list().add(AUDIO);
	}
	
	/**
	 * 
	 * @return The selected type of media.
	 */
	public SelectionAwareObservableList<String> types() {
		return types;
	}
	
	/**
	 * 
	 * @return The new file to save to.
	 */
	public SimpleStringProperty newFile() {
		return newFile;
	}
	
	/**
	 * 
	 * @return Display string for error.
	 */
	public SimpleStringProperty errorText() {
		return errorText;
	}
	
	/**
	 * 
	 * @return Display string for ok.
	 */
	public SimpleStringProperty okText() {
		return okText;
	}
	
	/**
	 * 
	 * @return Display string for title.
	 */
	public SimpleStringProperty titleText() {
		return titleText;
	}
	
	/**
	 * 
	 * @return Display string for type.
	 */
	public SimpleStringProperty typeText() {
		return typeText;
	}
	
	/**
	 * 
	 * @return Display string for new file.
	 */
	public SimpleStringProperty newFileText() {
		return newFileText;
	}
	
	/**
	 * 
	 * @return Display string for convert.
	 */
	public SimpleStringProperty convertText() {
		return convertText;
	}
	
	/**
	 * 
	 * @return If the conversion has succeeded.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return If the conversion is taking place.
	 */
	public SimpleBooleanProperty converting() {
		return converting;
	}
	
	/**
	 * 
	 * @param callback @see Callback to run when the conversion is complete. The boolean represents if the operation succeeded.
	 */
	public void convert(Callback<Boolean> callback) {
		if (converting.get()) {
			LogRunner.logger().log(Level.INFO, "Not converting since we are in the process of a conversion.");
			return;
		}
		
		converting.set(true);
		new Thread(() -> {
			String original = originalFile.getAbsolutePath();
			String newF = original.substring(0, original.lastIndexOf("\\")+1) + newFile.get();
			Boolean retVal = false;
			LogRunner.logger().log(Level.INFO, String.format("Converting %s to %s\n", original, newF));
			try {
				retVal = convertImpl(original, newF);
			} catch (Exception e) {
				e.printStackTrace();
				retVal = false;
			}
			
			valid.set(retVal);
			callback.execute(retVal);
			Platform.runLater(() -> {
				converting.set(false);
			});
		}).start();
	}
	
	private boolean convertImpl(String original, String newF) throws IOException, InterruptedException {
		String command = null;

		if (types.selected().get().equals(VIDEO)) {
			command = String.format(VIDEO_COMMAND, original, newF);
		} else if (types.selected().get().equals(AUDIO)) {
			command = String.format(AUDIO_COMMAND, original, newF);
		} else if (types.selected().get().equals(IMAGE)) {
			command = String.format(IMAGE_COMMAND, original, newF);
		}
		
		if (command == null) {
			LogRunner.logger().log(Level.INFO, "Unable to determine type for conversion");
			return false;
		}
		
		Process proc = Runtime.getRuntime().exec(command);
		ProcessHelpers.handleProcessStreams(proc);
		return proc.waitFor() == 0;
	}
	
	/**
	 * 
	 * @return The new file location to use.
	 */
	public String newFileLocation() {
		String original = originalFile.getAbsolutePath();
		String newF = original.substring(0, original.lastIndexOf("\\")+1) + newFile.get();
		String ext = new String();
		
		if (types.selected().get().equals(VIDEO)) {
			ext = "mp4";
		} else if (types.selected().get().equals(AUDIO)) {
			ext = "mp3";
		} else if (types.selected().get().equals(IMAGE)) {
			ext = "png";
		}
		
		return String.format("%s.%s", newF, ext);
	}
}
