package ilusr.textadventurecreator.search;

import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object to create a search cell for.
 */
public class SearchListCellFactory<T> implements Callback<ListView<T>, ListCell<T>> {

	private IViewCreator<T> creator;
	private ilusr.core.interfaces.Callback<T> searchAction;
	
	/**
	 * 
	 * @param creator A @see IViewCreator to use to create display nodes.
	 * @param searchAction A @see core.interfaces.Callback to call when a cell is inspected.
	 */
	public SearchListCellFactory(IViewCreator<T> creator, ilusr.core.interfaces.Callback<T> searchAction) {
		this.creator = creator;
		this.searchAction = searchAction;
	}
	
	@Override
	public ListCell<T> call(ListView<T> arg0) {
		return new SearchListCell<T>(creator, searchAction);
	}

}
