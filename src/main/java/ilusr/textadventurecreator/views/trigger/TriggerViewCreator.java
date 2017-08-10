package ilusr.textadventurecreator.views.trigger;

import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerViewCreator implements IViewCreator<TriggerPersistenceObject> {

	@Override
	public Node create(TriggerPersistenceObject input) {
		Node retVal;
		
		if (input instanceof TextTriggerPersistenceObject) {
			retVal = createTextTriggerView((TextTriggerPersistenceObject)input);
		} else if (input instanceof PlayerTriggerPersistenceObject) {
			retVal = createPlayerTriggerView((PlayerTriggerPersistenceObject)input);
		} else if (input instanceof ScriptedTriggerPersistenceObject) {
			retVal = createScriptedTriggerView((ScriptedTriggerPersistenceObject)input);
		} else if (input instanceof MultiPartTriggerPersistenceObject) {
			retVal = createMultiPartTrigger((MultiPartTriggerPersistenceObject)input);
		} else {
			retVal = new Label("Unknown Trigger");
		}
		return retVal;
	}

	private Node createTextTriggerView(TextTriggerPersistenceObject trigger) {
		Label retVal = new Label();
		retVal.textProperty().set("Text Trigger: " + trigger.text());
		return retVal;
	}
	
	private Node createPlayerTriggerView(PlayerTriggerPersistenceObject trigger) {
		Label retVal = new Label();
		retVal.textProperty().set("Player Trigger: " + trigger.playerName());
		return retVal;
	}
	
	private Node createScriptedTriggerView(ScriptedTriggerPersistenceObject trigger) {
		Label retVal = new Label();
		retVal.textProperty().set("Scripted Trigger");
		return retVal;
	}
	
	private Node createMultiPartTrigger(MultiPartTriggerPersistenceObject trigger) {
		AnchorPane pane = new AnchorPane();
		GridPane grid = new GridPane();
		grid.setVgap(10);
		int index = 0;
		for (TriggerPersistenceObject trig : trigger.triggers()) {
			grid.addRow(index, create(trig));
			index++;
		}
		pane.getChildren().add(grid);
		AnchorPane.setBottomAnchor(grid, 0.0);
		AnchorPane.setTopAnchor(grid, 0.0);
		AnchorPane.setLeftAnchor(grid, 0.0);
		AnchorPane.setRightAnchor(grid, 0.0);
		
		return new TitledPane("Multi-Part Trigger", pane);
	}
}
