package ilusr.textadventurecreator.views.action;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class RemovePlayerPropertyView extends PlayerDataView {

	/**
	 * 
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public RemovePlayerPropertyView(ILanguageService languageService) {
		super.getChildren().add(new Label(languageService.getValue(DisplayStrings.REMOVE_PLAYER_PROPERTY)));
	}

	@Override
	public SimpleObjectProperty<? extends Object> getData() {
		return null;
	}

}
