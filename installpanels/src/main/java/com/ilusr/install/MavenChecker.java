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
    private final String missingText = "Maven is missing please install maven before continuing.";

    public boolean hasDependency() {
        try {
            Process proc = getTestProcess();
            int res = proc.waitFor();
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
        return LabelFactory.create(this.missingText);
    }

    public JComponent getInstalledText() {
        return LabelFactory.create(this.installedText);
    }
}