package ilusr.textadventurecreator.views.layout;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutCreatorContentTab extends TabContent {

	private final String LAYOUT_NAME_PERSISTENCE = "LayoutName: ";
	private final LayoutCreatorView view;
	private final ILanguageService languageService;
	
	/**
	 * 
	 * @param view A @see LayoutCreatorView to display.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public LayoutCreatorContentTab(LayoutCreatorView view, ILanguageService languageService) {
		this.view = view;
		this.languageService = languageService;
		super.content().set(view);
		super.titleGraphic(languageService.getValue(DisplayStrings.LAYOUT));
		super.toolTip().set(languageService.getValue(DisplayStrings.LAYOUT_INFO));
		super.canClose().set(true);
		
		updateDisplayStrings();
		initialize();
	}
	
	private void initialize() {
		if (view.model().id().get() != null && !view.model().id().get().isEmpty()) {
			super.customData().set(LAYOUT_NAME_PERSISTENCE + view.model().id().get());
		}
		
		view.model().id().addListener((v, o, n) -> {
			super.titleGraphic(String.format("%s %s", languageService.getValue(DisplayStrings.LAYOUT), n));
			super.toolTip().set(String.format("%s %s", languageService.getValue(DisplayStrings.LAYOUT_INFO), n));
			super.customData().set(LAYOUT_NAME_PERSISTENCE + n);
		});
		
		languageService.addListener(() -> {
			updateDisplayStrings();
		});
	}
	
	private void updateDisplayStrings() {
		String stateId = view.model().id().get();
		
		if (stateId != null) {
			super.titleGraphic(String.format("%s %s", languageService.getValue(DisplayStrings.LAYOUT), stateId));
			super.toolTip().set(String.format("%s %s", languageService.getValue(DisplayStrings.LAYOUT_INFO), stateId));
		}
	}
}
