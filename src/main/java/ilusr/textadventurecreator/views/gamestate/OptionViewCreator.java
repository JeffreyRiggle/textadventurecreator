package ilusr.textadventurecreator.views.gamestate;

import ilusr.textadventurecreator.views.IViewCreator;
import javafx.scene.Node;
import javafx.scene.control.Label;
import textadventurelib.persistence.ActionPersistenceObject;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.FinishActionPersistenceObject;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class OptionViewCreator implements IViewCreator<OptionPersistenceObject>{

	@Override
	public Node create(OptionPersistenceObject input) {
		return new Label("Option: " + getActionString(input.action()));
	}

	private String getActionString(ActionPersistenceObject action) {
		if (action instanceof AppendTextActionPersistence) {
			return "Append: " + ((AppendTextActionPersistence)action).appendText();
		} else if (action instanceof CompletionActionPersistence) {
			return "Complete: " + ((CompletionActionPersistence)action).completionData();
		} else if (action instanceof ExecutionActionPersistence) {
			return "Execute: " + ((ExecutionActionPersistence)action).executable();
		} else if (action instanceof ModifyPlayerActionPersistence) {
			return "Mod Player " + ((ModifyPlayerActionPersistence)action).playerName();
		} else if (action instanceof SaveActionPersistenceObject) {
			return "Save Action";
		} else if (action instanceof ScriptedActionPersistenceObject) {
			return "Script Action";
		} else if (action instanceof FinishActionPersistenceObject) {
			return "Finish Action";
		}
		
		return action.toString();
	}
}
