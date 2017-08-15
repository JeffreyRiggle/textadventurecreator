package ilusr.textadventurecreator.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.language.LanguageMakerModel;
import ilusr.textadventurecreator.language.LanguageMakerView;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Jeff Riggle
 *
 */
public class LanguageSettingsModel {

	private final ILanguageService languageService;
	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;

	private Map<String, String> languageMap;

	private SelectionAwareObservableList<String> languages;
	private LanguageAwareString applyText;
	private LanguageAwareString addText;
	private LanguageAwareString titleText;
	private LanguageAwareString langText;
	private LanguageAwareString editText;

	/**
	 *
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param dialogProvider A @see IDialogProvider to create dialogs.
	 */
	public LanguageSettingsModel(ILanguageService languageService, IDialogService dialogService, IDialogProvider dialogProvider) {
		this.languageService = languageService;
		this.dialogService = dialogService;
		this.dialogProvider = dialogProvider;

		languageMap = new HashMap<String, String>();
		languages = new SelectionAwareObservableList<String>();
		applyText = new LanguageAwareString(languageService, DisplayStrings.APPLY);
		addText = new LanguageAwareString(languageService, DisplayStrings.ADD);
		titleText = new LanguageAwareString(languageService, DisplayStrings.LANGUAGE_SETTINGS);
		langText = new LanguageAwareString(languageService, DisplayStrings.LANGUAGES);
		editText = new LanguageAwareString(languageService, DisplayStrings.EDIT);

		initialize();
	}

	private void initialize() {
		for (Entry<String, String> entry : languageService.getLanguages().entrySet()) {
			LogRunner.logger().info(String.format("Adding %s(%s) to language list", entry.getValue(), entry.getKey()));
			languages.list().add(entry.getValue());
			languageMap.put(entry.getValue(), entry.getKey());
		}
	}

	/**
	 *
	 * @return The languages found on this machine.
	 */
	public SelectionAwareObservableList<String> languages() {
		return languages;
	}

	/**
	 *
	 * @return Display string for apply.
	 */
	public SimpleStringProperty applyText() {
		return applyText;
	}

	/**
	 *
	 * @return Display string for add.
	 */
	public SimpleStringProperty addText() {
		return addText;
	}

	/**
	 *
	 * @return Display string for title
	 */
	public SimpleStringProperty titleText() {
		return titleText;
	}

	/**
	 *
	 * @return Display string for language
	 */
	public SimpleStringProperty langText() {
		return langText;
	}

	/**
	 *
	 * @return Display string for edit.
	 */
	public SimpleStringProperty editText() {
		return editText;
	}

	/**
	 * Applies the selected language.
	 */
	public void apply() {
		LogRunner.logger().info(String.format("Applying language %s", languages.selected().get()));
		languageService.setLanguage(languageMap.get(languages.selected().get()));
	}

	/**
	 * Adds a new language.
	 */
	public void add() {
		LogRunner.logger().info("Adding new language.");
		LanguageMakerModel maker = new LanguageMakerModel(languageService);
		Dialog dialog = dialogProvider.create(new LanguageMakerView(maker));

		dialog.setOnComplete(() -> {
			LogRunner.logger().info(String.format("Creating new language %s", maker.name().get()));
			maker.buildFile(System.getProperty("user.home") + "/ilusr/languages/" + maker.code().get() + "language.txt");
		});

		dialogService.displayModeless(dialog);
	}

	/**
	 * Edits the selected language.
	 */
	public void edit() {
		String languageCode = languageMap.get(languages.selected().get());

		if (languageCode == null || languageCode.equals("en-US")) {
			LogRunner.logger().info("Unable to edit language since it was null or en-US");
			return;
		}

		File file = new File(System.getProperty("user.home") + "/ilusr/languages/" + languageCode + "language.txt");
		if (!file.exists()) {
			LogRunner.logger().info(String.format("Unable to edit language since %s does not exist", file.getAbsolutePath()));
			return;
		}

		LogRunner.logger().info(String.format("Editing language %s", languageCode));
		LanguageMakerModel maker = new LanguageMakerModel(file, languageService);
		Dialog dialog = dialogProvider.create(new LanguageMakerView(maker));

		dialog.setOnComplete(() -> {
			LogRunner.logger().info(String.format("Finished editing language %s. Saving file.", languageCode));
			maker.buildFile(System.getProperty("user.home") + "/ilusr/languages/" + maker.code().get() + "language.txt");
		});

		dialogService.displayModeless(dialog);
	}
}
