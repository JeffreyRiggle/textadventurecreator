package ilusr.textadventurecreator.search;

import java.util.List;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object to find.
 */
public abstract class BaseFinderModel<T> {
	
	private SelectionAwareObservableList<String> fields;
	private SelectionAwareObservableList<String> scope;
	private SelectionAwareObservableList<T> results;
	private SimpleStringProperty searchText;
	private LanguageAwareString helpText;
	
	/**
	 * 
	 * @param fields A list of fields to search against.
	 * @param scopes A list of scopes to search in.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public BaseFinderModel(List<String> fields, List<String> scopes, ILanguageService languageService) {
		this.fields = new SelectionAwareObservableList<String>();
		this.fields.list().addAll(fields);
		
		scope = new SelectionAwareObservableList<String>();
		scope.list().addAll(scopes);
		results = new SelectionAwareObservableList<T>();
		searchText = new SimpleStringProperty();
		helpText = new LanguageAwareString(languageService, DisplayStrings.SEARCH_HELP);
		
		initialize();
		bind();
	}
	
	private void initialize() {
		fields.selected().set(fields.list().get(0));
		scope.selected().set(scope.list().get(0));
	}
	
	private void bind() {
		searchText.addListener((v, o, n) -> {
			search(n);
		});
	}
	
	/**
	 * 
	 * @param text The text to search for.
	 */
	protected abstract void search(String text);
	
	/**
	 * 
	 * @param item The item to inspect.
	 */
	protected abstract void inspect(T item);
	
	/**
	 * 
	 * @return A display string for help.
	 */
	public SimpleStringProperty helpText() {
		return helpText;
	}
	
	/**
	 * 
	 * @return A display string for search.
	 */
	public SimpleStringProperty searchText() {
		return searchText;
	}
	
	/**
	 * 
	 * @return The fields.
	 */
	public SelectionAwareObservableList<String> fields() {
		return fields;
	}
	
	/**
	 * 
	 * @return The scopes.
	 */
	public SelectionAwareObservableList<String> scope() {
		return scope;
	}
	
	/**
	 * 
	 * @return The value that has been found and selected in this search.
	 */
	public T foundValue() {
		return results.selected().get();
	}
	
	/**
	 * 
	 * @return All values that have been found in this search.
	 */
	public SelectionAwareObservableList<T> results() {
		return results;
	}
}
