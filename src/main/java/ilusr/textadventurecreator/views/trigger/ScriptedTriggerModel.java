package ilusr.textadventurecreator.views.trigger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import ilusr.core.io.FileUtilities;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedTriggerModel {

	private ScriptedTriggerPersistenceObject trigger;
	private SimpleStringProperty script;
	private LanguageAwareString scriptText;
	private LanguageAwareString editorText;
	
	/**
	 * 
	 * @param trigger A @see ScriptedTriggerPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public ScriptedTriggerModel(ScriptedTriggerPersistenceObject trigger, ILanguageService languageService) {
		this.trigger = trigger;
		script = new SimpleStringProperty();
		scriptText = new LanguageAwareString(languageService, DisplayStrings.SCRIPT);
		editorText = new LanguageAwareString(languageService, DisplayStrings.VIEW_IN_EDITOR);
		
		initialize();
	}
	
	private void initialize() {
		script.addListener((v, o, n) -> {
			trigger.setScript(n);
		});
		
		if (trigger.getScript() == null || trigger.getScript().isEmpty()) {
			try {
				script.set(FileUtilities.getFileContentWithReturns(new File(getClass().getResource("DefaultTriggerScript.js").toURI().getSchemeSpecificPart())));
			} catch (Exception e) {
				script.set(new String());
			}
		} else {
			script.set(trigger.getScript());
		}
	}
	
	/**
	 * 
	 * @return The script to run.
	 */
	public SimpleStringProperty script() {
		return script;
	}
	
	/**
	 * Shows the script in an editor.
	 */
	public void showScriptInEditor() {
		try {
			File temp = File.createTempFile("temptriggerscript", ".js");
			FileWriter fWriter = new FileWriter(temp.getAbsolutePath());
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			bWriter.write(script.get());
			bWriter.close();
			
			LogRunner.logger().info("Opening editor for scripted trigger");
			Process proc = Runtime.getRuntime().exec(new String[] {"cmd", "/c", "start", "/wait",  temp.getAbsolutePath()});
			proc.waitFor();
		    
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
	public ScriptedTriggerPersistenceObject getPersistenceObject() {
		return trigger;
	}
	
	/**
	 * 
	 * @return Display string for script.
	 */
	public SimpleStringProperty scriptText() {
		return scriptText;
	}
	
	/**
	 * 
	 * @return Display string for editor.
	 */
	public SimpleStringProperty editorText() {
		return editorText;
	}
}
