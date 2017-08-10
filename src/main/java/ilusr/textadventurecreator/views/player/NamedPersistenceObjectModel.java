package ilusr.textadventurecreator.views.player;

import ilusr.persistencelib.configuration.ValueTypes;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleStringProperty;
import textadventurelib.persistence.player.NamedPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class NamedPersistenceObjectModel {

	private final String STRING_TYPE = "Word";
	private final String INT_TYPE = "Number";
	private final String DOUBLE_TYPE = "Decimal";
	private final String BOOLEAN_TYPE = "True/False";
	
	private NamedPersistenceObject persistence;
	private SelectionAwareObservableList<String> types;
	private SimpleStringProperty value;
	private SimpleStringProperty name;
	private SimpleStringProperty description;
	
	/**
	 * 
	 * @param persistence The @see NamedPersistenceObjec to bind to.
	 */
	public NamedPersistenceObjectModel(NamedPersistenceObject persistence) {
		this.persistence = persistence;
		types = new SelectionAwareObservableList<String>();
		value = new SimpleStringProperty();
		name = new SimpleStringProperty(persistence.objectName());
		description = new SimpleStringProperty(persistence.description());
		
		initialize();
	}
	
	private void initialize() {
		types.list().add(STRING_TYPE);
		types.list().add(INT_TYPE);
		types.list().add(DOUBLE_TYPE);
		types.list().add(BOOLEAN_TYPE);
		initalizeTypeAndValue();
		
		types.selected().addListener((v, o, n) -> {
			if (value.get() == null || value.get().isEmpty()) {
				return;
			}
			
			applyValues(n);
		});
		
		value.addListener((v, o, n) -> {
			if (n == null || n.isEmpty()) {
				return;
			}
			
			applyValues(types.selected().get());
		});
		
		name.addListener((v, o, n) -> {
			persistence.objectName(n);
		});
		
		description.addListener((v, o, n) -> {
			persistence.description(n);
		});
	}
	
	private void initalizeTypeAndValue() {
		switch (persistence.valueType()) {
			case ValueTypes.BooleanType:
				types.selected().set(BOOLEAN_TYPE);
				value.set(Boolean.toString((Boolean)persistence.objectValue()));
				break;
			case ValueTypes.IntegerType:
				types.selected().set(INT_TYPE);
				value.set(Integer.toString((Integer)persistence.objectValue()));
				break;
			case ValueTypes.FloatType:
			case ValueTypes.DoubleType:
				types.selected().set(DOUBLE_TYPE);
				value.set(Double.toString((Double)persistence.objectValue()));
				break;
			case ValueTypes.ObjectType:
			case ValueTypes.StringType:
			default:
				types.selected().set(STRING_TYPE);
				if (persistence.objectValue() != null) {
					value.set(persistence.objectValue().toString());
				}
				break;
		}
	}
	
	private void applyValues(String type) {
		try {
			switch (type) {
				case STRING_TYPE:
					persistence.objectValue(value.get());
					break;
				case INT_TYPE:
					persistence.<Integer>objectValue(Integer.parseInt(value.get()));
					break;
				case DOUBLE_TYPE:
					persistence.<Double>objectValue(Double.parseDouble(value.get()));
					break;
				case BOOLEAN_TYPE:
					persistence.<Boolean>objectValue(Boolean.parseBoolean(value.get()));
					break;
			}
		} catch (Exception e) {
			//TODO Validation error.
		}
	}
	
	/**
	 * 
	 * @return The value associated with this object.
	 */
	public SimpleStringProperty value() {
		return value;
	}
	
	/**
	 * 
	 * @return The name associated with this object.
	 */
	public SimpleStringProperty name() {
		return name;
	}
	
	/**
	 * 
	 * @return The description associated with this object.
	 */
	public SimpleStringProperty description() {
		return description;
	}
	
	/**
	 * 
	 * @return The value types available for this object.
	 */
	public SelectionAwareObservableList<String> types() {
		return types;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public NamedPersistenceObject getPersistence() {
		return persistence;
	}
}
