import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public abstract class BasePage {
    protected FxRobot robot;
    protected Node root;

    BasePage(FxRobot robot, Node root) {
        this.robot = robot;
        this.root = root;
    }

    protected Labeled waitForLabeled(String query) throws Exception {
        return waitForLabeled(query, root);
    }

    protected Labeled waitForLabeled(String query, Node context) throws Exception {
        return waitForType(query, context, Labeled.class);
    }

    protected ListView waitForListView(String query) throws Exception {
        return waitForListView(query, root);
    }

    protected ListView waitForListView(String query, Node context) throws Exception {
        return waitForType(query, context, ListView.class);
    }

    protected TextField waitForTextField(String query) throws Exception {
        return waitForTextField(query, root);
    }

    protected TextField waitForTextField(String query, Node context) throws Exception {
        return waitForType(query, context, TextField.class);
    }

    protected TextArea waitForTextArea(String query) throws Exception {
        return waitForType(query, root, TextArea.class);
    }

    protected Button waitForButton(String query) throws Exception {
        return waitForButton(query, root);
    }

    protected Button waitForButton(String query, Node context) throws Exception {
        return waitForType(query, context, Button.class);
    }

    protected ComboBox waitForComboBox(String query) throws Exception {
        return waitForType(query, root, ComboBox.class);
    }

    protected RadioButton waitForRadioButton(String query) throws Exception {
        return waitForType(query, root, RadioButton.class);
    }

    private <T extends Node> T waitForType(String query, Node context, Class<T> cls) throws Exception {
        final List<T> result = new ArrayList<T>(1);
        WaitForAsyncUtils.waitFor(30, TimeUnit.SECONDS, () -> {
            try {
                result.add(robot.from(context).lookup(query).queryAs(cls));
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        return result.get(0);
    }

    protected Node waitForWindow(List<Window> currentWindows) throws Exception {
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

    protected void setComboBySelector(String selector, String target) throws Exception {
        List<Window> currentWindows = robot.listWindows();
        robot.clickOn(waitForComboBox(selector));
        Node popup = waitForWindow(currentWindows);
        robot.clickOn(waitForLabeled(target, popup));
    }
}