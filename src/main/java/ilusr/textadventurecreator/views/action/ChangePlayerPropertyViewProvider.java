package ilusr.textadventurecreator.views.action;

import ilusr.iroshell.core.IViewProvider;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ChangePlayerPropertyViewProvider implements IViewProvider<PlayerDataView> {
	
	private ChangePlayerPropertyView view;
	private String initialData;
	
	/**
	 * 
	 * @param initialData The initial data for the view.
	 */
	public ChangePlayerPropertyViewProvider(String initialData) {
		this.initialData = initialData;
	}

	@Override
	public PlayerDataView getView() {
		if (view == null) {
			view = new ChangePlayerPropertyView(initialData);
		}
		
		return view;
	}
}
