package ilusr.textadventurecreator.views.trigger;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MultiTriggerViewProvider implements IViewProvider<BaseTriggerView> {

	private final MultiTriggerModel model;
	private MultiTriggerView view;
	
	/**
	 * 
	 * @param model A @see MultiTriggerModel to create a view for.
	 */
	public MultiTriggerViewProvider(MultiTriggerModel model) {
		this.model = model;
	}	
	
	@Override
	public BaseTriggerView getView() {
		if (view == null) {
			view = new MultiTriggerView(model);
		}
		
		return view;
	}
}
