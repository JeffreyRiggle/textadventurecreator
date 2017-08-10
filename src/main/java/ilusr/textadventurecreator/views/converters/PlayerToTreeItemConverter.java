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
import ilusr.textadventurecreator.views.player.PlayerModel;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
@SuppressWarnings("rawtypes")
public class PlayerToTreeItemConverter implements ListItemConverter<PlayerModel, TreeItem>{

	private final Map<PlayerModel, TreeItem<String>> createdItems;
	private final GameExplorerModel model;
	private final ILayoutService layoutService;
	
	/**
	 * 
	 * @param model A @see GameExplorerModel to manage.
	 * @param layoutService A @see LayoutService to add and remove tabs.
	 */
	public PlayerToTreeItemConverter(GameExplorerModel model, ILayoutService layoutService) {
		this.model = model;
		createdItems = new HashMap<PlayerModel, TreeItem<String>>();
		this.layoutService = layoutService;
	}
	
	@Override
	public TreeItem convert(PlayerModel item) {
		if (createdItems.containsKey(item)) {
			return createdItems.get(item);
		}
		
		TreeItem<String> retVal = new TreeItem<String>(item.playerID().get());
		retVal.graphicProperty().set(createRemoveButton(retVal, item));
		
		item.playerID().addListener((v, o, n) -> {
			retVal.valueProperty().set(n);
		});
		
		createdItems.put(item, retVal);
		return retVal;
	}
	
	private HBox createRemoveButton(TreeItem<String> treeItem, PlayerModel model) {
		HBox box = new HBox(5);
		Button removeButton = new Button();
		Button editButton = new Button();
		removeButton.getStylesheets().add(getClass().getResource("TreeItemRemove.css").toExternalForm());
		
		removeButton.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, String.format("Removing player %s", model.playerID().get()));
			treeItem.getParent().getChildren().remove(treeItem);
			this.model.players().remove(model);
		});
		
		editButton.getStylesheets().add(getClass().getResource("TreeItemEdit.css").toExternalForm());
		editButton.setOnAction((e) -> {
			LogRunner.logger().log(Level.INFO, String.format("Editing player %s", model.playerID().get()));
			addPlayerView(model);
		});
		
		box.getChildren().add(removeButton);
		box.getChildren().add(editButton);
		
		return box;
	}
	
	private void addPlayerView(PlayerModel model) {
		try {
			XmlManager manager = new XmlManager(new String());
			XmlConfigurationManager config = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			PlayerPersistenceObject player = model.persistablePlayer();
			player.prepareXml();
			config.addConfigurationObject(model.persistablePlayer());
			config.prepare();
			
			layoutService.addTab(BluePrintNames.Player, XMLDocumentUtilities.convertDocumentToString(manager.document()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
