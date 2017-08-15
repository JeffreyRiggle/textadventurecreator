package ilusr.textadventurecreator.views;

import ilusr.core.ioc.ServiceManager;
import ilusr.iroshell.services.ITabContent;
import ilusr.iroshell.services.ITabContentBluePrint;
import ilusr.logrunner.LogRunner;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameExplorerBluePrint implements ITabContentBluePrint{

	@Override
	public ITabContent create() {
		LogRunner.logger().info("Creating explorer tab.");
		return ServiceManager.getInstance().<GameExplorerContentTab>get("GameExplorerContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		LogRunner.logger().info("Creating explorer tab.");
		return ServiceManager.getInstance().<GameExplorerContentTab>get("GameExplorerContentTab");
	}

}
