package ilusr.textadventurecreator.menus;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.debug.IDebugService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugMenuItem extends GameAwareMenuItem {

	private final TextAdventureProvider provider;
	private final IDebugService service;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param service A @see DebugService to debug the text adventure with.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public DebugMenuItem(TextAdventureProvider provider,
					     IDebugService service,
					     ILanguageService languageService) {
		super(provider, languageService.getValue(DisplayStrings.DEBUG));
		this.provider = provider;
		this.service = service;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("Run -> Debug Pressed.");
			service.debugGame(provider.getTextAdventureProject().getTextAdventure());
		});
		
		languageService.addListener(() -> {
			super.textProperty().set(languageService.getValue(DisplayStrings.DEBUG));
		});
		
		try {
			Image debugIco = new Image(AssetLoader.getResourceURL("DebugManyIcon.png").toExternalForm(), 16, 16, true, true);
			super.setGraphic(new ImageView(debugIco));
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
}
