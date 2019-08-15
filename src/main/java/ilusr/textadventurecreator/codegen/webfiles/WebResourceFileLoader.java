package ilusr.textadventurecreator.codegen.webfiles;

import java.io.File;
import java.net.URISyntaxException;

public class WebResourceFileLoader {
	public File getResource(String file) throws URISyntaxException {
		return new File(getClass().getResource(file).toURI().getSchemeSpecificPart());
	}
}
