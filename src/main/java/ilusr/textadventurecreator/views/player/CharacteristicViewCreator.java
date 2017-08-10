package ilusr.textadventurecreator.views.player;

import java.util.HashMap;
import java.util.Map;

import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CharacteristicViewCreator implements IViewCreator<CharacteristicPersistenceObject> {

	private final ILanguageService languageService;
	private Map<CharacteristicPersistenceObject, Node> characteristics;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public CharacteristicViewCreator(ILanguageService languageService) {
		characteristics = new HashMap<CharacteristicPersistenceObject, Node>();
		this.languageService = languageService;
	}
	
	@Override
	public Node create(CharacteristicPersistenceObject input) {
		if (characteristics.containsKey(input)) {
			return characteristics.get(input);
		}
		
		NamedPersistenceObjectModel model = new NamedPersistenceObjectModel(input);
		CharacteristicViewer viewer = new CharacteristicViewer(model, languageService);
		
		TitledPane pane = new TitledPane(input.toString(), viewer);
		pane.textProperty().bind(model.name());
		characteristics.put(input, pane);
		
		return pane;
	}
}
