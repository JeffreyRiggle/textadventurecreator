import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class LandingPage extends BasePage {
    LandingPage(FxRobot robot, Node root) {
        super(robot, root);
    }

    public void assertTagLine() throws Exception {
        Assertions.assertThat(waitForLabeled("#tagLine")).hasText("A visual IDE for text adventurers");
    }
}