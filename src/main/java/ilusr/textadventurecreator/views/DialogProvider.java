package ilusr.textadventurecreator.views;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.scene.Node;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DialogProvider implements IDialogProvider {

	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	/**
	 * 
	 * @param languageService A @see LanguageService for display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public DialogProvider(ILanguageService languageService,
						  IStyleContainerService styleService,
						  InternalURLProvider urlProvider) {
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
	}
	
	@Override
	public Dialog create(Node node) {
		return create(node, null);
	}

	@Override
	public Dialog create(Node node, Runnable completionAction) {
		return create(node, completionAction, null);
	}
	
	@Override
	public Dialog create(Node node, Runnable completionAction, Runnable cancelAction) {
		Dialog retVal = new Dialog(node, languageService, styleService, urlProvider);
		
		if (completionAction != null) {
			retVal.setOnComplete(completionAction);
		}
		
		if (cancelAction != null) {
			retVal.setOnCancel(cancelAction);
		}
		
		return retVal;
	}
}
