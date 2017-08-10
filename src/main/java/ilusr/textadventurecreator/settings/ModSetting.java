package ilusr.textadventurecreator.settings;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModSetting {

	private final ISettingsManager settingsManager;
	private String name;
	private String id;
	private SimpleBooleanProperty active;
	
	/**
	 * 
	 * @param settingsManager A @see SettingsManager to provide settings.
	 * @param name The mod name.
	 * @param id The mod id.
	 * @param active If the mod is active.
	 */
	public ModSetting(ISettingsManager settingsManager, String name, String id, boolean active) {
		this.settingsManager = settingsManager;
		this.name = name;
		this.id = id;
		this.active = new SimpleBooleanProperty(active);
		
		initialize();
	}
	
	private void initialize() {
		active.addListener((v, o, n) -> {
			LogRunner.logger().log(Level.INFO, String.format("Updating activation state of %s to %s", id, n));
			settingsManager.setBooleanValue(id + "Active", n);
		});
	}
	
	/**
	 * 
	 * @return The name of the mod.
	 */
	public String name() {
		return name;
	}
	
	/**
	 * 
	 * @return The id of the mod.
	 */
	public String id() {
		return id;
	}
	
	/**
	 * 
	 * @return If the mod is active.
	 */
	public SimpleBooleanProperty active() {
		return active;
	}
}
