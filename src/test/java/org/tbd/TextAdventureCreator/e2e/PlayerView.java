import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class PlayerView extends BasePage {
    PlayerView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public PlayerView setPlayerName(String name) throws Exception {
        robot.clickOn(waitForTextField("#id")).write(name);
        return this;
    }

    private void addAndSetObjectValues(Node root, NamedObject obj) throws Exception {
        robot.clickOn(waitForButton("#listCellAdd", root));
        robot.clickOn(waitForTextField("#name", root)).write(obj.getName());
        robot.clickOn(waitForTextField("#value", root)).write(obj.getValue());
        robot.clickOn(waitForTextField("#description", root)).write(obj.getDescription());
    }

    public PlayerView addAttribute(NamedObject obj) throws Exception {
        var listView = waitForListView("#attributes");
        addAndSetObjectValues(listView, obj);
        return this;
    }

    public PlayerView addCharacteristic(NamedObject obj) throws Exception {
        var listView = waitForListView("#characteristics");
        addAndSetObjectValues(listView, obj);
        return this;
    }

    public PlayerView addBodyPart(String name, String description, NamedObject[] characteristics) throws Exception {
        var listView = waitForListView("#bodyParts");
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForButton("#listCellAdd", listView));
        Node popup = waitForWindow(currentWindows);
        robot.clickOn(waitForTextField("#name", popup)).write(name);
        robot.clickOn(waitForTextField("#description", popup)).write(description);
        robot.clickOn(waitForButton("Ok", popup));
        return this;
    }
}