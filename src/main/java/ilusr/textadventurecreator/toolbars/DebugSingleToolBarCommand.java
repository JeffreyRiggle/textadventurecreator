package ilusr.textadventurecreator.toolbars;

import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.ItemSelector;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import textadventurelib.persistence.GameStatePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugSingleToolBarCommand extends GameStateAwareButton {

	private final TextAdventureProvider provider;
	private final IDebugService debugService;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param debugService A @see DebugService to debug the current text adventure.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public DebugSingleToolBarCommand(TextAdventureProvider provider, 
									 IDebugService debugService, 
									 IDialogService dialogService,
									 ILanguageService languageService,
									 IStyleContainerService styleService,
									 InternalURLProvider urlProvider) {
		super(provider, null);
		this.provider = provider;
		this.debugService = debugService;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("DebugSingleCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.DEBUG_SINGLE)));
		
		super.setOnAction((e) -> {
			LogRunner.logger().info("Pressed Debug single toolbar item.");
			List<GameStatePersistenceObject> states = provider.getTextAdventureProject().getTextAdventure().gameStates();
			ItemSelector<GameStatePersistenceObject> selector = new ItemSelector<GameStatePersistenceObject>(states, languageService, styleService, urlProvider);
			selector.setSelectedAction((ev) -> {
				LogRunner.logger().info(String.format("Debugging game state %s", selector.selectedItem().stateId()));
				debugService.debugGameState(selector.selectedItem());
			});
			
			dialogService.displayModal(selector);
		});
		
		try {
			Image debugIco = new Image(AssetLoader.getResourceURL("DebugIcon.png").toExternalForm(), 16, 16, true, true);
			super.setGraphic(new ImageView(debugIco));
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.DEBUG_SINGLE));
		});
	}
}
