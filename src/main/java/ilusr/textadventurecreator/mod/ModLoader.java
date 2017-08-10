package ilusr.textadventurecreator.mod;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.shell.IInitialize;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModLoader implements IInitialize {

	private List<IMod> mods;
	
	/**
	 * Creates a mod loader
	 */
	public ModLoader() {
		mods = new ArrayList<IMod>();
	}

	@Override
	public void initialize() {
		File modFolder = new File(System.getProperty("user.home") + "/ilusr/mods");
		
		if (!modFolder.exists()) {
			modFolder.mkdirs();
		}
		
		File[] jars = modFolder.listFiles((f) -> {
			return f.getPath().toLowerCase().endsWith(".jar");
		});
		
		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < jars.length; i++) {
			try {
				urls.add(jars[i].toURI().toURL());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
		ServiceLoader<IMod> loader = ServiceLoader.load(IMod.class, classLoader);
		
		loader.forEach((m) -> {
			LogRunner.logger().log(Level.INFO, String.format("Found Mod %s", m.name()));
			mods.add(m);
		});
	}
	
	/**
	 * 
	 * @return A List of @see IMod that was loaded.
	 */
	public List<IMod> getMods() {
		return mods;
	}
}
