package ilusr.textadventurecreator.style.dark;

import java.net.URISyntaxException;

import ilusr.textadventurecreator.style.Theme;

public class DarkTheme extends Theme {

	public DarkTheme() throws URISyntaxException {
		super("Dark", DarkStyleHelper.generateMap());
	}
}
