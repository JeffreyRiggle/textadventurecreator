package ilusr.textadventurecreator.shell;

import java.util.logging.Level;

import ilusr.iroshell.core.LocationProvider;
import ilusr.iroshell.statusbar.IStatusBarService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.statusbars.ProjectStatus;
import ilusr.textadventurecreator.statusbars.ProjectStatusModel;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StatusBarInitializer implements IInitialize {

	private final IStatusBarService statusBarService;
	private final ProjectStatusService statusService;
	
	/**
	 * 
	 * @param statusBarService A @see IStatusBarService to display status bars.
	 * @param statusService A @see ProjectStatusService to manage status bars.
	 */
	public StatusBarInitializer(IStatusBarService statusBarService, ProjectStatusService statusService) {
		this.statusBarService = statusBarService;
		this.statusService = statusService;
	}
	
	@Override
	public void initialize() {
		LogRunner.logger().log(Level.INFO, "Initializing status bars.");
		statusBarService.addStatusBar(new ProjectStatus(new ProjectStatusModel(statusService)), LocationProvider.first());
	}
}
