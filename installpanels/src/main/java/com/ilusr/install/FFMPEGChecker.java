package com.ilusr.install;

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

    public String getMissingText() {
        return this.missingText;
    }

    public String getInstalledText() {
        return this.installedText;
    }
}