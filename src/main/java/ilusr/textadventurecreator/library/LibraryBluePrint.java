package ilusr.textadventurecreator.library;

import ilusr.core.ioc.ServiceManager;
import ilusr.iroshell.services.ITabContent;
import ilusr.iroshell.services.ITabContentBluePrint;
import ilusr.logrunner.LogRunner;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryBluePrint implements ITabContentBluePrint {

	@Override
	public ITabContent create() {
		LogRunner.logger().info("Creating library content tab");
		return ServiceManager.getInstance().<LibraryContentTab>get("LibraryContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		LogRunner.logger().info(String.format("Creating library content tab with data: %s", customData));
		return ServiceManager.getInstance().<LibraryContentTab>get("LibraryContentTab");
	}

}
