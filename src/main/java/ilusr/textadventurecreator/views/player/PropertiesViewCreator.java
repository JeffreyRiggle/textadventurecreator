package ilusr.textadventurecreator.views.player;

import java.util.HashMap;
import java.util.Map;

import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PropertiesViewCreator implements IViewCreator<PropertyPersistenceObject> {
	
	private final ILanguageService languageService;
	private Map<PropertyPersistenceObject, Node> properties;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PropertiesViewCreator(ILanguageService languageService) {
		this.languageService = languageService;
		properties = new HashMap<PropertyPersistenceObject, Node>();
	}
	
	@Override
	public Node create(PropertyPersistenceObject input) {
		if (properties.containsKey(input)) {
			return properties.get(input);
		}
		
		NamedPersistenceObjectModel model = new NamedPersistenceObjectModel(input);
		PropertyViewer viewer = new PropertyViewer(model, languageService);
		
		TitledPane pane = new TitledPane(input.toString(), viewer);
		pane.textProperty().bind(model.name());
		properties.put(input, pane);
		return pane;
	}
}
