import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;
import javafx.stage.Window;

public class LandingPage extends BasePage {
    LandingPage(FxRobot robot, Node root) {
        super(robot, root);
    }

    public void assertTagLine() throws Exception {
        Assertions.assertThat(waitForLabeled("#tagLine")).hasText("A visual IDE for text adventurers");
    }

    public CreateProjectWizard createProject() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForLabeled("#createProject"));
        // Find a better abstraction for this.
        Node popup = null;
        for (Window win : robot.listWindows()) {
            if (!currentWindows.contains(win)) {
                popup = win.getScene().getRoot();
                break;
            }
        }
        return new CreateProjectWizard(robot, popup);
    }
}