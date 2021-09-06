import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Label;

import ilusr.textadventurecreator.shell.TextAdventureCreatorShell;

public class E2ETest extends ApplicationTest {
    private TextAdventureCreatorShell shell;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) throws Exception {
        shell = new TextAdventureCreatorShell();
        shell.start(stage);
        stage.show();
        stage.toFront();
    }

    private void waitForStage(String title) throws Exception {
        WaitForAsyncUtils.waitFor(120, TimeUnit.SECONDS, () -> {
            try {
                targetWindow(title);
                window(title);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Test
    public void should_show_landing_page() throws Exception {
        waitForStage("Text Adventure Creator");
        new LandingPage(this, targetWindow().getScene().getRoot()).assertTagLine();
    }

    @Test
    public void should_create_java_games() throws Exception {
        waitForStage("Text Adventure Creator");
        new LandingPage(this, targetWindow().getScene().getRoot())
            .createProject()
            .setGameName("Sample Java Game")
            .setGameDescription("This is a test game!")
            .setGameIconPath("/fix/me/foo.png")
            .setGameCreator("Automation Tester")
            .goForward()
            .setStandAlone()
            .goForward();
            // .finish();
        // TODO
        // 1. set project name
        // 2. set description
        // 3. set icon
        // 4. set creator
        // 5. press forward
        // 6. press stand-alone
        // 7. press forward
        // 8. press finish?
        // 9. create player
        // 10. create game state.
        // 11. create finish action.
        // 12. generate game.
        // 13. execute game.
    }
    // Ideal test cases.
    // 1. generate java game.
    // 2. generate web game.
    // 3. generate electron game.
    // 4. create library.
    // 5. import library.
    // 6. debug a game.
    // 7. load a mod
    // 8. create a custom view
    // 9. apply a theme
    // 10. apply a language pack.
    // 11. load an existing game file.

    // TODO can a different test stage be created?
}