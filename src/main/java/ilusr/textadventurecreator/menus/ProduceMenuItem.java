package ilusr.textadventurecreator.menus;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.wizard.TextAdventureProjectManager;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProduceMenuItem extends GameAwareMenuItem {

	private final TextAdventureProvider provider;
	private final TextAdventureProjectManager manager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param manager A @see TextAdventureProjectManager to manage projects.
	 * @param languageService A @see LanguagService to provide display strings.
	 */
	public ProduceMenuItem(TextAdventureProvider provider, 
						   TextAdventureProjectManager manager,
						   ILanguageService languageService) {
		super(provider, languageService.getValue(DisplayStrings.PUBLISH));
		this.provider = provider;
		this.manager = manager;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		if (provider.getTextAdventureProject() != null) {
			updateMenuText(provider.getTextAdventureProject());
		}
		
		provider.addListener((p) -> {
			updateMenuText(p);
		});
		
		super.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, "File -> Publish|Build|Generate Pressed.");
			publish();
		});
		
		languageService.addListener(() -> {
			if (provider.getTextAdventureProject() != null) {
				updateMenuText(provider.getTextAdventureProject());
			} else {
				super.setText(languageService.getValue(DisplayStrings.PUBLISH));
			}
		});
	}
	
	private void publish() {
		manager.publish(provider.getTextAdventureProject(), super.parentPopupProperty().get().getOwnerWindow());
	}
	
	private void updateMenuText(TextAdventureProjectPersistence persistence) {
		if (!persistence.getIsStandAlone()) {
			super.setText(languageService.getValue(DisplayStrings.PUBLISH));
			return;
		}
		
		if (persistence.getIsDev()) {
			super.setText(languageService.getValue(DisplayStrings.BUILD));
			return;
		}
		
		super.setText(languageService.getValue(DisplayStrings.GENERATE));
	}
}
