package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AppendTextViewProvider implements IViewProvider<BaseActionView> {

	private final AppendTextModel model;
	private AppendTextView view;
	
	/**
	 * 
	 * @param model A @see AppendTextModel to use to create the view.
	 */
	public AppendTextViewProvider(AppendTextModel model) {
		this.model = model;
	}

	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new AppendTextView(model);
		}
		
		return view;
	}
}
