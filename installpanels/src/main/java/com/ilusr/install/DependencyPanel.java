package com.ilusr.install;

import com.izforge.izpack.api.GuiId;
import com.izforge.izpack.api.data.Info;
import com.izforge.izpack.api.data.Panel;
import com.izforge.izpack.api.resource.Resources;
import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.gui.LayoutConstants;
import com.izforge.izpack.gui.log.Log;
import com.izforge.izpack.installer.gui.InstallerFrame;
import com.izforge.izpack.installer.gui.IzPanel;
import com.izforge.izpack.installer.data.GUIInstallData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Dependency Panel class.
 *
 * @author Jeff Riggle
 */
public class DependencyPanel extends IzPanel
{
    private static final long serialVersionUID = 3257848774955905587L;

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, Resources resources, Log log)
    {
        this(panel, parent, idata, new IzPanelLayout(log), resources);
    }

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, LayoutManager2 layout, Resources resources)
    {
        super(panel, parent, idata, layout, resources);
        String welcomeText = "Checking system dependencies";
        JLabel welcomeLabel = LabelFactory.create(welcomeText, parent.getIcons().get("host"), LEADING);
        welcomeLabel.setName(GuiId.HELLO_PANEL_LABEL.id);
        add(welcomeLabel, NEXT_LINE);
        add(IzPanelLayout.createParagraphGap());

        getLayoutHelper().completeLayout();
    }

    public boolean isValidated()
    {
        return true;
    }
}