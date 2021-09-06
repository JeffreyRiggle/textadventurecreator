import java.util.concurrent.TimeUnit;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public abstract class BasePage {
    protected FxRobot robot;
    protected Node root;

    BasePage(FxRobot robot, Node root) {
        this.robot = robot;
        this.root = root;
    }

    protected Labeled waitForLabeled(String query) throws Exception {
        final Labeled[] result = new Labeled[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                NodeQuery res = robot.from(root).lookup(query);
                result[0] = res.queryLabeled();
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    protected TextField waitForTextField(String query) throws Exception {
        final TextField[] result = new TextField[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                result[0] = robot.from(root).lookup(query).queryAs(TextField.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    protected TextArea waitForTextArea(String query) throws Exception {
        final TextArea[] result = new TextArea[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                result[0] = robot.from(root).lookup(query).queryAs(TextArea.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    protected Button waitForButton(String query) throws Exception {
        final Button[] result = new Button[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                result[0] = robot.from(root).lookup(query).queryButton();
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }

    protected RadioButton waitForRadioButton(String query) throws Exception {
        final RadioButton[] result = new RadioButton[1];
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                result[0] = robot.from(root).lookup(query).queryAs(RadioButton.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result[0];
    }
}