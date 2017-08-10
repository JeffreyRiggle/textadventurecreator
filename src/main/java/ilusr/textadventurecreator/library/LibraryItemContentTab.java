package ilusr.textadventurecreator.library;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryItemContentTab extends TabContent {

	private final String LIBRARY_ITEM_PERSISTENCE = "LibraryItemName: ";
	private final String DELIM = ";=;";
	
	private final ILanguageService languageService;
	private LibraryItemModel model;
	
	/**
	 * 
	 * @param view A @see LibraryItemView to display.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LibraryItemContentTab(LibraryItemView view, ILanguageService languageService) {
		this.model = view.model();
		this.languageService = languageService;
		super.content().set(view);
		super.titleGraphic(languageService.getValue(DisplayStrings.LIBRARY_ITEM));
		super.toolTip().set(languageService.getValue(DisplayStrings.LIBRARY_ITEM));
		super.canClose().set(true);
		
		initialize();
	}
	
	private void initialize() {
		languageService.addListener(() -> {
			super.titleGraphic(languageService.getValue(DisplayStrings.LIBRARY_ITEM));
			super.toolTip().set(languageService.getValue(DisplayStrings.LIBRARY_ITEM));
		});
		
		applyCustomData();
		model.author().addListener((v, o, n) -> {
			applyCustomData();
		});
		
		model.name().addListener((v, o, n) -> {
			applyCustomData();
		});
	}
	
	private void applyCustomData() {
		String author = model.author().get();
		String name = model.name().get();
		
		if (author != null && !author.isEmpty() && name != null && !name.isEmpty()) {
			super.customData().set(LIBRARY_ITEM_PERSISTENCE + name + DELIM + author);
		}
	}
}
