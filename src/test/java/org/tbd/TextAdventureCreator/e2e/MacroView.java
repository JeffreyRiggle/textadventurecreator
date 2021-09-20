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
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForComboBox("#playersMacro"));
        Node popup = waitForWindow(currentWindows);
        robot.clickOn(waitForLabeled(playerName, popup));
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