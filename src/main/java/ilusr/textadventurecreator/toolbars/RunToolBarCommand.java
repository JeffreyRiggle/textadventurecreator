package ilusr.textadventurecreator.toolbars;

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
public class RunToolBarCommand extends GameStateAwareButton {

	private final TextAdventureProvider provider;
	private final IDebugService service;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param service A @see DebugService to debug the current game.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public RunToolBarCommand(TextAdventureProvider provider, 
							 IDebugService service,
							 ILanguageService languageService) {
		super(provider, null);
		this.provider = provider;
		this.service = service;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("RunCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.RUN)));
		
		super.setOnAction((e) -> {
			LogRunner.logger().info("Run game toolbar item pressed.");
			service.runGame(provider.getTextAdventureProject().getTextAdventure());
		});
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.RUN));
		});
	}
}
