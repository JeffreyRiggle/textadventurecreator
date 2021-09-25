import java.util.concurrent.TimeUnit;
import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class ApplicationPage extends BasePage {
    ApplicationPage(FxRobot robot, Node root) {
        super(robot, root);
    }

    public ApplicationPage generate() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForLabeled("File"));
        // Find a better abstraction for this.
        Node popup = waitForWindow(currentWindows);
        robot.clickOn(waitForLabeled("Generate", popup));
        waitForLabeled("Cleaning up...");
        return this;
    }
}