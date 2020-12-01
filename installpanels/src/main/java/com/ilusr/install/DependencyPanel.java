package com.ilusr.install;

import com.izforge.izpack.api.GuiId;
import com.izforge.izpack.api.data.Info;
import com.izforge.izpack.api.data.Panel;
import com.izforge.izpack.api.resource.Resources;
import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LayoutConstants;
import com.izforge.izpack.gui.log.Log;
import com.izforge.izpack.installer.gui.InstallerFrame;
import com.izforge.izpack.installer.gui.IzPanel;
import com.izforge.izpack.installer.data.GUIInstallData;

import javax.swing.*;
import java.awt.LayoutManager2;
import java.util.List;
import java.util.ArrayList;

/**
 * Dependency Panel class.
 *
 * @author Jeff Riggle
 */
public class DependencyPanel extends IzPanel
{
    private static final long serialVersionUID = 3257848774955905587L;
    private List<DependencyChecker> checkers;

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, Resources resources, Log log)
    {
        this(panel, parent, idata, new IzPanelLayout(log), resources);
    }

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, LayoutManager2 layout, Resources resources)
    {
        super(panel, parent, idata, layout, resources);
        this.checkers = new ArrayList<>();
        checkers.add(new MavenChecker());
        checkers.add(new FFMPEGChecker());

        this.evaluateCheckers();
        add(IzPanelLayout.createParagraphGap());

        getLayoutHelper().completeLayout();
    }

    private void evaluateCheckers()
    {
        for (DependencyChecker checker : this.checkers) {
            JComponent comp;
            if (checker.hasDependency()) {
                comp = checker.getInstalledText();
            } else {
                comp = checker.getMissingText();
            }

            add(comp, NEXT_LINE);
        }
    }

    public boolean isValidated()
    {
        return true;
    }
}