package ilusr.textadventurecreator.library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.core.interfaces.Callback;
import ilusr.iroshell.core.ApplicationClosingListener;
import ilusr.iroshell.services.ApplicationClosingManager;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import ilusr.textadventurecreator.shell.IInitialize;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;

/**
 *
 * @author Jeff Riggle
 *
 */
public class LibraryService implements ApplicationClosingListener, IInitialize {

	private final Object libraryLock;
	private final ProjectStatusService statusService;
	private final ISettingsManager settingsManager;
	private final TextAdventureProvider textAdventureProvider;
	private final ILanguageService languageService;
	private final String savePath;

	private List<LibraryItem> libraries;
	private List<Runnable> listeners;
	private String currentLibraryPath;

	/**
	 *
	 * @param statusService A @see ProjectStatusService to update the projects status.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 * @param closingManager A @see ApplicationClosingManager to register to closing events.
	 * @param textAdventureProvider A @see TextAdventureProvider to get the text adventure.
	 * @param languageService A @see LanguagueService to get display strings.
	 */
	public LibraryService(ProjectStatusService statusService,
						  ISettingsManager settingsManager,
						  ApplicationClosingManager closingManager,
						  TextAdventureProvider textAdventureProvider,
						  ILanguageService languageService) {
		this(statusService, settingsManager, closingManager, textAdventureProvider, languageService, System.getProperty("user.home") + "//ilusr//libraries//global.talml");
	}

	/**
	 *
	 * @param statusService A @see ProjectStatusService to update the projects status.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 * @param closingManager A @see ApplicationClosingManager to register to closing events.
	 * @param textAdventureProvider A @see TextAdventureProvider to get the text adventure.
	 * @param languageService A @see LanguagueService to get display strings.
	 * @param savePath the path to save the library to.
	 */
	public LibraryService(ProjectStatusService statusService,
			  ISettingsManager settingsManager,
			  ApplicationClosingManager closingManager,
			  TextAdventureProvider textAdventureProvider,
			  ILanguageService languageService,
			  String savePath) {
		this.statusService = statusService;
		this.settingsManager = settingsManager;
		this.textAdventureProvider = textAdventureProvider;
		this.languageService = languageService;
		this.savePath = savePath;

		libraries = new ArrayList<LibraryItem>();
		listeners = new ArrayList<Runnable>();
		libraryLock = new Object();
		closingManager.addApplicationClosingListener(this);
	}

	@Override
	public void initialize() {
		LogRunner.logger().info("Initializing Library Service");

		textAdventureProvider.addListener((p) -> {
			textAdventureChanged(p);
		});

		if (!settingsManager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true)) {
			LogRunner.logger().info("Global library disabled not loading global library file.");
			return;
		}

		File lib = new File(savePath);
		currentLibraryPath = lib.getAbsolutePath();

		if (lib.exists()) {
			LogRunner.logger().info("Loading global library into Library Service.");
			loadLibrary(lib.getAbsolutePath());
		}
	}

	private void textAdventureChanged(TextAdventureProjectPersistence textAdventure) {
		if (!settingsManager.getBooleanSetting(SettingNames.GAME_LIBRARY, false) || settingsManager.getBooleanSetting(SettingNames.GLOBAL_LIBRARY, true)) {
			LogRunner.logger().info("Game Library turned off or global library turned on not refreshing library.");
			return;
		}

		File lib = new File(System.getProperty("user.home") + "//ilusr//libraries//" + textAdventure.getTextAdventure().gameName() + ".talml");
		currentLibraryPath = lib.getAbsolutePath();

		if (lib.exists()) {
			LogRunner.logger().info(String.format("Loading Library associated with %s.", textAdventure.getGameInfo().gameName()));
			loadLibrary(lib.getAbsolutePath());
			return;
		}

		synchronized (libraryLock) {
			libraries.clear();
		}

		notifyListeners();
	}

	/**
	 *
	 * @param path The path to load the library item from.
	 * @param callback A @see Callback to run with the @see LibraryItem once the item has been created in memory.
	 */
	public void importLibrary(String path, Callback<LibraryItem> callback) {
		final StatusItem statusItem = new StatusItem(languageService.getValue(DisplayStrings.IMPORTING), null, 10000);

		statusItem.setOnStart(() -> {
			importAction(path, statusItem, callback);
		});

		LogRunner.logger().info(String.format("Queueing up import of %s.", path));
		statusService.addStatusItemToQueue(statusItem);
	}

	private void importAction(String path, StatusItem statusItem, Callback<LibraryItem> callback) {
		new Thread(() -> {
			try {
				LogRunner.logger().info(String.format("Starting import of %s.", path));
				statusItem.progressAmount().set(.2);
				XmlConfigurationManager manager = new XmlConfigurationManager(path);
				statusItem.progressAmount().set(.4);
				manager.load();
				statusItem.progressAmount().set(.7);
				LibraryItem item = new LibraryItem((XmlConfigurationObject)manager.configurationObjects().get(0));
				statusItem.progressAmount().set(.9);
				importLibrary(item);
				statusItem.progressAmount().set(1.0);
				statusItem.indicator().set(StatusIndicator.Good);
				if (callback != null) {
					callback.execute(item);
				}
			} catch (Exception e) {
				LogRunner.logger().severe(e);
				statusItem.indicator().set(StatusIndicator.Error);
			}

			LogRunner.logger().info(String.format("finished import of %s.", path));
			statusItem.finished().set(true);
		}).start();
	}

	/**
	 *
	 * @param item The @see LibraryItem to add to the service.
	 */
	public void importLibrary(LibraryItem item) {
		synchronized(libraryLock) {
			if (libraries.contains(item)) {
				return;
			}

			LogRunner.logger().info(String.format("Adding Library Item %s to service.", item.getLibraryName()));
			libraries.add(item);
		}

		notifyListeners();
	}

	/**
	 *
	 * @param path The path to save the library item to.
	 * @param item The @see LibraryItem to save.
	 * @param callback A @see Runnable to execute when the save is completed.
	 */
	public void exportLibrary(String path, LibraryItem item, Runnable callback) {
		final StatusItem statusItem = new StatusItem(languageService.getValue(DisplayStrings.EXPORTING), null, 10000);

		statusItem.setOnStart(() -> {
			exportAction(path, statusItem, item, callback);
		});

		LogRunner.logger().info(String.format("Queueing up export of Library Item %s.", item.getLibraryName()));
		statusService.addStatusItemToQueue(statusItem);
	}

	private void exportAction(String path, StatusItem statusItem, LibraryItem item, Runnable callback) {
		new Thread(() -> {
			try {
				LogRunner.logger().info(String.format("Exporting Library Item %s.", item.getLibraryName()));
				statusItem.progressAmount().set(.2);
				XmlConfigurationManager manager = new XmlConfigurationManager(path);
				statusItem.progressAmount().set(.4);
				manager.addConfigurationObject(item.toConfigurationObject());
				statusItem.progressAmount().set(.6);
				manager.save();
				statusItem.progressAmount().set(1.0);
				statusItem.indicator().set(StatusIndicator.Good);
			} catch (Exception e) {
				LogRunner.logger().severe(e);
				statusItem.indicator().set(StatusIndicator.Error);
			}

			if (callback != null) {
				callback.run();
			}

			LogRunner.logger().info(String.format("Finished exporting Library Item %s.", item.getLibraryName()));
			statusItem.finished().set(true);
		}).start();
	}

	/**
	 *
	 * @param path The path to save the library to.
	 * @param libraryName The name of the library.
	 * @param callback A @see Runnable to execute when the library has finished saving.
	 */
	public void exportLibrary(String path, String libraryName, Runnable callback) {
		LibraryItem item = null;

		synchronized(libraryLock) {
			Optional<LibraryItem> libraryitem = libraries.stream().filter(i -> i.getLibraryName().equals(libraryName)).findFirst();
			if (!libraryitem.isPresent()) {
				LogRunner.logger().info(String.format("Not exporting % since Library Item could not be found", libraryName));
				return;
			}

			item = libraryitem.get();
		}

		try {
			exportLibrary(path, item, callback);
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	/**
	 *
	 * @return A @see List of @see LibraryItem associated with this service.
	 */
	public List<LibraryItem> getItems() {
		synchronized(libraryLock) {
			return new ArrayList<LibraryItem>(libraries);
		}
	}

	/**
	 *
	 * @param item The @see LibraryItem to remove from this service.
	 */
	public void removeLibraryItem(LibraryItem item) {
		synchronized(libraryLock) {
			if (!libraries.contains(item)) {
				return;
			}

			LogRunner.logger().info(String.format("Removing Library Item %s from service.", item.getLibraryName()));
			libraries.remove(item);
		}

		notifyListeners();
	}

	/**
	 *
	 * @param libraryName The name of the library to remove from this service.
	 */
	public void removeLibraryItem(String libraryName) {
		synchronized(libraryLock) {
			Optional<LibraryItem> libraryitem = libraries.stream().filter(i -> i.getLibraryName().equals(libraryName)).findFirst();
			if (!libraryitem.isPresent()) {
				LogRunner.logger().info(String.format("Not removing Library Item %s from service since it could not be found", libraryName));
				return;
			}

			libraries.remove(libraryitem.get());
		}

		notifyListeners();
	}

	/**
	 *
	 * @param path A path to load library items from.
	 */
	public void loadLibrary(String path) {
		try {
			LogRunner.logger().info(String.format("Loading library from path %s", path));
			XmlConfigurationManager manager = new XmlConfigurationManager(path);
			manager.load();
			loadLibrary((XmlConfigurationObject)manager.configurationObjects().get(0));
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	private void loadLibrary(XmlConfigurationObject library) {
		LogRunner.logger().info("Loading library from configuration object.");
		List<LibraryItem> items = getLibraryItems(library);

		synchronized (libraryLock) {
			libraries.clear();
			libraries.addAll(items);
		}

		notifyListeners();
	}

	private List<LibraryItem> getLibraryItems(XmlConfigurationObject library) {
		List<LibraryItem> retVal = new ArrayList<LibraryItem>();

		for (PersistXml child : library.children()) {
			retVal.add(new LibraryItem((XmlConfigurationObject)child));
		}

		return retVal;
	}

	/**
	 *
	 * @param listener A @see Runnable to execute when a LibraryItem is added or removed from this service.
	 */
	public void addLibraryChangedListener(Runnable listener) {
		listeners.add(listener);
	}

	/**
	 *
	 * @param listener A @see Runnable to execute when a LibraryItem is added or removed from this service.
	 */
	public void removeLibraryChangedListener(Runnable listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (Runnable listener : listeners) {
			listener.run();
		}
	}

	@Override
	public void onClose() {
		if (currentLibraryPath == null || currentLibraryPath.isEmpty()) {
			LogRunner.logger().info("Application closed but there was nothing to save.");
			return;
		}

		save(currentLibraryPath);
	}

	private void save(String path) {
		try {
			LogRunner.logger().info(String.format("Saving Library to path %s", path));
			XmlConfigurationManager manager = new XmlConfigurationManager(path);
			manager.addConfigurationObject(toConfigurationObject());
			manager.save();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	private XmlConfigurationObject toConfigurationObject() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject root = new XmlConfigurationObject("Libraries");

		synchronized (libraryLock) {
			for (LibraryItem item : libraries) {
				root.addChild(item.toConfigurationObject());
			}
		}

		return root;
	}
}
