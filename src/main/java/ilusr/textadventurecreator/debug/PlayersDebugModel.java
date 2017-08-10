package ilusr.textadventurecreator.debug;

import java.util.List;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayersDebugModel {
	
	private ObservableList<PlayerDebugModel> players;
	private LanguageAwareString playersText;
	private LanguageAwareString noPlayersText;
	
	/**
	 * 
	 * @param players A @see List of type @See PlayerDebugModel.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public PlayersDebugModel(List<PlayerDebugModel> players, ILanguageService languageService) {
		this.players = FXCollections.observableArrayList(players);
		playersText = new LanguageAwareString(languageService, DisplayStrings.PLAYERS);
		noPlayersText = new LanguageAwareString(languageService, DisplayStrings.NO_PLAYERS);
	}
	
	/**
	 * 
	 * @return A @see ObservableList of type @see PlayerDebugModel representing the players.
	 */
	public ObservableList<PlayerDebugModel> players() {
		return players;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for Players.
	 */
	public SimpleStringProperty playersText() {
		return playersText;
	}
	
	/**
	 * 
	 * @return A @see SimpleStringProperty representing the display string for No Players.
	 */
	public SimpleStringProperty noPlayersText() {
		return noPlayersText;
	}
}