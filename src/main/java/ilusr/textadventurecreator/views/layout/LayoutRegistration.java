package ilusr.textadventurecreator.views.layout;

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
public class LayoutRegistration {
	
	/**
	 * 
	 * @param manager The @see ServiceManager to register with.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void register(ServiceManager manager) throws IOException, URISyntaxException {
		Resource resource = new ClassPathResource("layoutregistrations.xml", LayoutRegistration.class);
		manager.registerServicesFromResource(resource);
	}
}
