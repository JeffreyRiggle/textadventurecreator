package ilusr.textadventurecreator.language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilusr.core.data.Tuple;
import ilusr.core.i18n.LanguageManager;
import ilusr.core.io.FileUtilities;
import ilusr.core.io.StreamUtilities;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.settings.SettingNames;

/**
 *
 * @author Jeff Riggle
 *
 */
public class LanguageService implements ILanguageService {

	private final Object languageLock;
	private final ISettingsManager settingsManager;
	private final LanguageManager languageManager;

	private Map<String, String> languages;
	private List<Runnable> listeners;

	/**
	 *
	 * @param settingsManager A @see SettingsManager to save and load settings.
	 */
	public LanguageService(ISettingsManager settingsManager, LanguageManager languageManager) {
		this.settingsManager = settingsManager;
		this.languageManager = languageManager;

		languageLock = new Object();
		languages = new HashMap<String, String>();
		listeners = new ArrayList<Runnable>();

		initialize();
	}

	private void initialize() {
		try {
			LogRunner.logger().info("Loading data from English Language definition.");
			String stream = StreamUtilities.getStreamContents(getClass().getResourceAsStream("enLanguage.txt"));
			addLanguage(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		findLanguages();
		languageManager.setLanguage(settingsManager.getStringSetting(SettingNames.LANGUAGE, "en-US"));
	}

	private void findLanguages() {
		File folder = new File(System.getProperty("user.home") + "/ilusr/languages");

		if (!folder.exists()) {
			folder.mkdirs();
		}

		LogRunner.logger().info(String.format("Looking for languages in path: %s", folder.getAbsolutePath()));
		for (File file : folder.listFiles()) {
			if (!file.getAbsolutePath().endsWith(".txt")) {
				continue;
			}

			LogRunner.logger().info(String.format("Found language file: %s", file.getAbsolutePath()));
			addLanguage(file);
		}
	}

	@Override
	public void addLanguage(File language) {
		Tuple<String, String> lang = getLanguageCode(language);

		synchronized (languageLock) {
			if (languages.containsKey(lang.key())) {
				return;
			}

			LogRunner.logger().info(String.format("Adding Language: %s (%s)", lang.key(), lang.value()));
			languages.put(lang.key(), lang.value());
		}

		try {
			languageManager.addLanguagePack(language);
		} catch (IOException e) {
			LogRunner.logger().severe(e);
		}
	}

	private void addLanguage(String content) {
		Tuple<String, String> lang = getLanguageCode(content);

		synchronized (languageLock) {
			if (languages.containsKey(lang.key())) {
				return;
			}

			languages.put(lang.key(), lang.value());
		}

		languageManager.addLanguagePack(content);
	}

	@Override
	public void removeLanguage(File language) {
		Tuple<String, String> lang = getLanguageCode(language);

		synchronized (languageLock) {
			if (!languages.containsKey(lang.key())) {
				return;
			}

			LogRunner.logger().info(String.format("Removing Language: %s (%s)", lang.key(), lang.value()));
			languages.remove(lang.key());
		}

		languageManager.removeLanguagePack(lang.key());
	}

	@Override
	public Map<String, String> getLanguages() {
		synchronized (languageLock) {
			return new HashMap<String, String>(languages);
		}
	}

	@Override
	public void setLanguage(String code) {
		LogRunner.logger().info(String.format("Setting Language to: %s", code));
		languageManager.setLanguage(code);
		settingsManager.setStringSetting(SettingNames.LANGUAGE, code);
		notifyListeners();
	}

	@Override
	public String getValue(String resource) {
		return languageManager.getValue(resource);
	}

	@Override
	public void addListener(Runnable listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(Runnable listener) {
		listeners.remove(listener);
	}

	private void notifyListeners() {
		for (Runnable listener : listeners) {
			listener.run();
		}
	}

	private Tuple<String, String> getLanguageCode(File language) {
		Tuple<String, String> retVal = new Tuple<String, String>(null, null);

		try {
			retVal = getLanguageCode(FileUtilities.getFileContentWithReturns(language));
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}

		return retVal;
	}

	private Tuple<String, String> getLanguageCode(String content) {
		Tuple<String, String> retVal = new Tuple<String, String>(null, null);

		String[] values = content.split("\r\n");
		for (String val : values) {
			String[] code = val.split(";=;");
			if (code.length != 2) {
				continue;
			}

			if (code[0].equals("LanguageCode")) {
				retVal.key(code[1]);
			}

			if (code[0].equals("LanguageDisplay")) {
				retVal.value(code[1]);
			}

			if (retVal.value() != null && retVal.key() != null) {
				break;
			}
		}

		return retVal;
	}
}
