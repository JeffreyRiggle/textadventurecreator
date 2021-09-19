import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class GameStateView extends BasePage {
    private String gameStateName = "Start";

    GameStateView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public GameStateView setTextLog(String log) throws Exception {
        robot.clickOn(waitForTextArea("#textLog")).write(log);
        return this;
    }

    public GameStateView focus() throws Exception {
        robot.clickOn(waitForLabeled("Game State " + gameStateName));
        return this;
    }

    public MacroView createMacro() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForButton("#macro"));
        Node popup = waitForWindow(currentWindows);
        return new MacroView(robot, popup);
    }
}