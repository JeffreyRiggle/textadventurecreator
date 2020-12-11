package com.ilusr.install;

import com.izforge.izpack.gui.LabelFactory;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.*;

public class FFMPEGChecker implements DependencyChecker {
    private final String installedText = "FFMPEG has been installed";

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
            File temp = File.createTempFile("ffmpeg", ".ps1");
            InputStream inStream = this.getClass().getResourceAsStream("FFMPEGPrereqCheck.ps1");
            byte[] buffer = new byte[inStream.available()];
            inStream.read(buffer);

            OutputStream outStream = new FileOutputStream(temp);
            outStream.write(buffer);
            outStream.close();
            return Runtime.getRuntime().exec(new String[] { "powershell.exe", temp.getAbsolutePath() });
        }

        return Runtime.getRuntime().exec(new String[] {"ffmpeg", "-version"});
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