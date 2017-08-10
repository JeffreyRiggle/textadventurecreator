package ilusr.textadventurecreator.language;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ILanguageService {
	/**
	 * 
	 * @param language The language file to add.
	 */
	void addLanguage(File language);
	/**
	 * 
	 * @param language The language file to remove.
	 */
	void removeLanguage(File language);
	/**
	 * 
	 * @return All of the languages registered to this service..
	 */
	Map<String, String> getLanguages();
	/**
	 * 
	 * @param code The new language to use.
	 */
	void setLanguage(String code);
	/**
	 * 
	 * @param resource The id for the display string.
	 * @return A localized value based off of the provided id.
	 */
	String getValue(String resource);
	/**
	 * 
	 * @param listener A @see Runnable to execute when the language changes.
	 */
	void addListener(Runnable listener);
	/**
	 * 
	 * @param listener A @see Runnable to execute when the language changes.
	 */
	void removeListener(Runnable listener);
}
