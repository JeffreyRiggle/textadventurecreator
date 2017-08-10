package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionActionViewProvider implements IViewProvider<BaseActionView> {

	private final CompletionActionModel model;
	private CompletionActionView view;
	
	/**
	 * 
	 * @param model A @see CompletionActionModel to use to create the view.
	 */
	public CompletionActionViewProvider(CompletionActionModel model) {
		this.model = model;
	}

	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new CompletionActionView(model);
		}
		
		return view;
	}
}
