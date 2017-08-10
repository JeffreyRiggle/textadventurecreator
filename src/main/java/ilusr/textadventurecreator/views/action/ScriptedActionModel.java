package ilusr.textadventurecreator.views.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;

import ilusr.core.io.FileUtilities;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedActionModel {

	private ScriptedActionPersistenceObject action;
	private SimpleStringProperty script;
	private LanguageAwareString actionText;
	private LanguageAwareString viewText;
	
	/**
	 * 
	 * @param action A @see ScriptedActionPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ScriptedActionModel(ScriptedActionPersistenceObject action, ILanguageService languageService) {
		this.action = action;
		
		script = new SimpleStringProperty(action.getScript());
		actionText = new LanguageAwareString(languageService, DisplayStrings.ACTION_SCRIPT);
		viewText = new LanguageAwareString(languageService, DisplayStrings.VIEW_IN_EDITOR);
		
		initialize();
	}
	
	private void initialize() {
		script.addListener((v, o, n) -> {
			action.setScript(n);
		});
		
		if (action.getScript() != null && !action.getScript().isEmpty()) {
			script.set(action.getScript());
			return;
		}
		
		LogRunner.logger().log(Level.INFO, "No script found. Using default script.");
		try {
			script.set(FileUtilities.getFileContentWithReturns(new File(getClass().getResource("DefaultActionScript.js").toURI().getSchemeSpecificPart())));
		} catch (Exception e) {
			script.set(new String());
		}
	}
	
	/**
	 * 
	 * @return The script contents
	 */
	public SimpleStringProperty script() {
		return script;
	}
	
	/**
	 * 
	 * @return Display string for action script.
	 */
	public SimpleStringProperty actionText() {
		return actionText;
	}
	
	/**
	 * 
	 * @return Display string for view in editor.
	 */
	public SimpleStringProperty viewText() {
		return viewText;
	}
	
	/**
	 * Opens the current script in the OS associated editor for js files.
	 */
	public void showScriptInEditor() {
		try {
			File temp = File.createTempFile("tempactionscript", ".js");
			FileWriter fWriter = new FileWriter(temp.getAbsolutePath());
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			bWriter.write(script.get());
			bWriter.close();
			
			LogRunner.logger().log(Level.INFO, "Opening script file.");
			Process proc = Runtime.getRuntime().exec(new String[] {"cmd", "/c", "start", "/wait",  temp.getAbsolutePath()});
			proc.waitFor();
		    
			LogRunner.logger().log(Level.INFO, "Script file closed.");
			script.set(FileUtilities.getFileContentWithReturns(temp));
			temp.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public ScriptedActionPersistenceObject getPersistenceObject() {
		return action;
	}
}
