package ilusr.textadventurecreator.settings;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ilusr.core.ioc.ServiceManager;


public class SettingRegistration {

	/**
	 * 
	 * @param manager A @see ServiceManager to register services to.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void register(ServiceManager manager) throws IOException, URISyntaxException {
		Resource resource = new ClassPathResource("settingsregistration.xml", SettingRegistration.class);
		manager.registerServicesFromResource(resource);
	}
}
