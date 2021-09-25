import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class GameStateView extends BasePage {
    private String gameStateName;

    GameStateView(FxRobot robot, Node root) {
        super(robot, root);
        gameStateName = "Start";
    }

    public GameStateView setGameStateName(String name) throws Exception {
        robot.clickOn(waitForTextField("#gameStateId")).write(name);
        gameStateName = name;
        return this;
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

    public GameStateView setLayout(String layout) throws Exception {
        setComboBySelector("#layout", layout);
        return this;
    }

    public OptionView addOption() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        Node optionList = waitForListView("#options");
        robot.clickOn(waitForButton("#listCellAdd", optionList));
        Node popup = waitForWindow(currentWindows);
        return new OptionView(robot, popup);
    }

    public void close() throws Exception {
        var header = waitForLabeled("Game State " + gameStateName).getParent().getParent();
        robot.clickOn(robot.from(header).lookup(".tab-close-button").queryAs(Node.class));
    }
}