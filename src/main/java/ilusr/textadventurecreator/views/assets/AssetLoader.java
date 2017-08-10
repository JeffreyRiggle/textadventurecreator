package ilusr.textadventurecreator.views.assets;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AssetLoader {

	/**
	 * 
	 * @param resource The resource to get.
	 * @return The resource
	 */
	public static InputStream getStream(String resource) {
		return AssetLoader.class.getResourceAsStream(resource);
	}
	
	/**
	 * 
	 * @param resource The resource to get.
	 * @return A link to the resource.
	 */
	public static URL getResourceURL(String resource) {
		return AssetLoader.class.getResource(resource);
	}
}
