package com.ilusr.install;

import com.izforge.izpack.gui.LabelFactory;
import java.awt.event.ActionEvent;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.net.URI;
import javax.swing.*;

public class MavenChecker implements DependencyChecker {
    private final String installedText = "Maven has been installed";

    public boolean hasDependency() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] {"mvn", "-v"});
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
        panel.add(new JLabel("Maven is missing please install maven before continuing."));
        JButton downloadButton = new JButton();
        downloadButton.setText("<HTML>Maven can be downloaded <FONT color=\"#000099\"><U>here</U></FONT></HTML>");
        downloadButton.addActionListener((ActionEvent e) -> {
            try {
                openLink(new URI("https://maven.apache.org/download.cgi"));
            } catch (Exception ex) { }
        });
        panel.add(downloadButton);

        JButton installButton = new JButton();
        installButton.setText("<HTML>Install instructions can be found <FONT color=\"#000099\"><U>here</U></FONT></HTML>");
        installButton.addActionListener((ActionEvent e) -> {
            try {
                openLink(new URI("https://maven.apache.org/install.html"));
            } catch (Exception ex) { }
        });
        panel.add(installButton);
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