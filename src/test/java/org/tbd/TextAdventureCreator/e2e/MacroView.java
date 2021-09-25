import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class MacroView extends BasePage {

    MacroView(FxRobot robot, Node root) {
        super(robot, root);
    }

    private void setComboById(String id, String target) throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForComboBox(id));
        Node popup = waitForWindow(currentWindows);
        robot.clickOn(waitForLabeled(target, popup));
    }

    public MacroView setPlayer(String playerName) throws Exception {
        setComboById("#playersMacro", playerName);
        return this;
    }

    public MacroView setSelector(String selector) throws Exception {
        setComboById("#selectorMacro", selector);
        return this;
    }

    public MacroView setAttribute(String attribute) throws Exception {
        setComboById("#attributesMacro", attribute);
        return this;
    }

    public MacroView setPropertyName(String propName) throws Exception {
        setComboById("#propertynamesMacro", propName);
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