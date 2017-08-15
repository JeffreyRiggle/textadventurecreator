package ilusr.textadventurecreator.debug;

import ilusr.logrunner.LogRunner;
import playerlib.core.ValueListener;
import playerlib.items.IProperty;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugProperty implements INamedObject, ValueListener<IProperty> {

	private IProperty property;
	private Runnable updateListener;
	
	/**
	 * 
	 * @param property The @see IProperty to assoicate with this model.
	 */
	public DebugProperty(IProperty property) {
		this.property = property;
		
		initialize();
	}
	
	private void initialize() {
		property.addChangeListener(this);
	}
	
	@Override
	public String name() {
		return property.name();
	}

	@Override
	public String value() {
		if (property.value() instanceof Integer) {
			return Integer.toString(property.value());
		}
		if (property.value() instanceof Double) {
			return Double.toString(property.value());
		}
		if (property.value() instanceof Float) {
			return Float.toString(property.value());
		}
		if (property.value() instanceof Boolean) {
			return Boolean.toString(property.value());
		}
		
		return property.value();
	}

	@Override
	public String description() {
		return property.description();
	}

	@Override
	public void updated(Runnable updated) {
		updateListener = updated;
	}

	@Override
	public <T> void changed(IProperty source, T value) {
		LogRunner.logger().info(String.format("Property: %s, updated to %s", source.name(), value));
		if (updateListener != null) {
			updateListener.run();
		}
	}
}
