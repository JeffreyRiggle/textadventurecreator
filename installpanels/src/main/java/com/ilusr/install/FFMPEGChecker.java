package com.ilusr.install;

import com.izforge.izpack.gui.LabelFactory;
import java.awt.event.ActionEvent;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.net.URI;
import javax.swing.*;

public class FFMPEGChecker implements DependencyChecker {
    private final String missingText = "FFMPEG is missing please install maven before continuing.";
    private final String installedText = "FFMPEG has been installed";

    public boolean hasDependency() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] {"ffmpeg", "-version"});
            int res = proc.waitFor();
            System.out.println(res);
            return res == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public JComponent getMissingText() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(new JLabel("FFMPEG is missing please install FFMPEG before continuing."));
        JButton downloadButton = new JButton();
        downloadButton.setText("<HTML>FFMPEG can be downloaded <FONT color=\"#000099\"><U>here</U></FONT></HTML>");
        downloadButton.addActionListener((ActionEvent e) -> {
            try {
                openLink(new URI("https://ffmpeg.org/download.html"));
            } catch (Exception ex) { }
        });
        panel.add(downloadButton);
        return panel;
    }

    public JComponent getInstalledText() {
        return LabelFactory.create(this.installedText);
    }

    private void openLink(URI uri) throws Exception {
        if (!Desktop.isDesktopSupported()) {
            return;
        }

        Desktop.getDesktop().browse(uri);
    }
}