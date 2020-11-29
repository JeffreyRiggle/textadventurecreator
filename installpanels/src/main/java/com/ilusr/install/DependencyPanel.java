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
    private MavenChecker mavenChecker;
    private FFMPEGChecker ffmpegChecker;

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, Resources resources, Log log)
    {
        this(panel, parent, idata, new IzPanelLayout(log), resources);
    }

    public DependencyPanel(Panel panel, InstallerFrame parent, GUIInstallData idata, LayoutManager2 layout, Resources resources)
    {
        super(panel, parent, idata, layout, resources);
        this.mavenChecker = new MavenChecker();
        this.ffmpegChecker = new FFMPEGChecker();

        this.evaluateMaven();
        this.evaluateFFMPEG();
        add(IzPanelLayout.createParagraphGap());

        getLayoutHelper().completeLayout();
    }

    private void evaluateMaven()
    {
        String panelText;
        if (this.mavenChecker.hasDependency()) {
            panelText = "Maven has been installed";
        } else {
            panelText = "Maven is missing please install maven before continuing.";
        }

        JLabel mvnLabel = LabelFactory.create(panelText, parent.getIcons().get("host"), LEADING);
        mvnLabel.setName(GuiId.HELLO_PANEL_LABEL.id);
        add(mvnLabel, NEXT_LINE);
    }

    private void evaluateFFMPEG()
    {
        String panelText;
        if (this.ffmpegChecker.hasDependency()) {
            panelText = "FFMPEG has been installed";
        } else {
            panelText = "FFMPEG is missing please install maven before continuing.";
        }

        JLabel ffmpegLabel = LabelFactory.create(panelText, parent.getIcons().get("host"), LEADING);
        ffmpegLabel.setName(GuiId.HELLO_PANEL_LABEL.id);
        add(ffmpegLabel, NEXT_LINE);
    }

    public boolean isValidated()
    {
        return true;
    }
}