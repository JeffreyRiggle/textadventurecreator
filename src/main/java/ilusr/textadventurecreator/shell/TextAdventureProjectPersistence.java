package ilusr.textadventurecreator.shell;

import java.util.Base64;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.iroshell.services.ILayoutService;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;
import textadventurelib.persistence.GameInfoPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureProjectPersistence extends XmlConfigurationObject {

	private static final String TEXT_PROJECT = "TextAdventureProject";
	private static final String ICON = "Icon";
	private static final String STAND_ALONE = "StandAlone";
	private static final String DEV = "Dev";
	private static final String LANG = "Language";
	private static final String PROJ_LOCATION = "ProjectLocation";
	private static final String BACKGROUND_LOCATION = "BackgroundLocation";
	private static final String LAYOUT = "layout";
	
	private final ILayoutService layoutService;
	private final ISettingsManager settingsManager;
	
	private TextAdventurePersistenceObject textAdventure;
	private GameInfoPersistenceObject gameInfo;
	private XmlConfigurationObject iconLocation;
	private XmlConfigurationObject isStandAlone;
	private XmlConfigurationObject isDev;
	private XmlConfigurationObject language;
	private XmlConfigurationObject projectLocation;
	private XmlConfigurationObject backgroundLocation;
	private XmlConfigurationObject layout;
	
	/**
	 * 
	 * @param layoutService A @see LayoutService to provide tabs.
	 * @param settingsManager A @see SettingsManager to provide settings.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TextAdventureProjectPersistence(ILayoutService layoutService, ISettingsManager settingsManager) throws TransformerConfigurationException, ParserConfigurationException {
		super(TEXT_PROJECT);
		this.layoutService = layoutService;
		this.settingsManager = settingsManager;
		
		textAdventure = new TextAdventurePersistenceObject();
		gameInfo = new GameInfoPersistenceObject();
		iconLocation = new XmlConfigurationObject(ICON, new String());
		isStandAlone = new XmlConfigurationObject(STAND_ALONE, false);
		isDev = new XmlConfigurationObject(DEV, false);
		language = new XmlConfigurationObject(LANG, new String());
		projectLocation = new XmlConfigurationObject(PROJ_LOCATION, new String());
		backgroundLocation = new XmlConfigurationObject(BACKGROUND_LOCATION, new String());
		layout = new XmlConfigurationObject(LAYOUT, new String());
	}
	
	/**
	 * 
	 * @return A @see TextAdventurePersistenceObject represeting the current text adventure.
	 */
	public TextAdventurePersistenceObject getTextAdventure() {
		return textAdventure;
	}
	
	/**
	 * 
	 * @return The current @see GameInfoPersistenceObject.
	 */
	public GameInfoPersistenceObject getGameInfo() {
		return gameInfo;
	}
	
	/**
	 * 
	 * @param location The new location for the game icon.
	 */
	public void setIconLocation(String location) {
		iconLocation.value(location);
	}
	
	/**
	 * 
	 * @return The new location for the game icon.
	 */
	public String getIconLocation() {
		return iconLocation.value();
	}
	
	/**
	 * 
	 * @param standAlone The new value for if the game is a stand alone game.
	 */
	public void setIsStandAlone(boolean standAlone) {
		isStandAlone.value(standAlone);
	}
	
	/**
	 * 
	 * @return If the game is a stand alone game.
	 */
	public boolean getIsStandAlone() {
		return isStandAlone.value();
	}
	
	/**
	 * 
	 * @param isDev The new value for if this game is a dev owned game.
	 */
	public void setIsDev(boolean isDev) {
		this.isDev.value(isDev);
	}
	
	/**
	 * 
	 * @return If this game is a dev owned game.
	 */
	public boolean getIsDev() {
		return isDev.value();
	}
	
	/**
	 * 
	 * @param lang The new language for the game (ex: Java)
	 */
	public void setLanguage(String lang) {
		language.value(lang);
	}
	
	/**
	 * 
	 * @return The current language of the game (ex: Java)
	 */
	public String getLanguage() {
		return language.value();
	}
	
	/**
	 * 
	 * @param location The new location for the project.
	 */
	public void setProjectLocation(String location) {
		projectLocation.value(location);
	}
	
	/**
	 * 
	 * @return The current location for the project.
	 */
	public String getProjectLocation() {
		return projectLocation.value();
	}
	
	/**
	 * 
	 * @param location The new location for the projects background image.
	 */
	public void setBackgroundLocation(String location) {
		backgroundLocation.value(location);
	}
	
	/**
	 * 
	 * @return The location of the projects background image.
	 */
	public String getBackgroundLocation() {
		return backgroundLocation.value();
	}
	
	/**
	 * 
	 * @return The current tab layout.
	 */
	public String layout() {
		return layout.<String>value();
	}
	
	/**
	 * Prepares this object to be saved.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearChildren();
		
		gameInfo.prepareXml();
		super.addChild(gameInfo);
		super.addChild(iconLocation);
		super.addChild(isStandAlone);
		super.addChild(isDev);
		super.addChild(language);
		super.addChild(projectLocation);
		super.addChild(backgroundLocation);
		textAdventure.prepareXml();
		super.addChild(textAdventure);
		
		if (settingsManager.getBooleanSetting(SettingNames.PERSIST_LAYOUT, true)) {
			layout.value(Base64.getEncoder().encodeToString(layoutService.getLayout().getBytes()));
			super.addChild(layout);
		}
	}
	
	/**
	 * 
	 * @param persistence A @see XmlConfigurationObject to load from.
	 */
	public void convertFromPersistence(XmlConfigurationObject persistence) {
		for (PersistXml child : persistence.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case "GameInfo":
					convertGameInfo(cChild);
					break;
				case ICON:
					setIconLocation(cChild.<String>value());
					break;
				case STAND_ALONE:
					setIsStandAlone(cChild.<Boolean>value());
					break;
				case DEV:
					setIsDev(cChild.<Boolean>value());
					break;
				case LANG:
					setLanguage(cChild.<String>value());
					break;
				case PROJ_LOCATION:
					setProjectLocation(cChild.<String>value());
					break;
				case BACKGROUND_LOCATION:
					setBackgroundLocation(cChild.<String>value());
					break;
				case "TextAdventure":
					convertTextAdventure(cChild);
					break;
				case LAYOUT:
					if (settingsManager.getBooleanSetting(SettingNames.PERSIST_LAYOUT, true)) {
						layout.value(new String(Base64.getDecoder().decode(cChild.<String>value())));
					} else {
						layout.value(new String());
					}
				default:
					break;
			}
		}
	}
	
	private void convertGameInfo(XmlConfigurationObject obj) {
		try {
			GameInfoPersistenceObject info = new GameInfoPersistenceObject();
			info.convertFromPersistence(obj);
			
			gameInfo = info;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void convertTextAdventure(XmlConfigurationObject obj) {
		try {
			TextAdventurePersistenceObject textAdventure = new TextAdventurePersistenceObject();
			textAdventure.convertFromPersistence(obj);
			this.textAdventure = textAdventure;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
