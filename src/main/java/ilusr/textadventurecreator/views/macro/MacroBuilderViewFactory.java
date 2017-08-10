package ilusr.textadventurecreator.views.macro;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MacroBuilderViewFactory {

	private final TextAdventureProvider provider;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public MacroBuilderViewFactory(TextAdventureProvider provider, 
								   ILanguageService languageService,
								   IStyleContainerService styleService,
								   InternalURLProvider urlProvider) {
		this.provider = provider;
		this.languageService = languageService;
		this.urlProvider = urlProvider;
		this.styleService = styleService;
	}
	
	/**
	 * 
	 * @return A newly created @see MacroBuilderView.
	 */
	public MacroBuilderView build() {
		MacroBuilderModel model = new MacroBuilderModel(provider.getTextAdventureProject().getTextAdventure().players(), languageService);
		return new MacroBuilderView(model, styleService, urlProvider);
	}
}
