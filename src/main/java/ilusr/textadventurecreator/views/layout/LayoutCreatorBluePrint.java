package ilusr.textadventurecreator.views.layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import ilusr.core.datamanager.xml.XmlGenerator;
import ilusr.core.datamanager.xml.XmlInputReader;
import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.iroshell.services.ITabContentBluePrint;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import textadventurelib.persistence.LayoutPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutCreatorBluePrint implements ITabContentBluePrint {

	private final String LAYOUT_NAME_PERSISTENCE = "LayoutName: ";
	private final TextAdventureProvider provider;
	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final InternalURLProvider urlProvider;
	private final IStyleContainerService styleService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param urlProvider A @see InternalURLProvider to provide internal resources.
	 * @param styleService service to manage styles.
	 */
	public LayoutCreatorBluePrint(TextAdventureProvider provider,
			                      ILanguageService languageService,
			                      IDialogService dialogService,
			                      InternalURLProvider urlProvider,
			                      IStyleContainerService styleService) {
		this.provider = provider;
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.urlProvider = urlProvider;
		this.styleService = styleService;
	}
	
	@Override
	public ITabContent create() {
		LogRunner.logger().info("Creating new layout tab.");
		return ServiceManager.getInstance().<LayoutCreatorContentTab>get("LayoutCreatorContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		LayoutCreatorContentTab tab = null;
		
		if (customData.startsWith(LAYOUT_NAME_PERSISTENCE)) {
			LogRunner.logger().info(String.format("Creating new layout tab with data.", customData));
			return findLayoutTab(customData.substring(LAYOUT_NAME_PERSISTENCE.length()));
		}
		
		try {
			LogRunner.logger().info("Creating new layout tab from persistence.");
			InputStream stream = new ByteArrayInputStream(customData.getBytes(StandardCharsets.UTF_8));
			XmlInputReader reader = new XmlInputReader(stream);
			XmlManager manager = new XmlManager(new String(), new XmlGenerator(), reader);
			XmlConfigurationManager configurationManager = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			configurationManager.load();
			
			LayoutPersistenceObject layout = new LayoutPersistenceObject();
			layout.convertFromPersistence((XmlConfigurationObject)configurationManager.configurationObjects().get(0));
			
			for (LayoutPersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().getLayouts()) {
				if (obj.id().equals(layout.id())) {
					provider.getTextAdventureProject().getTextAdventure().removeLayout(obj);
					break;
				}
			}
			
			provider.getTextAdventureProject().getTextAdventure().addLayout(layout);
			LayoutCreatorModel model = new LayoutCreatorModel(layout, languageService, dialogService, urlProvider, new SelectionManager());
			tab = new LayoutCreatorContentTab(new LayoutCreatorView(model, styleService, urlProvider), languageService);
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}

		return tab;
	}

	private LayoutCreatorContentTab findLayoutTab(String layout) {
		LayoutPersistenceObject retVal = null;
		try {
			retVal = new LayoutPersistenceObject();
			for (LayoutPersistenceObject obj : provider.getTextAdventureProject().getTextAdventure().getLayouts()) {
				if (obj.id().equals(layout)) {
					retVal = obj;
					break;
				}
			}
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
		
		LayoutCreatorModel model = new LayoutCreatorModel(retVal, languageService, dialogService, urlProvider, new SelectionManager());
		return new LayoutCreatorContentTab(new LayoutCreatorView(model, styleService, urlProvider), languageService);
	}
}
