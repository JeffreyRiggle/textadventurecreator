package ilusr.textadventurecreator.codegen;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.StatusItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProjectBuilder {

	private final TextAdventureProjectPersistence persistence;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param persistence A @see TextAdventureProjectPersistence representing the game to build.
	 * @param languageService A @see LanaugageService used to localize display strings.
	 */
	public ProjectBuilder(TextAdventureProjectPersistence persistence, ILanguageService languageService) {
		this.persistence = persistence;
		this.languageService = languageService;
	}
	
	/**
	 * Builds the project and updates a status item.
	 * 
	 * @param item A @see StatusItem to update as the build process occurs.
	 */
	public void build(StatusItem item) {
		if (!persistence.getIsStandAlone()) {
			LogRunner.logger().log(Level.INFO, "Non-standalone game not building.");
			return;
		}
		
		IProjectBuilder builder = null;
		
		if (!persistence.getIsDev()) {
			LogRunner.logger().log(Level.INFO, "Non-dev game building as a java project.");
			builder = new JavaProjectBuilder(persistence, languageService);
			builder.build(item);
			return;
		}
		
		switch (persistence.getLanguage()) {
			case "Java":
				LogRunner.logger().log(Level.INFO, "Building as a java project.");
				builder = new JavaProjectBuilder(persistence, languageService);
				break;
			case "C++":
			case "HTML":
				LogRunner.logger().log(Level.INFO, "Unitended language selected.");
				//TODO:
				break;
		}
		
		if (builder != null) {
			builder.build(item);
		}
	}
}
