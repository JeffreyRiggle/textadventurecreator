import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Label;

import ilusr.textadventurecreator.shell.TextAdventureCreatorShell;

public class E2ETest extends ApplicationTest {
    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) throws Exception {
        new TextAdventureCreatorShell().start(stage);
        stage.show();
        stage.toFront();
    }

    private void waitFor(String query) throws Exception {
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, new Callable<Boolean>() {
            @Override
            public Boolean call() {
                try {
                    lookup(query);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    @Test
    public void should_show_landing_page() throws Exception {
        // waitFor("#tagLine");
        // Assertions.assertThat(lookup("#tagLine").queryText()).hasText("A visual IDE for text adventurers");
    }
}