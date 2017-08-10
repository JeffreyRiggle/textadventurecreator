package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;
import ilusr.textadventurecreator.language.ILanguageService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class RemovePlayerPropertyViewProvider implements IViewProvider<PlayerDataView> {

	private final ILanguageService languageService;
	private RemovePlayerPropertyView view;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public RemovePlayerPropertyViewProvider(ILanguageService languageService) {
		this.languageService = languageService;
	}
	
	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new RemovePlayerPropertyView(languageService);
		}
		
		return view;
	}

}
