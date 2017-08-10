package ilusr.textadventurecreator.views.gamestate;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateContentTab extends TabContent {
	
	private final String GAME_STATE_NAME_PERSISTENCE = "GameStateName: ";
	private final ILanguageService languageService;
	private GameStateView view;
	
	/**
	 * 
	 * @param view A @see GameStateView to display.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public GameStateContentTab(GameStateView view, ILanguageService languageService) {
		this.view = view;
		this.languageService = languageService;
		super.content().set(view);
		super.titleGraphic(languageService.getValue(DisplayStrings.GAME_STATE));
		super.toolTip().set(languageService.getValue(DisplayStrings.GAME_STATE_INFO));
		super.canClose().set(true);
		
		updateDisplayStrings();
		initialize();
	}
	
	private void initialize() {
		if (view.model().stateId().get() != null && !view.model().stateId().get().isEmpty()) {
			super.customData().set(GAME_STATE_NAME_PERSISTENCE + view.model().stateId().get());
		}
		
		view.model().stateId().addListener((v, o, n) -> {
			super.titleGraphic(String.format("%s %s", languageService.getValue(DisplayStrings.GAME_STATE), n));
			super.toolTip().set(String.format("%s %s", languageService.getValue(DisplayStrings.GAME_STATE_INFO), n));
			super.customData().set(GAME_STATE_NAME_PERSISTENCE + n);
		});
		
		languageService.addListener(() -> {
			updateDisplayStrings();
		});
	}
	
	private void updateDisplayStrings() {
		String stateId = view.model().stateId().get();
		
		if (stateId != null) {
			super.titleGraphic(String.format("%s %s", languageService.getValue(DisplayStrings.GAME_STATE), stateId));
			super.toolTip().set(String.format("%s %s", languageService.getValue(DisplayStrings.GAME_STATE_INFO), stateId));
		}
	}

}
