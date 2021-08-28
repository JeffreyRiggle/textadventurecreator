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

    private NodeQuery waitFor(String query) throws Exception {
        final NodeQuery[] result = new NodeQuery[1];
        WaitForAsyncUtils.waitFor(120, TimeUnit.SECONDS, () -> {
            try {
                // TODO find a better way to do this.
                NodeQuery res = from(targetWindow().getScene().getRoot()).lookup(query);
                res.queryLabeled();
                result[0] = res;
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    @Test
    public void should_show_landing_page() throws Exception {
        waitForStage("Text Adventure Creator");
        Assertions.assertThat(waitFor("#tagLine").queryLabeled()).hasText("A visual IDE for text adventurers");
    }
}