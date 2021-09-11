import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class GameStateView extends BasePage {
    GameStateView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public GameStateView setTextLog(String log) throws Exception {
        robot.clickOn(waitForTextArea("#textLog")).write(log);
        return this;
    }
}