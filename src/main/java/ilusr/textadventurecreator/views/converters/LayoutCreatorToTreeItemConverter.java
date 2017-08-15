package ilusr.textadventurecreator.views.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.io.XMLDocumentUtilities;
import ilusr.core.javafx.ListItemConverter;
import ilusr.iroshell.services.ILayoutService;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.views.BluePrintNames;
import ilusr.textadventurecreator.views.GameExplorerModel;
import ilusr.textadventurecreator.views.layout.LayoutCreatorModel;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import textadventurelib.persistence.LayoutPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
@SuppressWarnings("rawtypes")
public class LayoutCreatorToTreeItemConverter implements ListItemConverter<LayoutCreatorModel, TreeItem>{

	private final Map<LayoutCreatorModel, TreeItem<String>> createdItems;
	private final GameExplorerModel model;
	private final ILayoutService layoutService;
	
	/**
	 * 
	 * @param model A @see GameExplorerModel to manage.
	 * @param layoutService A @see LayoutService to add and remove tabs.
	 */
	public LayoutCreatorToTreeItemConverter(GameExplorerModel model, ILayoutService layoutService) {
		createdItems = new HashMap<LayoutCreatorModel, TreeItem<String>>();
		this.model = model;
		this.layoutService = layoutService;
	}
	
	@Override
	public TreeItem convert(LayoutCreatorModel item) {
		if (createdItems.containsKey(item)) {
			return createdItems.get(item);
		}
		
		TreeItem<String> retVal = new TreeItem<String>(item.id().get());
		retVal.setGraphic(createRemoveButton(retVal, item));
		
		item.id().addListener((v, o, n) -> {
			retVal.valueProperty().set(n);
		});
		
		createdItems.put(item, retVal);
		return retVal;
	}
	
	private HBox createRemoveButton(TreeItem<String> treeItem, LayoutCreatorModel model) {
		HBox box = new HBox(5);
		Button removeButton = new Button();
		Button editButton = new Button();
		removeButton.getStylesheets().add(getClass().getResource("TreeItemRemove.css").toExternalForm());
		
		removeButton.setOnAction((e) -> {
			LogRunner.logger().info(String.format("Removing layout %s", model.id().get()));
			treeItem.getParent().getChildren().remove(treeItem);
			this.model.layouts().remove(model);
		});
		
		editButton.getStylesheets().add(getClass().getResource("TreeItemEdit.css").toExternalForm());
		editButton.setOnAction((e) -> {
			LogRunner.logger().info(String.format("Editing layout %s", model.id().get()));
			addLayoutView(model);
		});
		
		box.getChildren().add(removeButton);
		box.getChildren().add(editButton);
		
		return box;
	}
	
	private void addLayoutView(LayoutCreatorModel model) {
		try {
			XmlManager manager = new XmlManager(new String());
			XmlConfigurationManager config = new XmlConfigurationManager(manager, new ArrayList<PersistXml>());
			LayoutPersistenceObject layout = model.persistableLayout();
			layout.prepareXml();
			config.addConfigurationObject(layout);
			config.prepare();
			
			layoutService.addTab(BluePrintNames.LayoutCreator, XMLDocumentUtilities.convertDocumentToString(manager.document()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
