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
public class RunGameMenuItem extends GameAwareMenuItem{

	private final TextAdventureProvider provider;
	private final IDebugService service;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param service A @see DebugService to debug the text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public RunGameMenuItem(TextAdventureProvider provider, 
						   IDebugService service,
						   ILanguageService languageService) {
		super(provider, languageService.getValue(DisplayStrings.RUN));
		this.provider = provider;
		this.service = service;
		this.languageService = languageService;
		
		initialize();
	}
	
	private void initialize() {
		super.setOnAction((e) -> {
			LogRunner.logger().info("Run -> Run Pressed.");
			service.runGame(provider.getTextAdventureProject().getTextAdventure());
		});
		
		try {
			Image runIco = new Image(AssetLoader.getResourceURL("Run.png").toExternalForm(), 16, 16, true, true);
			super.setGraphic(new ImageView(runIco));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		languageService.addListener(() -> {
			super.setText(languageService.getValue(DisplayStrings.RUN));
		});
	}
}
