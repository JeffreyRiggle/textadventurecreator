import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class MacroView extends BasePage {

    MacroView(FxRobot robot, Node root) {
        super(robot, root);
    }

    public MacroView setPlayer(String playerName) throws Exception {
        // robot.clickOn(waitForComboBox("#playersMacro"));
        robot.clickOn(waitForButton(playerName));
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