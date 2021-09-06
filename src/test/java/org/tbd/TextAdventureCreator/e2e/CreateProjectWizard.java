import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Node;

public class CreateProjectWizard extends BasePage {
    CreateProjectWizard(FxRobot robot, Node root) {
        super(robot, root);
    }

    public CreateProjectWizard setGameName(String name) throws Exception {
        robot.clickOn(waitForTextField("#gameName")).write(name);
        return this;
    }

    public CreateProjectWizard setGameDescription(String description) throws Exception {
        robot.clickOn(waitForTextArea("#gameDescription")).write(description);
        return this;
    }

    public CreateProjectWizard setGameIconPath(String path) throws Exception {
        robot.clickOn(waitForTextField("#iconLocation")).write(path);
        return this;
    }

    public CreateProjectWizard setGameCreator(String creator) throws Exception {
        robot.clickOn(waitForTextField("#creator")).write(creator);
        return this;
    }

    public CreateProjectWizard goForward() throws Exception {
        robot.clickOn(waitForButton("#forward"));
        return this;
    }

    public CreateProjectWizard setStandAlone() throws Exception {
        robot.clickOn(waitForRadioButton("#standAlone"));
        return this;
    }

    public CreateProjectWizard finish() throws Exception {
        robot.clickOn(waitForButton("#finish"));
        return this;
    }
}