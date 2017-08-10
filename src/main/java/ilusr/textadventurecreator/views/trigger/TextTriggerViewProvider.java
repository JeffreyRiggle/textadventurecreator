package ilusr.textadventurecreator.views.trigger;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextTriggerViewProvider implements IViewProvider<BaseTriggerView> {

	private final TextTriggerModel model;
	private TextTriggerView view;
	
	/**
	 * 
	 * @param model The model to create a view for.
	 */
	public TextTriggerViewProvider(TextTriggerModel model) {
		this.model = model;
	}
	
	
	@Override
	public BaseTriggerView getView() {
		if (view == null) {
			view = new TextTriggerView(model);
		}
		
		return view;
	}

}
