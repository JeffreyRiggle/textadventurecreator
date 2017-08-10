package ilusr.textadventurecreator.shell;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.interfaces.Callback;
import ilusr.iroshell.services.ILayoutService;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;
import ilusr.persistencelib.configuration.PersistXml;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProjectPersistenceManager {

	private final ProjectStatusService statusService;
	private final ILayoutService layoutService;
	private final ISettingsManager settingsManager;
	
	private XmlManager manager;
	private XmlConfigurationManager configManager;
	
	/**
	 * 
	 * @param manager A @see XmlManager to save data.
	 * @param statusService A @see ProjectStatusService to display status.
	 * @param layoutService A @see LayoutService to add tabs.
	 * @param settingsManager A @see SettingsManager to manage settings.
	 */
	public ProjectPersistenceManager(XmlManager manager, 
									 ProjectStatusService statusService,
									 ILayoutService layoutService,
									 ISettingsManager settingsManager) {
		this.manager = manager;
		this.statusService = statusService;
		this.layoutService = layoutService;
		this.settingsManager = settingsManager;
		configManager = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
	}
	
	/**
	 * 
	 * @param path The file to load.
	 * @return A @see TextAdventureProjectPersistence that was created from the provided path.
	 * @throws TransformerConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public TextAdventureProjectPersistence load(String path) throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
		LogRunner.logger().log(Level.INFO, String.format("Loading project from %s", path));
		TextAdventureProjectPersistence retVal = new TextAdventureProjectPersistence(layoutService, settingsManager);
		
		manager.saveLocation(path);
		configManager.load();
		retVal.convertFromPersistence((XmlConfigurationObject)configManager.configurationObjects().get(0));
		return retVal;
	}
	
	/**
	 * 
	 * @param path The file to load.
	 * @param callback A @see Callback to run when the project is loaded.
	 */
	public void loadAsync(String path, Callback<TextAdventureProjectPersistence> callback) {
		LogRunner.logger().log(Level.INFO, String.format("Queuing up load of %s", path));
		final StatusItem loadItem = new StatusItem("Opening...");
		
		loadItem.setOnStart(() -> {
			new Thread() {
				@Override
				public void run() {
					callback.execute(loadAction(path, loadItem));
				}
			}.start();
		});
		
		loadItem.postFinishLiveTime().set(10000);
		
		statusService.addStatusItemToQueue(loadItem);
	}
	
	private TextAdventureProjectPersistence loadAction(String path, StatusItem item) {
		TextAdventureProjectPersistence retVal = null;
		
		try {
			LogRunner.logger().log(Level.INFO, String.format("Loading project from %s", path));
			retVal = new TextAdventureProjectPersistence(layoutService, settingsManager);
			item.progressAmount().set(.2);
			manager.saveLocation(path);
			item.progressAmount().set(.4);
			configManager.load();
			item.progressAmount().set(.8);
			retVal.convertFromPersistence((XmlConfigurationObject)configManager.configurationObjects().get(0));
			item.progressAmount().set(1.0);
			item.indicator().set(StatusIndicator.Good);
		} catch (Exception e) {
			e.printStackTrace();
			item.indicator().set(StatusIndicator.Error);
		}
		
		item.finished().set(true);
		return retVal;
	}
	
	/**
	 * 
	 * @param project A @see TextAdventureProjectPersistence to save.
	 * @param path The location to save at.
	 */
	public void saveAsync(TextAdventureProjectPersistence project, String path) {
		saveImpl(project, path, true);
	}
	
	/**
	 * 
	 * @param project A @see TextAdventureProjectPersistence to save.
	 * @param path The location to save at.
	 */
	public void save(TextAdventureProjectPersistence project, String path) {
		saveImpl(project, path, false);
	}
	
	/**
	 * 
	 * @param project A @see TextAdventureProjectPersistence to save.
	 */
	public void saveAsync(TextAdventureProjectPersistence project) {
		saveImpl(project, null, true);
	}
	
	/**
	 * 
	 * @param project A @see TextAdventureProjectPersistence to save.
	 */
	public void save(TextAdventureProjectPersistence project) {
		saveImpl(project, null, false);
	}
	
	private void saveImpl(TextAdventureProjectPersistence project, String path, boolean async) {
		LogRunner.logger().log(Level.INFO, String.format("Saving project %s to path %s", path, async ? "Async" : "Sync"));
		final StatusItem saveItem = new StatusItem("Saving...");
		
		saveItem.setOnStart(() -> {
			if (!async) {
				saveAction(project, path, saveItem);
				return;
			}
			
			new Thread() {
				@Override
				public void run() {
					saveAction(project, path, saveItem);
				}
			}.start();
		});
		
		saveItem.postFinishLiveTime().set(10000);
		
		statusService.addStatusItemToQueue(saveItem);
	}
	
	private void saveAction(TextAdventureProjectPersistence project, String path, StatusItem saveItem) {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Saving project to path %s", path));
			saveItem.progressAmount().set(.2);
			if (path != null) {
				manager.saveLocation(path);
			}
			
			configManager.configurationObjects().clear();
			saveItem.progressAmount().set(.4);
			project.prepareXml();
			configManager.addConfigurationObject(project);
			saveItem.progressAmount().set(.6);
			configManager.save();
			saveItem.progressAmount().set(1.0);
			saveItem.indicator().set(StatusIndicator.Good);
			saveItem.finished().set(true);
		} catch (Exception e) {
			e.printStackTrace();
			saveItem.indicator().set(StatusIndicator.Error);
			saveItem.finished().set(true);
		}
	}
}
