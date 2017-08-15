package ilusr.textadventurecreator.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ilusr.iroshell.core.ApplicationClosingListener;
import ilusr.iroshell.services.IApplicationClosingManager;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SettingsManager implements ISettingsManager, ApplicationClosingListener {

	private Map<String, Object> settings;
	private XmlConfigurationManager configurationManager;
	private IApplicationClosingManager closingManager;
	
	/**
	 * 
	 * @param configurationManager A @see XmlConfigurationManager to use to save data.
	 * @param closingManager A @see IApplicationClosingManager to listen for closing events.
	 */
	public SettingsManager(XmlConfigurationManager configurationManager, IApplicationClosingManager closingManager) {
		settings = new HashMap<String, Object>();
		this.configurationManager = configurationManager;
		this.closingManager = closingManager;
		
		initialize();
	}
	
	private void initialize() {
		closingManager.addApplicationClosingListener(this);
		
		try {
			configurationManager.load();
			
			XmlConfigurationObject root = (XmlConfigurationObject)configurationManager.configurationObjects().get(0);
			for (PersistXml child : root.children()) {
				if (!(child instanceof XmlConfigurationObject)) {
					continue;
				}
				
				XmlConfigurationObject childC = (XmlConfigurationObject)child;
				LogRunner.logger().info(String.format("Initializing setting %s with value %s", childC.name(), childC.value()));
				settings.put(childC.name(), childC.value());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setStringSetting(String settingName, String value) {
		settings.put(settingName, value);
	}
	
	@Override
	public String getStringSetting(String settingName, String defaultValue) {
		if (!settings.containsKey(settingName)) {
			return defaultValue;
		}
		
		Object retVal = settings.get(settingName);
		if (retVal instanceof String) {
			return (String)retVal;
		}
		
		return defaultValue;
	}
	
	@Override
	public void setBooleanValue(String settingName, boolean value) {
		settings.put(settingName, value);
	}
	
	@Override
	public boolean getBooleanSetting(String settingName, boolean defaultValue) {
		if (!settings.containsKey(settingName)) {
			return defaultValue;
		}
		
		Object retVal = settings.get(settingName);
		if (retVal instanceof Boolean) {
			return (Boolean)retVal;
		}
		
		return defaultValue;
	}
	
	@Override
	public void setIntValue(String settingName, int value) {
		settings.put(settingName, value);
	}
	
	@Override
	public int getIntSetting(String settingName, int defaultValue) {
		if (!settings.containsKey(settingName)) {
			return defaultValue;
		}
		
		Object retVal = settings.get(settingName);
		if (retVal instanceof Integer) {
			return (Integer)retVal;
		}
		
		return defaultValue;
	}
	
	@Override
	public void saveAsync() {
		new Thread(() -> { save(); }).start();
	}
	
	@Override
	public void save() {
		try {
			LogRunner.logger().info("Saving settings.");
			configurationManager.clearConfigurationObjects();
			XmlConfigurationObject root = new XmlConfigurationObject("Settings");
			for (Entry<String, Object> setting : settings.entrySet()) {
				try {
					root.addChild(new XmlConfigurationObject(setting.getKey(), setting.getValue()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			configurationManager.addConfigurationObject(root);
			configurationManager.prepare();
			configurationManager.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		save();
	}
}
