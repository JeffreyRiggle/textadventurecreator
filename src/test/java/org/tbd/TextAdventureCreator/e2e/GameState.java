import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class GameState extends BasePage {
    GameState(FxRobot robot, Node root) {
        super(robot, root);
    }

    public GameState setTextLog(String log) throws Exception {
        robot.clickOn(waitForTextArea("#textLog")).write(log);
        return this;
    }
}