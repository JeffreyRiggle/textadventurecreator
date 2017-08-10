package ilusr.textadventurecreator.debug;

import javax.swing.JPanel;

import ilusr.gamestatemanager.GameStateManager;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MockedGameStateViewHost extends JPanel{

	private static final long serialVersionUID = 1L;
	private final String id;
	private final GameStateManager manager;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param id The id of the game state.
	 * @param manager A @see GameStateManager.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MockedGameStateViewHost(String id, GameStateManager manager, ILanguageService languageService) {
		this.id = id;
		this.manager = manager;
		this.languageService = languageService;
		init();
	}
	
	private void init() {
		final JFXPanel view = new JFXPanel();
		super.add(view);
		super.setVisible(true);
		
		Platform.runLater(() -> {
			initView(view);
		});
	}
	
	private void initView(JFXPanel view) {
		Scene scene = new Scene(new MockedGameStateView(id, manager, languageService), 800, 600);
		view.setScene(scene);
	}
}
