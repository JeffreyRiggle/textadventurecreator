package com.ilusr.install;

import com.izforge.izpack.gui.LabelFactory;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.*;

public class MavenChecker implements DependencyChecker {
    private final String installedText = "Maven has been installed";

    public boolean hasDependency() {
        try {
            Process proc = getTestProcess();
            int res = proc.waitFor();
            System.out.println(res);
            return res == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private Process getTestProcess() throws IOException, URISyntaxException {
        if (Utils.isWindows()) {
            File temp = File.createTempFile("mvn", ".ps1");
            InputStream inStream = this.getClass().getResourceAsStream("MavenPrereqCheck.ps1");
            byte[] buffer = new byte[inStream.available()];
            inStream.read(buffer);

            OutputStream outStream = new FileOutputStream(temp);
            outStream.write(buffer);
            outStream.close();
            return Runtime.getRuntime().exec(new String[] { "powershell.exe", temp.getAbsolutePath() });
        }

        return Runtime.getRuntime().exec(new String[] {"mvn", "-v"});
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