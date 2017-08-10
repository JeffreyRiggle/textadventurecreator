package ilusr.textadventurecreator.views.gamestate;

import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import textadventurelib.persistence.CompletionTimerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionTimerViewCreator implements IViewCreator<CompletionTimerPersistenceObject>{

	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CompletionTimerViewCreator(ILanguageService languageService) {
		this.languageService = languageService;
	}
	
	@Override
	public Node create(CompletionTimerPersistenceObject input) {
		CompletionTimerModel model = new CompletionTimerModel(input, languageService);
		CompletionTimerView viewer = new CompletionTimerView(model);
		TitledPane pane = new TitledPane(input.toString(), viewer);
		return pane;
	}

}
