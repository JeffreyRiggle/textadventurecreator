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
import ilusr.textadventurecreator.shell.IInitialize;

public class LocalThemeFinder implements IInitialize, IDirectoryListener {

	private final ThemeService themeService;
	private DirectoryWatcher watcher;
	private Map<String, Theme> themes;

	public LocalThemeFinder(ThemeService themeService) throws IOException {
		this.themeService = themeService;

		themes = new HashMap<>();

		File themeDir = new File(System.getProperty("user.home") + "/ilusr/themes");
		if (!themeDir.exists()) {
			themeDir.mkdir();
		}

		watcher = new DirectoryWatcher(System.getProperty("user.home") + "/ilusr/themes");
	}

	@Override
	public void initialize() {
		findInitialThemes();

		try {
			watcher.startWatching(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void findInitialThemes() {
		File dir = new File(watcher.watchedDirectory());
		File[] listOfFiles = dir.listFiles();

		for (File file : listOfFiles) {
			if (!file.isDirectory()) continue;

			addTheme(file);
		}
	}

	@Override
	public void directoryUpdated(WatchEvent<?> event, UpdateType type) {
		switch(type) {
		case Added:
			processAdd(event);
			break;
		case Modified:
			break;
		case Removed:
			processRemove(event);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void processAdd(WatchEvent<?> event) {
		WatchEvent<Path> path = (WatchEvent<Path>)event;

		File file = path.context().toFile();

		if (!file.isDirectory()) {
			return;
		}

		addTheme(file);
	}

	private void addTheme(File file) {
		try {
			LocalTheme theme = new LocalTheme(file.getName(), new DirectoryWatcher(file.getAbsolutePath()));
			themeService.addTheme(theme);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void processRemove(WatchEvent<?> event) {
		WatchEvent<Path> path = (WatchEvent<Path>)event;

		File file = path.context().toFile();

		if (!themes.containsKey(file.getName())) {
			return;
		}

		themeService.removeTheme(themes.get(file.getName()));
		themes.remove(file.getName());
	}
}
