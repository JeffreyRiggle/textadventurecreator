package ilusr.textadventurecreator.style;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class Theme {
	
	private String name;
	private Map<String, File> styles;
	private List<ILocalThemeListener> listeners;
	private Timer updateThrottle;
	private Map<String, File> updateStyles;
	
	public Theme(String name) {
		this(name, new HashMap<>());
	}
	
	public Theme(String name, Map<String, File> styles) {
		this.name = name;
		this.styles = styles;
		updateStyles = new HashMap<>();
		listeners = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, File> styles() {
		return styles;
	}
	
	public void addListener(ILocalThemeListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ILocalThemeListener listener) {
		listeners.remove(listener);
	}
	
	public void update(String style, File file) {
		updateStyles.put(style, file);
		
		if (updateThrottle != null) {
			return;
		}
		
		if (updateThrottle == null) {
			updateThrottle = new Timer();
		}
		
		updateThrottle.schedule(new UpdateTask(() -> {
			updateListeners();
			updateThrottle = null;
		}), 500);
		
		
	}
	
	private void updateListeners() {
		for (Entry<String, File> entry : updateStyles.entrySet()) {
			for (ILocalThemeListener listener : listeners) {
				listener.updated(entry.getKey(), entry.getValue());
			}
		}
		
		updateStyles.clear();
	}
	
	private class UpdateTask extends TimerTask {
		
		private Runnable runnable;
		
		public UpdateTask(Runnable runnable) {
			this.runnable = runnable;
		}
		
		@Override
		public void run() {
			runnable.run();
		}
	}
}
