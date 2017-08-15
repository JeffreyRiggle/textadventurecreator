package ilusr.textadventurecreator.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilusr.core.ioc.ServiceManager;
import ilusr.iroshell.services.IApplicationClosingManager;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.shell.IInitialize;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModManager implements IInitialize {

	private final ModLoader loader;
	private final ISettingsManager settingsManager;
	private final IApplicationClosingManager closingManager;
	
	private Map<IMod, Boolean> modMap;
	
	/**
	 * 
	 * @param loader A @see ModLoader to load mods.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 */
	public ModManager(ModLoader loader, 
					  ISettingsManager settingsManager,
					  IApplicationClosingManager closingManager) {
		this.loader = loader;
		this.settingsManager = settingsManager;
		this.closingManager = closingManager;
		modMap = new HashMap<IMod, Boolean>();
	}

	@Override
	public void initialize() {
		if (!settingsManager.getBooleanSetting(SettingNames.ALLOW_MODS, true)) {
			LogRunner.logger().info("Not loading mods since mods have been turned off.");
			return;
		}
		
		loader.initialize();
		
		for (IMod mod : loader.getMods()) {
			if (!settingsManager.getBooleanSetting(mod.id() + "Active", true)) {
				modMap.put(mod, false);
				continue;
			}
			
			loadMod(mod);
			modMap.put(mod, true);
		}
		
		closingManager.addApplicationClosingListener(() -> {
			for (IMod mod : modMap.keySet()) {
				if (getActivation(mod.id())) {
					unloadMod(mod);
				}
			}
		});
	}
	
	private void loadMod(IMod mod) {
		try {
			LogRunner.logger().info(String.format("Loading mod: %s(%s)", mod.name(), mod.id()));
			mod.load(ServiceManager.getInstance());
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	private void unloadMod(IMod mod) {
		try {
			LogRunner.logger().info(String.format("Unloading mod: %s(%s)", mod.name(), mod.id()));
			mod.unload();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	/**
	 * 
	 * @return A List of @see IMod that have been loaded.
	 */
	public List<IMod> mods() {
		return new ArrayList<IMod>(modMap.keySet());
	}
	
	/**
	 * Activates a mod.
	 * @param id The Id of the mod to activate.
	 */
	public void activateMod(String id) {
		IMod mod = findModById(id);
		
		if (mod == null) {
			return;
		}
		
		loadMod(mod);
		modMap.put(mod, true);
	}
	
	/**
	 * Deactivates a mod.
	 * @param id The Id of the mod to deactivate.
	 */
	public void deactivateMod(String id) {
		IMod mod = findModById(id);
		
		if (mod == null) {
			return;
		}
		
		unloadMod(mod);
		modMap.put(mod, false);
	}
	
	/**
	 * 
	 * @param id The Id of the mod to check activation status.
	 * @return If the mod is active or not.
	 */
	public boolean getActivation(String id) {
		IMod mod = findModById(id);
		
		if (mod == null) {
			return false;
		}
		
		return modMap.get(mod);
	}
	
	private IMod findModById(String id) {
		for (IMod mod : modMap.keySet()) {
			if (mod.id().equals(id)) {
				return mod;
			}
		}
		
		return null;
	}
}
