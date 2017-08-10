package ilusr.textadventurecreator.style;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;

import ilusr.core.io.DirectoryWatcher;
import ilusr.core.io.IDirectoryListener;
import ilusr.core.io.UpdateType;
import ilusr.iroshell.core.StyleArea;
import ilusr.iroshell.dockarea.overlay.OverlayStyleNames;

public class LocalTheme extends Theme implements IDirectoryListener {

	private Map<String, String> fileMap;
	private DirectoryWatcher watcher;
	private Path directory;
	
	public LocalTheme(String name, DirectoryWatcher watcher) throws IOException {
		super(name);
		this.directory = new File(watcher.watchedDirectory()).toPath();
		this.watcher = watcher;
		
		fileMap = new HashMap<>();
		initialize();
	}

	private void initialize() throws IOException {
		fileMap.put("menu.css", StyleArea.MENU);
		fileMap.put("status.css", StyleArea.STATUS);
		fileMap.put("app.css", StyleArea.APP);
		fileMap.put("dockarea.css", StyleArea.DOCKAREA);
		fileMap.put("panel.css", StyleArea.DOCKPANEL);
		fileMap.put("defaultarrow.css", OverlayStyleNames.DEFAULT_ARROW);
		fileMap.put("hoverarrow.css", OverlayStyleNames.HOVER_ARROW);
		fileMap.put("preview.css", OverlayStyleNames.PREVIEW_DROP);
		fileMap.put("toolarea.css", StyleArea.TOOLAREA);
		fileMap.put("wizard.css", StyledComponents.WIZARD_AREA);
		fileMap.put("wizardview.css", StyledComponents.WIZARD_VIEW);
		fileMap.put("about.css", StyledComponents.ABOUT);
		fileMap.put("reportissue.css", StyledComponents.REPORT);
		fileMap.put("dialog.css", StyledComponents.DIALOG);
		fileMap.put("settings.css", StyledComponents.SETTINGS);
		fileMap.put("libraryview.css", StyledComponents.LIBRARY);
		fileMap.put("libraryitem.css", StyledComponents.LIBRARY_ITEM);
		fileMap.put("explorer.css", StyledComponents.EXPLORER);
		fileMap.put("gamestate.css", StyledComponents.GAME_STATE);
		fileMap.put("option.css", StyledComponents.OPTION);
		fileMap.put("trigger.css", StyledComponents.TRIGGER);
		fileMap.put("macro.css", StyledComponents.MACRO);
		fileMap.put("player.css", StyledComponents.PLAYER);
		fileMap.put("bodypart.css", StyledComponents.BODY_PART);
		fileMap.put("finder.css", StyledComponents.FINDER);
		fileMap.put("inventory.css", StyledComponents.INVENTORY);
		fileMap.put("item.css", StyledComponents.ITEM);
		fileMap.put("equip.css", StyledComponents.EQUIPMENT);
		fileMap.put("itemselector.css", StyledComponents.ITEM_SELECTOR);
		fileMap.put("debug.css", StyledComponents.DEBUG);
		fileMap.put("layout.css", StyledComponents.LAYOUT);
		fileMap.put("stylecreator.css", StyledComponents.STYLE_CREATOR);
		
		findInitialThemes();
		watcher.startWatching(this);
	}
	
	private void findInitialThemes() {
		File dir = new File(watcher.watchedDirectory());
		File[] listOfFiles = dir.listFiles();
		
		if (listOfFiles == null) {
			return;
		}
		
		for (File file : listOfFiles) {
			if (file.isDirectory()) continue;
			
			addOrUpdateStyle(file);
		}
	}
	
	@Override
	public void directoryUpdated(WatchEvent<?> event, UpdateType type) {
		switch(type) {
			case Added:
				processAdd(event);
				break;
			case Modified:
				processUpdate(event);
				break;
			case Removed:
				processRemove(event);
				break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processAdd(WatchEvent<?> event) {
		WatchEvent<Path> path = (WatchEvent<Path>)event;
		
		Path fn = directory.resolve(path.context());
		addOrUpdateStyle(fn.toFile());
	}
	
	@SuppressWarnings("unchecked")
	private void processUpdate(WatchEvent<?> event) {
		WatchEvent<Path> path = (WatchEvent<Path>)event;
		
		Path fn = directory.resolve(path.context());
		addOrUpdateStyle(fn.toFile());
	}
	
	@SuppressWarnings("unchecked")
	private void processRemove(WatchEvent<?> event) {
		WatchEvent<Path> path = (WatchEvent<Path>)event;
		
		File file = path.context().toFile();
		
		if (!fileMap.containsKey(file.getName())) {
			return;
		}
		
		String style = fileMap.get(file.getName());
		styles().remove(style);
		update(style, null);
	}
	
	private void addOrUpdateStyle(File file) {
		String fileName = file.getName();
		if (!fileMap.containsKey(fileName)) {
			return;
		}
		
		String style = fileMap.get(fileName);
		if (!styles().containsKey(fileName)) {
			styles().put(style, file);
		}
		
		update(style, file);
	}
}
