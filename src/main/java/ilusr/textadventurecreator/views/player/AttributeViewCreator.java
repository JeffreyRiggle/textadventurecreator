package ilusr.textadventurecreator.views.player;

import java.util.HashMap;
import java.util.Map;

import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import textadventurelib.persistence.player.AttributePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AttributeViewCreator implements IViewCreator<AttributePersistenceObject> {
	
	private final ILanguageService languageService;
	private Map<AttributePersistenceObject, Node> attributeMap;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public AttributeViewCreator(ILanguageService languageService) {
		this.languageService = languageService;
		attributeMap = new HashMap<AttributePersistenceObject, Node>();
	}
	
	@Override
	public Node create(AttributePersistenceObject input) {
		if (attributeMap.containsKey(input)) {
			return attributeMap.get(input);
		}
		
		NamedPersistenceObjectModel model = new NamedPersistenceObjectModel(input);
		AttributeViewer viewer = new AttributeViewer(model, languageService);
		TitledPane pane = new TitledPane(input.toString(), viewer);
		pane.textProperty().bind(model.name());
		
		attributeMap.put(input, pane);
		return pane;
	}
}
