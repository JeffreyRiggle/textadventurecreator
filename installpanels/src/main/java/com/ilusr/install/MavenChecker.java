package com.ilusr.install;

public class MavenChecker implements DependencyChecker {
    private final String missingText = "Maven is missing please install maven before continuing.";
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

    public String getMissingText() {
        return this.missingText;
    }

    public String getInstalledText() {
        return this.installedText;
    }
}