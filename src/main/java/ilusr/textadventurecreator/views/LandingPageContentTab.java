package ilusr.textadventurecreator.views;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LandingPageContentTab extends TabContent {

	/**
	 * 
	 * @param view The @see LandingPageView to display.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LandingPageContentTab(LandingPageView view, ILanguageService languageService) {
		super.content().set(view);
		super.titleGraphic(languageService.getValue(DisplayStrings.GREETINGS));
		super.toolTip().set(languageService.getValue(DisplayStrings.WELCOMETOOLTIP));
		super.canClose().set(true);
		
		languageService.addListener(() -> {
			super.titleGraphic(languageService.getValue(DisplayStrings.GREETINGS));
			super.toolTip().set(languageService.getValue(DisplayStrings.WELCOMETOOLTIP));
		});
	}
}
