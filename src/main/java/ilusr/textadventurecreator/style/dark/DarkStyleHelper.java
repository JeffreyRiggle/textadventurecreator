package ilusr.textadventurecreator.style.dark;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import ilusr.iroshell.core.StyleArea;
import ilusr.iroshell.dockarea.overlay.OverlayStyleNames;
import ilusr.textadventurecreator.style.StyledComponents;

public class DarkStyleHelper {

	public static Map<String, File> generateMap() throws URISyntaxException {
		Map<String, File> darkStyleMap = new HashMap<String, File>();
		
		darkStyleMap.put(StyleArea.MENU, getResourceFile("menu.css"));
		darkStyleMap.put(StyleArea.STATUS, getResourceFile("status.css"));
		darkStyleMap.put(StyleArea.APP, getResourceFile("app.css"));
		darkStyleMap.put(StyleArea.DOCKAREA, getResourceFile("dockarea.css"));
		darkStyleMap.put(StyleArea.DOCKPANEL, getResourceFile("panel.css"));
		darkStyleMap.put(OverlayStyleNames.DEFAULT_ARROW, getResourceFile("defaultarrow.css"));
		darkStyleMap.put(OverlayStyleNames.HOVER_ARROW, getResourceFile("hoverarrow.css"));
		darkStyleMap.put(OverlayStyleNames.PREVIEW_DROP, getResourceFile("preview.css"));
		darkStyleMap.put(StyleArea.TOOLAREA, getResourceFile("toolarea.css"));
		darkStyleMap.put(StyledComponents.WIZARD_AREA, getResourceFile("wizard.css"));
		darkStyleMap.put(StyledComponents.WIZARD_VIEW, getResourceFile("wizardview.css"));
		darkStyleMap.put(StyledComponents.ABOUT, getResourceFile("about.css"));
		darkStyleMap.put(StyledComponents.REPORT, getResourceFile("reportissue.css"));
		darkStyleMap.put(StyledComponents.DIALOG, getResourceFile("dialog.css"));
		darkStyleMap.put(StyledComponents.SETTINGS, getResourceFile("settings.css"));
		darkStyleMap.put(StyledComponents.LIBRARY, getResourceFile("libraryview.css"));
		darkStyleMap.put(StyledComponents.LIBRARY_ITEM, getResourceFile("libraryitem.css"));
		darkStyleMap.put(StyledComponents.EXPLORER, getResourceFile("explorer.css"));
		darkStyleMap.put(StyledComponents.GAME_STATE, getResourceFile("gamestate.css"));
		darkStyleMap.put(StyledComponents.OPTION, getResourceFile("option.css"));
		darkStyleMap.put(StyledComponents.TRIGGER, getResourceFile("trigger.css"));
		darkStyleMap.put(StyledComponents.MACRO, getResourceFile("macro.css"));
		darkStyleMap.put(StyledComponents.PLAYER, getResourceFile("player.css"));
		darkStyleMap.put(StyledComponents.BODY_PART, getResourceFile("bodypart.css"));
		darkStyleMap.put(StyledComponents.FINDER, getResourceFile("finder.css"));
		darkStyleMap.put(StyledComponents.INVENTORY, getResourceFile("inventory.css"));
		darkStyleMap.put(StyledComponents.ITEM, getResourceFile("item.css"));
		darkStyleMap.put(StyledComponents.EQUIPMENT, getResourceFile("equip.css"));
		darkStyleMap.put(StyledComponents.ITEM_SELECTOR, getResourceFile("itemselector.css"));
		darkStyleMap.put(StyledComponents.DEBUG, getResourceFile("debug.css"));
		darkStyleMap.put(StyledComponents.LAYOUT, getResourceFile("layout.css"));
		darkStyleMap.put(StyledComponents.STYLE_CREATOR, getResourceFile("stylecreator.css"));
		
		return darkStyleMap;
	}
	
	private static File getResourceFile(String r) throws URISyntaxException {
		String res = DarkStyleHelper.class.getResource(r).toURI().getSchemeSpecificPart();
		return new File(res);
	}
}
