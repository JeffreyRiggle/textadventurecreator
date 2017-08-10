package ilusr.textadventurecreator.debug;

import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import playerlib.characteristics.ICharacteristic;
import playerlib.core.ValueListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class DebugCharacteristic implements INamedObject, ValueListener<ICharacteristic> {

	private ICharacteristic characteristic;
	private Runnable updateListener;
	
	/**
	 * 
	 * @param characteristic A @see ICharacteristic to associate with this model.
	 */
	public DebugCharacteristic(ICharacteristic characteristic) {
		this.characteristic = characteristic;
		
		initialize();
	}
	
	private void initialize() {
		characteristic.addChangeListener(this);
	}
	
	@Override
	public String name() {
		return characteristic.name();
	}

	@Override
	public String value() {
		if (characteristic.value() instanceof Integer) {
			return Integer.toString(characteristic.value());
		}
		if (characteristic.value() instanceof Double) {
			return Double.toString(characteristic.value());
		}
		if (characteristic.value() instanceof Float) {
			return Float.toString(characteristic.value());
		}
		if (characteristic.value() instanceof Boolean) {
			return Boolean.toString(characteristic.value());
		}
		
		return characteristic.value();
	}

	@Override
	public String description() {
		return characteristic.description();
	}

	@Override
	public void updated(Runnable updated) {
		updateListener = updated;
	}

	@Override
	public <T> void changed(ICharacteristic source, T value) {
		LogRunner.logger().log(Level.INFO, String.format("Characteristic: %s changed to: %s", source.name(), source.value()));
		if (updateListener != null) {
			updateListener.run();
		}
	}
}
