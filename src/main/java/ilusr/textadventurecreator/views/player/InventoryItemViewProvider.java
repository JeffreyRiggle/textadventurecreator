package ilusr.textadventurecreator.views.player;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.IViewProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.action.PlayerDataView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventoryItemViewProvider implements IViewProvider<PlayerDataView> {
	
	private final IDialogService service;
	private final InventoryItem item;
	private final ILanguageService languageService;
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private InventoryItemViewer view;

	/**
	 * 
	 * @param service A @see IDialogService to show dialogs.
	 * @param item A @see InventoryItem to create views for.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public InventoryItemViewProvider(IDialogService service, 
									 InventoryItem item, 
									 ILanguageService languageService,
									 IDialogProvider dialogProvider,
									 IStyleContainerService styleService,
									 InternalURLProvider urlProvider) {
		this.service = service;
		this.item = item;
		this.languageService = languageService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}

	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new InventoryItemViewer(service, item, languageService, dialogProvider, styleService, urlProvider);
		}
		
		return view;
	}
}
