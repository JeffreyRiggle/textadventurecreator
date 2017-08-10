package ilusr.textadventurecreator.views.player;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.IViewProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.PlayerDataView;
import textadventurelib.persistence.player.BodyPartPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartViewProvider implements IViewProvider<PlayerDataView>{

	private final BodyPartPersistenceObject bodyPart;
	private final IDialogService dialogService;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private BodyPartViewer view;
	
	/**
	 * 
	 * @param bodyPart A @see BodyPartPersistenceObject to provide a view for.
	 * @param dialogService A @see IDialogService to show dialogs.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public BodyPartViewProvider(BodyPartPersistenceObject bodyPart, 
								IDialogService dialogService, 
								ILanguageService languageService,
								IDialogProvider dialogProvider,
								IStyleContainerService styleService,
								InternalURLProvider urlProvider) {
		this.bodyPart = bodyPart;
		this.dialogService = dialogService;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new BodyPartViewer(new BodyPartModel(bodyPart, null, dialogService, languageService, dialogProvider, styleService, urlProvider),
					languageService, styleService, urlProvider);
		}
		
		return view;
	}

}
