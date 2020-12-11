package com.ilusr.install;
 
public class Utils {
    private static String OS_NAME = System.getProperty("os.name").toLowerCase();
	
	/**
	 * 
	 * @return A boolean representing if this is a windows environment.
	 */
	public static boolean isWindows() {
		return OS_NAME.contains("win");
	}
}