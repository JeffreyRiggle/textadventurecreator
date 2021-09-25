import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class Explorer extends BasePage {
    private boolean playerToggled;
    private boolean gameStateToggled;

    Explorer(FxRobot robot, Node root) {
        super(robot, root);
    }

    private void toggle(String label) throws Exception {
        var treeCell = waitForLabeled(label);
        robot.clickOn(robot.from(treeCell).lookup(".arrow").queryAs(Node.class));
    }

    public PlayerView createPlayer() throws Exception {
        if (!playerToggled) {
            toggle("Players");
            playerToggled = true;
        }
        robot.clickOn(waitForButton("#addPlayer"));
        return new PlayerView(robot, root);
    }

    public GameStateView createGameState() throws Exception {
        if (!gameStateToggled) {
            toggle("Game States");
            gameStateToggled = true;
        }
        robot.clickOn(waitForButton("#addGameState"));
        return new GameStateView(robot, root);
    }
}