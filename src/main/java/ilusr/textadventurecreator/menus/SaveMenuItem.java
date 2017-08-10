package ilusr.textadventurecreator.menus;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProvider;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveMenuItem extends GameAwareMenuItem {

	private final TextAdventureProvider provider;
	private final ProjectPersistenceManager persistenceManager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param persistenceManager A @see ProjectPersistenceManager to persist the project.
	 * @param provider A @see TextAdventureProvider to get the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public SaveMenuItem(ProjectPersistenceManager persistenceManager, 
						TextAdventureProvider provider,
						ILanguageService languageService) {
		super(provider, languageService.getValue(DisplayStrings.SAVE));
		this.persistenceManager = persistenceManager;
		this.provider = provider;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "File -> Save Pressed.");
			save();
		});
		
		languageService.addListener(() -> {
			super.setText(languageService.getValue(DisplayStrings.SAVE));
		});
	}
	
	private void save() {
		persistenceManager.saveAsync(provider.getTextAdventureProject());
	}
}
