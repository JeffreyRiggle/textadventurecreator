package ilusr.textadventurecreator.codegen.webfiles;

import java.io.IOException;

public class WebResourceFileLoader {
	public byte[] getResource(String file) throws IOException {
		return getClass().getResourceAsStream(file).readAllBytes();
	}
}
