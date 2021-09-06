import java.util.concurrent.TimeUnit;
import java.util.List;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
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

    private Node waitForWindow(List<Window> currentWindows) throws Exception {
        final Node[] result = new Node[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                for (Window win : robot.listWindows()) {
                    if (!currentWindows.contains(win)) {
                        result[0] = win.getScene().getRoot();
                    }
                }
                return result[0] != null;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    public CreateProjectWizard createProject() throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForLabeled("#createProject"));
        // Find a better abstraction for this.
        Node popup = waitForWindow(currentWindows);
        return new CreateProjectWizard(robot, popup);
    }
}