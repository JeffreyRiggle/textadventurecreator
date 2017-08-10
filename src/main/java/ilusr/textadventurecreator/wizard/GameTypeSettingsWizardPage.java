package ilusr.textadventurecreator.wizard;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameTypeSettingsWizardPage extends GameSettingsWizardPage{

	private final HostedGameWizardView hView;
	private final StandAloneGameWizardView saView;
	private final GameSettingsModel model;
	
	/**
	 * 
	 * @param hView The hosted game view.
	 * @param saView The stand alone view.
	 * @param model The model to bind to.
	 * @param canFinish If the page can finish.
	 */
	public GameTypeSettingsWizardPage(HostedGameWizardView hView, 
									  StandAloneGameWizardView saView, 
									  GameSettingsModel model, 
									  boolean canFinish,
									  IStyleContainerService styleService,
									  InternalURLProvider urlProvider) {
		super(hView, canFinish, styleService, urlProvider);
		
		this.hView = hView;
		this.saView = saView;
		this.model = model;
		
		initialize();
	}
	
	private void initialize() {
		model.standAlone().addListener((v, o, n) -> {
			if (n) {
				super.setView(saView);
				super.content().set(saView);
			} else {
				super.setView(hView);
				super.content().set(hView);
			}
		});
	}
}
