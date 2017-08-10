package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class FinishActionViewProvider implements IViewProvider<BaseActionView> {

	private FinishActionModel model;
	private FinishActionView view;
	
	/**
	 * 
	 * @param model A @see FinishActionModel to use to create the view.
	 */
	public FinishActionViewProvider(FinishActionModel model) {
		this.model = model;
	}
	
	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new FinishActionView(model);
		}
		
		return view;
	}
}
