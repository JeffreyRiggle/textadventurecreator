package ilusr.textadventurecreator.language;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;

import ilusr.core.io.FileUtilities;
import ilusr.core.io.StreamUtilities;
import ilusr.logrunner.LogRunner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LanguageMakerModel {

	private final String DELIM = ";=;";
	private final String SEPERATOR = "\r\n";
	
	private SimpleStringProperty code;
	private SimpleStringProperty name;
	private SimpleStringProperty nameText;
	private SimpleStringProperty codeText;
	private SimpleStringProperty keywordText;
	private SimpleStringProperty usEnglishText;
	private SimpleStringProperty valueText;
	private ObservableList<LanguageItem> items;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to use.
	 */
	public LanguageMakerModel(ILanguageService languageService) {
		code = new SimpleStringProperty();
		name = new SimpleStringProperty();
		nameText = new SimpleStringProperty(languageService.getValue(DisplayStrings.LANGUAGE_NAME));
		codeText = new SimpleStringProperty(languageService.getValue(DisplayStrings.LANGUAGE_CODE));
		keywordText = new SimpleStringProperty(languageService.getValue(DisplayStrings.KEYWORD));
		usEnglishText = new SimpleStringProperty(languageService.getValue(DisplayStrings.US_ENGLISH_VALUE));
		valueText = new SimpleStringProperty(languageService.getValue(DisplayStrings.NEW_VALUE));
		
		items = FXCollections.observableArrayList();
		
		initialize();
	}
	
	/**
	 * 
	 * @param file A file to load language from.
	 * @param languageService A @see LanguageService to use.
	 */
	public LanguageMakerModel(File file, ILanguageService languageService) {
		this(languageService);
		
		readFromFile(file);
	}
	
	private void initialize() {
		try {
			LogRunner.logger().log(Level.INFO, "loading langauge items from English language definition.");
			String content = StreamUtilities.getStreamContents(getClass().getResourceAsStream("enLanguage.txt"));
			String[] values = content.split("\r\n");
			for (String val : values) {
				String[] code = val.split(";=;");
				if (code.length != 2 || code[0].equals("LanguageCode") || code[0].equals("LanguageDisplay")) {
					continue;
				}
				
				items.add(new LanguageItem(code[0], code[1], new String()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readFromFile(File file) {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Reading language from file: %s", file.getAbsolutePath()));
			String content = FileUtilities.getFileContentWithReturns(file);
			String[] values = content.split("\r\n");
			for (String val : values) {
				String[] code = val.split(";=;");
				if (code.length != 2) {
					continue;
				}
				
				if (code[0].equals("LanguageCode")) {
					this.code.set(code[1]);
					continue;
				}
				
				if (code[0].equals("LanguageDisplay")) {
					this.name.set(code[1]);
					continue;
				}
				
				Optional<LanguageItem> item = items.stream().filter(f -> f.getKeyword().equals(code[0])).findFirst();
				if (item.isPresent()) {
					item.get().setNewValue(code[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The language code (example: en-us).
	 */
	public SimpleStringProperty code() {
		return code;
	}
	
	/**
	 * 
	 * @return The name of the language
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return A display value for language name.
	 */
	public SimpleStringProperty nameText() {
		return nameText;
	}
	
	/**
	 * 
	 * @return A display value for language code.
	 */
	public SimpleStringProperty codeText() {
		return codeText;
	}
	
	/**
	 * 
	 * @return The display value for keyword.
	 */
	public SimpleStringProperty keywordText() {
		return keywordText;
	}
	
	/**
	 * 
	 * @return The display value for English.
	 */
	public SimpleStringProperty usEnglishText() {
		return usEnglishText;
	}
	
	/**
	 * 
	 * @return The display value for value.
	 */
	public SimpleStringProperty valueText() {
		return valueText;
	}
	
	/**
	 * 
	 * @param path The path to save the language file to.
	 */
	public void buildFile(String path) {
		try {
			LogRunner.logger().log(Level.INFO, String.format("Saving language to file: %s", path));
			FileUtilities.saveToFile(path, createContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return All of the registered language items.
	 */
	public ObservableList<LanguageItem> items() {
		return items;
	}
	
	private String createContent() {
		StringBuilder builder = new StringBuilder();
		builder.append("LanguageCode");
		builder.append(DELIM);
		builder.append(code.get());
		builder.append(SEPERATOR);
		builder.append("LanguageDisplay");
		builder.append(DELIM);
		builder.append(name.get());
		builder.append(SEPERATOR);
		
		for (LanguageItem item : items) {
			builder.append(item.getKeyword());
			builder.append(DELIM);
			builder.append(item.getNewValue());
			builder.append(SEPERATOR);
		}
		
		return builder.toString();
	}
}
