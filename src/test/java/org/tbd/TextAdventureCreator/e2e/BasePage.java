import java.util.concurrent.TimeUnit;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.service.query.NodeQuery;

import javafx.scene.Node;
import javafx.scene.control.Labeled;

public abstract class BasePage {
    protected FxRobot robot;
    protected Node root;

    BasePage(FxRobot robot, Node root) {
        this.robot = robot;
        this.root = root;
    }

    protected Labeled waitForLabeled(String query) throws Exception {
        final Labeled[] result = new Labeled[1];
        WaitForAsyncUtils.waitFor(120, TimeUnit.SECONDS, () -> {
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
}