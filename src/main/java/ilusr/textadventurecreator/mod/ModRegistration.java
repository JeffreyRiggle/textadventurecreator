package ilusr.textadventurecreator.mod;

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
public class ModRegistration {

	/**
	 * 
	 * @param manager A @see ServiceManager to register to.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void register(ServiceManager manager) throws IOException, URISyntaxException {
		Resource resource = new ClassPathResource("modregistrations.xml", ModRegistration.class);
		manager.registerServicesFromResource(resource);
	}
}
