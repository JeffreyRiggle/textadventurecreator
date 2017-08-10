package ilusr.textadventurecreator.views;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import ilusr.core.ioc.ServiceManager;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ViewRegistration {

	/**
	 * 
	 * @param manager A @see ServiceManager to register to.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void register(ServiceManager manager) throws IOException, URISyntaxException {
		Resource resource = new ClassPathResource("viewregistrations.xml", ViewRegistration.class);
		manager.registerServicesFromResource(resource);
	}
}
