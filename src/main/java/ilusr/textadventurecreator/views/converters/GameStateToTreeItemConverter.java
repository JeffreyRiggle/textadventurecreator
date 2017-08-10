package ilusr.textadventurecreator.views.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.io.XMLDocumentUtilities;
import ilusr.core.javafx.ListItemConverter;
import ilusr.iroshell.services.ILayoutService;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.GameExplorerModel;
import ilusr.textadventurecreator.views.gamestate.GameStateModel;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.GameStatePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
@SuppressWarnings("rawtypes")
public class GameStateToTreeItemConverter implements ListItemConverter<GameStateModel, TreeItem>{

	private final Map<GameStateModel, TreeItem<String>> createdItems;
	private final GameExplorerModel model;
	private final ILayoutService layoutService;
	
	/**
	 * 
	 * @param model A @see GameExplorerModel to manage.
	 * @param layoutService A @see LayouService to add and remove tabs.
	 */
	public GameStateToTreeItemConverter(GameExplorerModel model, ILayoutService layoutService) {
		createdItems = new HashMap<GameStateModel, TreeItem<String>>();
		this.model = model;
		this.layoutService = layoutService;
	}
	
	@Override
	public TreeItem convert(GameStateModel item) {
		if (createdItems.containsKey(item)) {
			return createdItems.get(item);
		}
		
		TreeItem<String> retVal = new TreeItem<String>(item.stateId().get());
		retVal.setGraphic(createRemoveButton(retVal, item));
		
		item.stateId().addListener((v, o, n) -> {
			retVal.valueProperty().set(n);
		});
		
		createdItems.put(item, retVal);
		return retVal;
	}
	
	private HBox createRemoveButton(TreeItem<String> treeItem, GameStateModel model) {
		HBox box = new HBox(5);
		Button removeButton = new Button();
		Button editButton = new Button();
		removeButton.getStylesheets().add(getClass().getResource("TreeItemRemove.css").toExternalForm());
		
		removeButton.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, String.format("Removing game state %s", model.stateId().get()));
			treeItem.getParent().getChildren().remove(treeItem);
			this.model.gameStates().remove(model);
		});
		
		editButton.getStylesheets().add(getClass().getResource("TreeItemEdit.css").toExternalForm());
		editButton.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing game state %s", model.stateId().get()));
			addGameStateView(model);
		});
		
		box.getChildren().add(removeButton);
		box.getChildren().add(editButton);
		
		return box;
	}
	
	private void addGameStateView(GameStateModel model) {
		try {
			XmlManager manager = new XmlManager(new String());
			XmlConfigurationManager config = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			GameStatePersistenceObject gameState = model.persistableGameState();
			gameState.prepareXml();
			config.addConfigurationObject(gameState);
			config.prepare();
			
			layoutService.addTab(BluePrintNames.GameState, XMLDocumentUtilities.convertDocumentToString(manager.document()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
