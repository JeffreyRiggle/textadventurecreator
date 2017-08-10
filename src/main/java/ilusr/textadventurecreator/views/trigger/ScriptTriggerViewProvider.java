package ilusr.textadventurecreator.views.trigger;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptTriggerViewProvider implements IViewProvider<BaseTriggerView> {

	private final ScriptedTriggerModel model;
	private ScriptedTriggerView view;
	
	/**
	 * 
	 * @param model The model to create a view for.
	 */
	public ScriptTriggerViewProvider(ScriptedTriggerModel model) {
		this.model = model;
	}
	
	
	@Override
	public BaseTriggerView getView() {
		if (view == null) {
			view = new ScriptedTriggerView(model);
		}
		
		return view;
	}

}
