package ilusr.textadventurecreator.views.action;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ChangePlayerPropertyView extends PlayerDataView {

	private TextField text;
	private SimpleObjectProperty<String> textValue;
	
	/**
	 * 
	 * @param initialValue The initial value of the property.
	 */
	public ChangePlayerPropertyView(String initialValue) {
		text = new TextField();
		super.getChildren().add(text);
		textValue = new SimpleObjectProperty<String>();
		textValue.bindBidirectional(text.textProperty());
		text.setText(initialValue);
	}
	
	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return textValue;
	}
}
