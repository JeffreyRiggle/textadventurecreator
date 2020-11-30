package com.ilusr.install;

public interface DependencyChecker {
    public boolean hasDependency();
    public String getMissingText();
    public String getInstalledText();
}