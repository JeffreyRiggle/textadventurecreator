package ilusr.textadventurecreator.library;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;

import ilusr.core.datamanager.xml.XmlGenerator;
import ilusr.core.datamanager.xml.XmlInputReader;
import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.ioc.ServiceManager;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.ITabContent;
import ilusr.iroshell.services.ITabContentBluePrint;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.ActionViewFactory;
import ilusr.textadventurecreator.views.action.PlayerModProviderFactory;
import ilusr.textadventurecreator.views.trigger.TriggerViewFactory;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryItemBluePrint implements ITabContentBluePrint {

	private final String LIBRARY_ITEM_PERSISTENCE = "LibraryItemName: ";
	private final IDialogService dialogService;
	private final TriggerViewFactory triggerFactory;
	private final ActionViewFactory actionFactory;
	private final PlayerModProviderFactory playerModFactory;
	private final ILanguageService languageService;
	private final LibraryService libraryService;
	private final InternalURLProvider urlProvider;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	
	/**
	 * 
	 * @param dialogService A @see IDialogService to display stages.
	 * @param triggerFactory A @see TriggerViewFactory to create trigger views.
	 * @param actionFactory A @see ActionViewFactory to create action views.
	 * @param playerModFactory A @see PlayerModProviderFactory to create player mod views.
	 * @param languageService A @see LanguageService to create display strings.
	 * @param libraryService A @see LibraryService to register with.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 * @param dialogProvider A @see IDialogProvider to provide dialogs.
	 * @param styleService service to manage styles.
	 */
	public LibraryItemBluePrint(IDialogService dialogService,
						        TriggerViewFactory triggerFactory, 
						        ActionViewFactory actionFactory,
						        PlayerModProviderFactory playerModFactory,
						        ILanguageService languageService,
						        LibraryService libraryService,
						        InternalURLProvider urlProvider,
						        IDialogProvider dialogProvider,
						        IStyleContainerService styleService) {
		this.dialogService = dialogService;
		this.triggerFactory = triggerFactory;
		this.actionFactory = actionFactory;
		this.playerModFactory = playerModFactory;
		this.languageService = languageService;
		this.libraryService = libraryService;
		this.urlProvider = urlProvider;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
	}
	
	@Override
	public ITabContent create() {
		LogRunner.logger().log(Level.INFO, "Creating new library item view.");
		return ServiceManager.getInstance().<LibraryItemContentTab>get("LibraryItemContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		LogRunner.logger().log(Level.INFO, String.format("Creating new library item view with data: %s", customData));
		LibraryItemContentTab tab = null;
		
		if (customData.startsWith(LIBRARY_ITEM_PERSISTENCE)) {
			return findLibraryInfoTab(customData.substring(LIBRARY_ITEM_PERSISTENCE.length()));
		}
		
		try {
			InputStream stream = new ByteArrayInputStream(customData.getBytes(StandardCharsets.UTF_8));
			XmlInputReader reader = new XmlInputReader(stream);
			XmlManager manager = new XmlManager(new String(), new XmlGenerator(), reader);
			XmlConfigurationManager configurationManager = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			configurationManager.load();
			
			LibraryItem item = new LibraryItem((XmlConfigurationObject)configurationManager.configurationObjects().get(0));
			
			tab = new LibraryItemContentTab(new LibraryItemView(new LibraryItemModel(dialogService, item, triggerFactory, actionFactory, playerModFactory, languageService, urlProvider, ServiceManager.getInstance(), dialogProvider, styleService), languageService, styleService, urlProvider), languageService);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tab;
	}

	private LibraryItemContentTab findLibraryInfoTab(String item) {
		LibraryItem lItem = new LibraryItem();
		String[] values = item.split(";=;");
		
		if (values.length != 2) {
			return new LibraryItemContentTab(new LibraryItemView(new LibraryItemModel(dialogService, lItem, triggerFactory, actionFactory, playerModFactory, languageService, urlProvider, ServiceManager.getInstance(), dialogProvider, styleService), languageService, styleService, urlProvider), languageService);
		}
		
		for (LibraryItem itm : libraryService.getItems()) {
			if (itm.getLibraryName().equals(values[0]) && itm.getAuthor().equals(values[1])) {
				lItem = itm;
				break;
			}
		}
		
		return new LibraryItemContentTab(new LibraryItemView(new LibraryItemModel(dialogService, lItem, triggerFactory, actionFactory, playerModFactory, languageService, urlProvider, ServiceManager.getInstance(), dialogProvider, styleService), languageService, styleService, urlProvider), languageService);
	}
}
