import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class OptionView extends BasePage {

    OptionView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public TriggerView addTrigger() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        Node optionList = waitForListView("#triggers");
        robot.clickOn(waitForButton("#listCellAdd", optionList));
        Node popup = waitForWindow(currentWindows);
        return new TriggerView(robot, popup);
    }

    public OptionView setAction(String action) throws Exception {
        setComboBySelector("#actionType", action);
        return this;
    }

    public OptionView setCompletionData(String data) throws Exception {
        robot.clickOn(waitForTextField("#completionData")).write(data);
        return this;
    }

    public void ok() throws Exception {
        robot.clickOn(waitForButton("Ok"));
    }
}