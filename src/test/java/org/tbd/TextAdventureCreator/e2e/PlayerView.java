import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class PlayerView extends BasePage {
    PlayerView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public PlayerView setPlayerName(String name) throws Exception {
        robot.clickOn(waitForTextField("#id")).write(name);
        return this;
    }

    private void addAndSetObjectValues(Node root, String name, String value, String description) throws Exception {
        robot.clickOn(waitForButton("#listCellAdd", root));
        robot.clickOn(waitForTextField("#name", root)).write(name);
        robot.clickOn(waitForTextField("#value", root)).write(value);
        robot.clickOn(waitForTextField("#description", root)).write(description);
    }

    public PlayerView addAttribute(String name, String value, String description) throws Exception {
        var listView = waitForListView("#attributes");
        addAndSetObjectValues(listView, name, value, description);
        return this;
    }

    public PlayerView addCharacteristic(String name, String value, String description) throws Exception {
        var listView = waitForListView("#characteristics");
        addAndSetObjectValues(listView, name, value, description);
        return this;
    }

    public PlayerView addBodyPart(String name, String description, String[] characteristics) throws Exception {
        var listView = waitForListView("#bodyParts");
        robot.clickOn(waitForButton("#listCellAdd", listView));
        // TODO open window and set stuff
        // robot.clickOn(waitForTextField("#name", listView)).write(name);
        return this;
    }
}