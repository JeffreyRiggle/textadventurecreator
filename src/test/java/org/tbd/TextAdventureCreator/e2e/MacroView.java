import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class MacroView extends BasePage {

    MacroView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public MacroView setPlayer(String playerName) throws Exception {
        setComboBySelector("#playersMacro", playerName);
        return this;
    }

    public MacroView setSelector(String selector) throws Exception {
        setComboBySelector("#selectorMacro", selector);
        return this;
    }

    public MacroView setAttribute(String attribute) throws Exception {
        setComboBySelector("#attributesMacro", attribute);
        return this;
    }

    public MacroView setPropertyName(String propName) throws Exception {
        setComboBySelector("#propertynamesMacro", propName);
        return this;
    }

    public MacroView build() throws Exception {
        robot.clickOn(waitForButton("Build"));
        return this;
    }

    public void ok() throws Exception {
        robot.clickOn(waitForButton("Ok"));
    }
}