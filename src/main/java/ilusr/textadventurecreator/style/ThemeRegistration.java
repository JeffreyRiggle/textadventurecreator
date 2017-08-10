package ilusr.textadventurecreator.style;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ilusr.core.ioc.ServiceManager;

public class ThemeRegistration {

	/**
	 * 
	 * @param manager A @see ServiceManager to register services to.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void register(ServiceManager manager) throws IOException, URISyntaxException {
		Resource resource = new ClassPathResource("themeregistration.xml", ThemeRegistration.class);
		manager.registerServicesFromResource(resource);
	}
}
