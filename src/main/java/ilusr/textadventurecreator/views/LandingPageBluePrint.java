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
public class LandingPageBluePrint implements ITabContentBluePrint {

	@Override
	public ITabContent create() {
		LogRunner.logger().info("Creating landing tab");
		return ServiceManager.getInstance().<LandingPageContentTab>get("LandingPageContentTab");
	}

	@Override
	public ITabContent create(String customData) {
		LogRunner.logger().info("Creating landing tab");
		return ServiceManager.getInstance().<LandingPageContentTab>get("LandingPageContentTab");
	}
}
