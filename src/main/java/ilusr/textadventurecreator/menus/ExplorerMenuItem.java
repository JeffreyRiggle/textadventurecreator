package ilusr.textadventurecreator.menus;

import java.util.List;

import ilusr.iroshell.dockarea.ICloseable;
import ilusr.iroshell.services.ILayoutService;
import ilusr.iroshell.services.ITabContent;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.IProviderListener;
import ilusr.textadventurecreator.shell.LayoutApplicationService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.BluePrintNames;
import javafx.scene.control.CheckMenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExplorerMenuItem extends CheckMenuItem implements IProviderListener, ICloseable {

	private final TextAdventureProvider provider;
	private final ILayoutService layoutService;
	private final ILanguageService languageService;
	private final LayoutApplicationService layoutApplicationService;
	private String tabId;
	private ITabContent tabContent;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param layoutService A @see ILayoutService to add tabs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param layoutApplicationService A @see LayoutApplicationService to listen for layout application.
	 */
	public ExplorerMenuItem(TextAdventureProvider provider, 
							ILayoutService layoutService,
							ILanguageService languageService,
							LayoutApplicationService layoutApplicationService) {
		super(languageService.getValue(DisplayStrings.EXPLORER));
		this.provider = provider;
		this.layoutService = layoutService;
		this.languageService = languageService;
		this.layoutApplicationService = layoutApplicationService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("View -> Explorer Pressed.");
			clicked();
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.EXPLORER));
		});
		
		super.setDisable(provider.getTextAdventureProject() == null);
		provider.addListener(this);
		
		if (provider.getTextAdventureProject() != null) {
			LogRunner.logger().info("Text adventure is not null setting up explorer.");
			setupTabFromLayout();
		}
		
		layoutApplicationService.addListener(() -> {
			setupTabFromLayout();
		});
	}

	private void clicked() {
		if (tabId != null && !tabId.isEmpty()) {
			LogRunner.logger().info("Toggling off Explorer. Explorer view will be removed.");
			layoutService.removeTab(tabId);
			tabId = null;
			super.setSelected(false);
			return;
		}
		
		LogRunner.logger().info("Toggling on Explorer. Explorer will be added.");
		tabId = layoutService.addTab(BluePrintNames.Explorer);
		tabContent = layoutService.getTabContent(tabId);
		tabContent.addCloseListener(this);
		super.setSelected(true);
	}
	
	@Override
	public void provided(TextAdventureProjectPersistence textAdventure) {
		super.setDisable(textAdventure == null);
	}
	
	private void setupTabFromLayout() {
		List<String> tabs = layoutService.getTabIds(BluePrintNames.Explorer);
		if (tabs.size() > 0) {
			tabId = tabs.get(0);
			tabContent = layoutService.getTabContent(tabId);
			tabContent.addCloseListener(this);
			super.setSelected(true);
		}
	}

	@Override
	public void close() {
		LogRunner.logger().info("Toggling off Explorer. Explorer tab closed.");
		tabId = null;
		tabContent.removeCloseListener(this);
		tabContent = null;
		super.setSelected(false);
	}
}
