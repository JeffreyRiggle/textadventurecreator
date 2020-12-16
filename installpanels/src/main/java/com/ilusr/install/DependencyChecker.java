package com.ilusr.install;

import javax.swing.*;

public interface DependencyChecker {
    public boolean hasDependency();
    public JComponent getMissingText();
    public JComponent getInstalledText();
}