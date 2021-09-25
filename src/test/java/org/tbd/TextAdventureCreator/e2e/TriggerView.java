import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class TriggerView extends BasePage {
    TriggerView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public TriggerView setType(String type) throws Exception {
        setComboBySelector("#types", type);
        return this;
    }

    public TriggerView setText(String text) throws Exception {
        robot.clickOn(waitForTextField("#textValue")).write(text);
        return this;
    }

    public TriggerView setCaseSensitive(boolean caseSensitive) throws Exception {
        String target = caseSensitive ? "#yes" : "#no";
        robot.clickOn(waitForRadioButton(target));
        return this;
    }

    public void ok() throws Exception {
        robot.clickOn(waitForButton("Ok"));
    }
}