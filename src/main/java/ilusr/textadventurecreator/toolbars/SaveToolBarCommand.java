package ilusr.textadventurecreator.toolbars;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import javafx.scene.control.Tooltip;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveToolBarCommand extends GameStateAwareButton {

	private final TextAdventureProvider provider;
	private final ProjectPersistenceManager persistenceManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param persistenceManager A @see ProjectPersistenceManager to save the current project.
	 * @param provider A @see TextAdventureProvider to provide text adventure games.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public SaveToolBarCommand(ProjectPersistenceManager persistenceManager, 
							  TextAdventureProvider provider,
							  ILanguageService languageService) {
		super(provider, null);
		this.persistenceManager = persistenceManager;
		this.provider = provider;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.getStylesheets().add(getClass().getResource("SaveCommand.css").toExternalForm());
		super.tooltipProperty().set(new Tooltip(languageService.getValue(DisplayStrings.SAVE)));
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "Save toolbar item pressed.");
			persistenceManager.saveAsync(provider.getTextAdventureProject());
		});
		
		languageService.addListener(() -> {
			super.getTooltip().setText(languageService.getValue(DisplayStrings.SAVE));
		});
	}
}
