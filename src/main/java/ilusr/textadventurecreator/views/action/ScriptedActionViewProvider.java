package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedActionViewProvider implements IViewProvider<BaseActionView> {

	private final ScriptedActionModel model;
	private ScriptedActionView view;
	
	/**
	 * 
	 * @param model The @see ScriptedActionModel to use to create the view.
	 */
	public ScriptedActionViewProvider(ScriptedActionModel model) {
		this.model = model;
	}
	
	@Override
	public BaseActionView getView() {
		if (view == null) {
			view = new ScriptedActionView(model);
		}
		
		return view;
	}

}