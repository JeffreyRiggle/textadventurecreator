package ilusr.textadventurecreator.debug;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import playerlib.attributes.IAttribute;
import playerlib.core.ValueListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugAttribute implements INamedObject, ValueListener<IAttribute> {

	private IAttribute attribute;
	private Runnable updateListener;
	
	/**
	 * 
	 * @param attribute A @see IAttibute to associate this model with.
	 */
	public DebugAttribute(IAttribute attribute) {
		this.attribute = attribute;
		
		initialize();
	}

	private void initialize() {
		attribute.addChangeListener(this);
	}
	
	@Override
	public String name() {
		return attribute.name();
	}

	@Override
	public String value() {
		if (attribute.value() instanceof Integer) {
			return Integer.toString(attribute.value());
		}
		if (attribute.value() instanceof Double) {
			return Double.toString(attribute.value());
		}
		if (attribute.value() instanceof Float) {
			return Float.toString(attribute.value());
		}
		if (attribute.value() instanceof Boolean) {
			return Boolean.toString(attribute.value());
		}
		
		return attribute.value();
	}

	@Override
	public String description() {
		return attribute.description();
	}

	@Override
	public void updated(Runnable updated) {
		updateListener = updated;
	}

	@Override
	public <T> void changed(IAttribute source, T value) {
		LogRunner.logger().log(Level.INFO, String.format("Attribute: %s changed to: %s", source.name(), source.value()));
		if (updateListener != null) {
			updateListener.run();
		}
	}
}
