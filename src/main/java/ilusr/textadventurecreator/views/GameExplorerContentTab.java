package ilusr.textadventurecreator.views;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameExplorerContentTab extends TabContent {

	/**
	 * 
	 * @param view The @see GameExplorerView to display in this tab.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public GameExplorerContentTab(GameExplorerView view, ILanguageService languageService) {
		super.content().set(view);
		super.titleGraphic(languageService.getValue(DisplayStrings.EXPLORER));
		super.toolTip().set(languageService.getValue(DisplayStrings.GAME_EXPLORER));
		super.canClose().set(true);
		
		languageService.addListener(() -> {
			super.titleGraphic(languageService.getValue(DisplayStrings.EXPLORER));
			super.toolTip().set(languageService.getValue(DisplayStrings.GAME_EXPLORER));
		});
	}
}
