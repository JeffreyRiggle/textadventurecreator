package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveActionViewProvider implements IViewProvider<BaseActionView> {

	private final SaveActionModel model;
	private SaveActionView view;
	
	/**
	 * 
	 * @param model A @see SaveActionModel to use to create the view.
	 */
	public SaveActionViewProvider(SaveActionModel model) {
		this.model = model;
	}
	
	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new SaveActionView(model);
		}
		
		return view;
	}

}
