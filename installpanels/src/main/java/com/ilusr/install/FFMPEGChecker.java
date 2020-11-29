package com.ilusr.install;

public class FFMPEGChecker implements DependencyChecker {
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
}