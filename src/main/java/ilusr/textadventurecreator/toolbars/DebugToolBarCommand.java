package ilusr.textadventurecreator.toolbars;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.scene.control.Tooltip;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugToolBarCommand extends GameStateAwareButton {

	private final TextAdventureProvider provider;
	private final IDebugService debugService;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param debugService A @see DebugService to debug games.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public DebugToolBarCommand(TextAdventureProvider provider, 
							   IDebugService debugService,
							   ILanguageService languageService) {
		super(provider, null);
		this.provider = provider;
		this.debugService = debugService;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("DebugCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.DEBUG)));
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "ToolBar item for debug game pressed.");
			debugService.debugGame(provider.getTextAdventureProject().getTextAdventure());
		});
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.DEBUG));
		});
	}
}
