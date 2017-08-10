package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExecutableActionViewProvider implements IViewProvider<BaseActionView> {

	private final ExecutionActionModel model;
	private ExecutionActionView view;
	
	/**
	 * 
	 * @param model A @see ExecutionActionModel to use to create the view.
	 */
	public ExecutableActionViewProvider(ExecutionActionModel model) {
		this.model = model;
	}
	
	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new ExecutionActionView(model);
		}
		
		return view;
	}

}
