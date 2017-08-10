package ilusr.textadventurecreator.wizard;

import java.util.Arrays;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.WizardPage;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameSettingsWizardPage extends WizardPage<GameSettingsModel> implements IStyleWatcher {

	private GameWizardView view;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private StyleUpdater styleUpdater;
	
	/**
	 * 
	 * @param view The @see GameWizardView to display.
	 * @param canFinish If the game wizard can finish.
	 */
	public GameSettingsWizardPage(GameWizardView view, 
								  boolean canFinish, 
								  IStyleContainerService styleService,
								  InternalURLProvider urlProvider) {
		super(view, canFinish);
		this.view = view;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		setup();
	}
	
	public void setView(GameWizardView view) {
		teardown();
		this.view = view;
		setup();
	}
	
	private void setup() {
		if (super.result().get() != null) {
			view.setModel(super.result().get());
		}
		
		super.result().addListener((v, o, n) -> {
			view.setModel(n);
		});
		
		super.valid().bind(view.valid());
		
		styleUpdater = new StyleUpdater(urlProvider, "wizareastylesheet.css", view);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.WIZARD_AREA), this, false);
		
		String style = styleService.get(StyledComponents.WIZARD_AREA);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	private void teardown() {
		styleService.stopWatchingStyle(this);
	}

	@Override
	public void update(String style, String css) {
		if (styleUpdater != null) {
			styleUpdater.update(css);
		}
	}
}
