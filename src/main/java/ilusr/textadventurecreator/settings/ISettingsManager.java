package ilusr.textadventurecreator.settings;

public interface ISettingsManager {
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param value The new value for the setting.
	 */
	void setStringSetting(String settingName, String value);
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param defaultValue The default value to return if the setting does not exist.
	 * @return The value of the setting.
	 */
	String getStringSetting(String settingName, String defaultValue);
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param value The new value for the setting.
	 */
	void setBooleanValue(String settingName, boolean value);
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param defaultValue The default value to return if the setting does not exist.
	 * @return The value of the setting.
	 */
	boolean getBooleanSetting(String settingName, boolean defaultValue);
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param value The new value for the setting.
	 */
	void setIntValue(String settingName, int value);
	/**
	 * 
	 * @param settingName The name of the setting.
	 * @param defaultValue The default value to return if the setting does not exist.
	 * @return The value of the setting.
	 */
	int getIntSetting(String settingName, int defaultValue);
	/**
	 * Saves settings asynchronously.
	 */
	void saveAsync();
	/**
	 * Saves settings.
	 */
	void save();
}
