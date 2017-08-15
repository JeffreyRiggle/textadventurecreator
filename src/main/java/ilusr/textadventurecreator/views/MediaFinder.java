package ilusr.textadventurecreator.views;

import java.io.File;

import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.gamestate.MediaConverterModel;
import ilusr.textadventurecreator.views.gamestate.MediaConverterView;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MediaFinder {

	//Java does not have full video/audio codec support http://docs.oracle.com/javafx/2/api/javafx/scene/media/package-summary.html
	private final String[] supportedExtensions = { "aif", "aiff", "fxm", "flv", "m3u8", "mp3", "mp4", "m4a", "m4v", "wav", "jpg", "png", "bmp", "wbmp", "gif" };
	
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	
	private Window window;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to provide dialogs.
	 */
	public MediaFinder(IDialogService dialogService, ILanguageService languageService, IDialogProvider dialogProvider) {
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
	}
	
	/**
	 * 
	 * @param window The window to show the open dialog against.
	 */
	public void setWindow(Window window) {
		this.window = window;
	}
	
	/**
	 * 
	 * @param location The property to update when the location is selected.
	 */
	public void getMediaLocation(SimpleStringProperty location) {
		FileChooser chooser = new FileChooser();
		File content = chooser.showOpenDialog(window);
				
		if (content == null) {
			LogRunner.logger().info("No media selected not updating location.");
			return;
		}
				
		String fileName = content.getName();
		String extension = fileName.substring(fileName.indexOf('.')+1);
		
		if (supportedExtension(extension)) {
			LogRunner.logger().info(String.format("Setting location to %s", content.getAbsolutePath()));
			location.set(content.getAbsolutePath());
			return;
		}
		
		LogRunner.logger().info(String.format("Extension %s is not a java supported extension showing conversion dialog.", extension));
		MediaConverterModel model = new MediaConverterModel(content, languageService);
		
		Dialog dialog = dialogProvider.create(new MediaConverterView(model));
		dialog.isValid().bind(model.valid());
		dialog.setOnComplete(() -> {
			LogRunner.logger().info(String.format("Setting location to %s", model.newFileLocation()));
			location.set(model.newFileLocation());
		});
				
		dialogService.displayModal(dialog, languageService.getValue(DisplayStrings.CONVERT));
	}
	
	private boolean supportedExtension(String extension) {
		for (String ext : supportedExtensions) {
			if (ext.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		
		return false;
	}
}
