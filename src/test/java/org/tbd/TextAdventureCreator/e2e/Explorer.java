import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class Explorer extends BasePage {
    Explorer(FxRobot robot, Node root) {
        super(robot, root);
    }

    private void togglePlayers() throws Exception {
        var treeCell = waitForLabeled("Players");
        robot.clickOn(robot.from(treeCell).lookup(".arrow").queryAs(Node.class));
    }

    public Explorer createPlayer() throws Exception {
        togglePlayers();
        waitForButton("#addPlayer");
        return this;
    }
}