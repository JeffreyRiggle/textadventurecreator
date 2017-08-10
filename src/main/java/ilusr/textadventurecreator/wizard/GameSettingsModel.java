package ilusr.textadventurecreator.wizard;

import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.MediaFinder;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Window;
import textadventurelib.persistence.DisplayType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameSettingsModel {

	private final String JAVA = "Java";
	//TODO: Create support for these languages.
	//private final String JAVASCRIPT = "JavaScript";
	//private final String CPP = "C++";
	
	private final MediaFinder mediaFinder;
	
	private TextAdventureProjectPersistence project;
	private SimpleStringProperty gameName;
	private SimpleStringProperty gameDescription;
	private SimpleStringProperty iconLocation;
	private SimpleStringProperty creator;
	private SimpleBooleanProperty standAlone;
	private SimpleBooleanProperty isDev;
	private SelectionAwareObservableList<String> transitionTypes;
	private SimpleStringProperty mediaLocation;
	private SelectionAwareObservableList<String> supportedLanguages;
	private SimpleStringProperty projectLocation;
	private SimpleStringProperty backgroundLocation;
	private SimpleBooleanProperty gameStatesInline;
	private SimpleStringProperty gameStatesLocation;
	private SimpleBooleanProperty playersInline;
	private SimpleStringProperty playersLocation;
	private SimpleBooleanProperty layoutInline;
	private SimpleStringProperty layoutLocation;
	private SimpleObjectProperty<Integer> bufferSize;
	private SimpleBooleanProperty gameInfoValid;
	private LanguageAwareString inlinePlayerText;
	private LanguageAwareString inlineGameText;
	private LanguageAwareString inlineLayoutText;
	private LanguageAwareString browseText;
	private LanguageAwareString bufferText;
	private LanguageAwareString sizeText;
	private LanguageAwareString gameNameText;
	private LanguageAwareString gameDescriptionText;
	private LanguageAwareString gameIconText;
	private LanguageAwareString creatorText;
	private LanguageAwareString gameTypeText;
	private LanguageAwareString hostedText;
	private LanguageAwareString standAloneText;
	private LanguageAwareString isDevText;
	private LanguageAwareString transitionTypeText;
	private LanguageAwareString mediaText;
	private LanguageAwareString projectLocationText;
	private LanguageAwareString backgroundText;
	private LanguageAwareString languageText;
	
	/**
	 * 
	 * @param project A @see TextAdventureProjectPersistence to setup.
	 * @param mediaFinder A @see MediaFinder to find media.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public GameSettingsModel(TextAdventureProjectPersistence project, 
							 MediaFinder mediaFinder,
							 ILanguageService languageService) {
		this.project = project;
		this.mediaFinder = mediaFinder;
		gameName = new SimpleStringProperty();
		gameDescription = new SimpleStringProperty();
		iconLocation = new SimpleStringProperty();
		creator = new SimpleStringProperty();
		standAlone = new SimpleBooleanProperty();
		isDev = new SimpleBooleanProperty();
		transitionTypes = new SelectionAwareObservableList<String>();
		mediaLocation = new SimpleStringProperty();
		supportedLanguages = new SelectionAwareObservableList<String>();
		projectLocation = new SimpleStringProperty();
		backgroundLocation = new SimpleStringProperty();
		gameStatesInline = new SimpleBooleanProperty();
		gameStatesLocation = new SimpleStringProperty();
		playersInline = new SimpleBooleanProperty();
		playersLocation = new SimpleStringProperty();
		layoutInline = new SimpleBooleanProperty();
		layoutLocation = new SimpleStringProperty();
		bufferSize = new SimpleObjectProperty<Integer>();
		gameInfoValid = new SimpleBooleanProperty(false);
		
		inlinePlayerText = new LanguageAwareString(languageService, DisplayStrings.INLINE_PLAYER);
		browseText = new LanguageAwareString(languageService, DisplayStrings.BROWSE);
		inlineGameText = new LanguageAwareString(languageService, DisplayStrings.INLINE_GAME);
		inlineLayoutText = new LanguageAwareString(languageService, DisplayStrings.INLINE_LAYOUT);
		bufferText = new LanguageAwareString(languageService, DisplayStrings.BUFFER);
		sizeText = new LanguageAwareString(languageService, DisplayStrings.SIZE);
		gameNameText = new LanguageAwareString(languageService, DisplayStrings.GAME_NAME);
		gameDescriptionText = new LanguageAwareString(languageService, DisplayStrings.GAME_DESCRIPTION);
		gameIconText = new LanguageAwareString(languageService, DisplayStrings.GAME_ICON);
		creatorText = new LanguageAwareString(languageService, DisplayStrings.CREATOR);
		gameTypeText = new LanguageAwareString(languageService, DisplayStrings.GAME_TYPE);
		hostedText = new LanguageAwareString(languageService, DisplayStrings.HOSTED);
		standAloneText = new LanguageAwareString(languageService, DisplayStrings.STAND_ALONE);
		isDevText = new LanguageAwareString(languageService, DisplayStrings.IS_DEV);
		transitionTypeText = new LanguageAwareString(languageService, DisplayStrings.TRANSITION_TYPE);
		mediaText = new LanguageAwareString(languageService, DisplayStrings.MEDIA);
		projectLocationText = new LanguageAwareString(languageService, DisplayStrings.PROJECT_LOCATION);
		backgroundText = new LanguageAwareString(languageService, DisplayStrings.APPLICATION_BACKGROUND);
		languageText = new LanguageAwareString(languageService, DisplayStrings.LANGUAGE);
		
		initialize();
	}
	
	private void initialize() {
		gameName.addListener((v, o, n) -> {
			project.getGameInfo().gameName(n);
			projectLocation.set(System.getProperty("user.home") + "/TextAdventure/" + n);
			project.getTextAdventure().gameName(n);
			gameInfoValid.set(n != null && !n.isEmpty());
		});
		
		gameDescription.addListener((v, o, n) -> {
			project.getGameInfo().description(n);
		});
		
		creator.addListener((v, o, n) -> {
			project.getGameInfo().creator(n);
		});
		
		iconLocation.addListener((v, o, n) -> {
			project.setIconLocation(n);
		});
		
		standAlone.addListener((v, o, n) -> {
			project.setIsStandAlone(n);
		});
		
		isDev.addListener((v, o, n) -> {
			project.setIsDev(n);
		});
		
		gameDescription.addListener((v, o, n) -> {
			project.getGameInfo().creator(n);
		});
		
		for (DisplayType t : DisplayType.values()) {
			transitionTypes.list().add(t.toString());
		}
		
		transitionTypes.selected().set(transitionTypes.list().get(0));
		
		transitionTypes.selected().addListener((v, o, n) -> {
			if (n == null) {
				return;
			}
			
			project.getTextAdventure().transition().displayType(DisplayType.valueOf(n));
		});
		
		mediaLocation.addListener((v, o, n) -> {
			project.getTextAdventure().transition().mediaLocation(n);
		});
		
		supportedLanguages.list().add(JAVA);
		//TODO: Create support for these languages.
		//supportedLanguages.list().add(JAVASCRIPT);
		//supportedLanguages.list().add(CPP);
		
		supportedLanguages.selected().addListener((v, o, n) -> {
			project.setLanguage(n);
		});
		
		supportedLanguages.selected().set(JAVA);
		
		projectLocation.addListener((v, o, n) -> {
			project.setProjectLocation(n);
		});
		
		backgroundLocation.addListener((v, o, n) -> {
			project.setBackgroundLocation(n);
		});
		
		gameStatesInline.addListener((v, o, n) -> {
			project.getTextAdventure().gameStatesInline(n);
		});
		gameStatesInline.set(true);
		
		gameStatesLocation.addListener((v, o, n) -> {
			project.getTextAdventure().gameStatesLocation(n);
		});
		
		playersInline.addListener((v, o, n) -> {
			project.getTextAdventure().playersInline(n);
		});
		playersInline.set(true);
		
		playersLocation.addListener((v, o, n) -> {
			project.getTextAdventure().playersLocation(n);
		});
		
		layoutInline.addListener((v, o, n) -> {
			project.getTextAdventure().layoutsInline(n);
		});
		layoutInline.set(true);
		
		layoutLocation.addListener((v, o, n) -> {
			project.getTextAdventure().layoutsLocation(n);
		});
		
		bufferSize.addListener((v, o, n) -> {
			project.getTextAdventure().buffer(n);
		});
	}
	
	/**
	 * 
	 * @return The name of the game.
	 */
	public SimpleStringProperty gameName() {
		return gameName;
	}
	
	/**
	 * 
	 * @return The description for the game.
	 */
	public SimpleStringProperty gameDescription() {
		return gameDescription;
	}
	
	/**
	 * 
	 * @return The location of the games icon.
	 */
	public SimpleStringProperty iconLocation() {
		return iconLocation;
	}
	
	/**
	 * 
	 * @return The creator of the game.
	 */
	public SimpleStringProperty creator() {
		return creator;
	}
	
	/**
	 * 
	 * @return If the game is a stand alone (non-hosted) game.
	 */
	public SimpleBooleanProperty standAlone() {
		return standAlone;
	}
	
	/**
	 * 
	 * @return If the game is a dev game.
	 */
	public SimpleBooleanProperty isDev() {
		return isDev;
	}
	
	/**
	 * 
	 * @return The selected transition type for a hosted game.
	 */
	public SelectionAwareObservableList<String> transitionTypes() {
		return transitionTypes;
	}
	
	/**
	 * 
	 * @return The location of the media for the game.
	 */
	public SimpleStringProperty mediaLocation() {
		return mediaLocation;
	}
	
	/**
	 * Attempts to find and set the media for the games media location.
	 * @param window The window to use.
	 */
	public void browseContent(Window window) {
		mediaFinder.setWindow(window);
		mediaFinder.getMediaLocation(mediaLocation);
	}
	
	/**
	 * 
	 * @return The language to build in.
	 */
	public SelectionAwareObservableList<String> supportedLanguages() {
		return supportedLanguages;
	}
	
	/**
	 * 
	 * @return The location to build the project in.
	 */
	public SimpleStringProperty projectLocation() {
		return projectLocation;
	}
	
	/**
	 * 
	 * @return The location of the games background image.
	 */
	public SimpleStringProperty backgroundLocation() {
		return backgroundLocation;
	}
	
	/**
	 * 
	 * @return If game states should be in-lined into the game file.
	 */
	public SimpleBooleanProperty gameStatesInline() {
		return gameStatesInline;
	}
	
	/**
	 * 
	 * @return The location game states should be saved to.
	 */
	public SimpleStringProperty gameStatesLocation() {
		return gameStatesLocation;
	}
	
	/**
	 * 
	 * @return If players should be in-lined into the game file.
	 */
	public SimpleBooleanProperty playersInline() {
		return playersInline;
	}
	
	/**
	 * 
	 * @return The location players should be saved to.
	 */
	public SimpleStringProperty playersLocation() {
		return playersLocation;
	}
	
	/**
	 * 
	 * @return If layouts should be in-lined into the game file.
	 */
	public SimpleBooleanProperty layoutInline() {
		return layoutInline;
	}
	
	/**
	 * 
	 * @return The location layouts should be saved to.
	 */
	public SimpleStringProperty layoutLocation() {
		return layoutLocation;
	}
	
	/**
	 * 
	 * @return The number of active game states to have at any given time.
	 */
	public SimpleObjectProperty<Integer> bufferSize() {
		return bufferSize;
	}
	
	/**
	 * 
	 * @return If the game info is valid or not.
	 */
	public SimpleBooleanProperty gameInfoValid() {
		return gameInfoValid;
	}
	
	/**
	 * 
	 * @return The associated project.
	 */
	public TextAdventureProjectPersistence persistenceObject() {
		return project;
	}
	
	/**
	 * 
	 * @return Display string for in-line player.
	 */
	public SimpleStringProperty inlinePlayerText() {
		return inlinePlayerText;
	}
	
	/**
	 * 
	 * @return Display string for in-line game states.
	 */
	public SimpleStringProperty inlineGameText() {
		return inlineGameText;
	}
	
	/**
	 * 
	 * @return Display string for in-line layout.
	 */
	public SimpleStringProperty inlineLayoutText() {
		return inlineLayoutText;
	}
	
	/**
	 * 
	 * @return Display string for browse.
	 */
	public SimpleStringProperty browseText() {
		return browseText;
	}
	
	/**
	 * 
	 * @return Display string for buffer.
	 */
	public SimpleStringProperty bufferText() {
		return bufferText;
	}
	
	/**
	 * 
	 * @return Display string for size.
	 */
	public SimpleStringProperty sizeText() {
		return sizeText;
	}
	
	/**
	 * 
	 * @return Display string for game name.
	 */
	public SimpleStringProperty gameNameText() {
		return gameNameText;
	}
	
	/**
	 * 
	 * @return Display string for game description.
	 */
	public SimpleStringProperty gameDescriptionText() {
		return gameDescriptionText;
	}
	
	/**
	 * 
	 * @return Display string for game icon.
	 */
	public SimpleStringProperty gameIconText() {
		return gameIconText;
	}
	
	/**
	 * 
	 * @return Display string for creator.
	 */
	public SimpleStringProperty creatorText() {
		return creatorText;
	}
	
	/**
	 * 
	 * @return Display string for game type.
	 */
	public SimpleStringProperty gameTypeText() {
		return gameTypeText;
	}
	
	/**
	 * 
	 * @return Display string for hosted.
	 */
	public SimpleStringProperty hostedText() {
		return hostedText;
	}
	
	/**
	 * 
	 * @return Display string for stand alone.
	 */
	public SimpleStringProperty standAloneText() {
		return standAloneText;
	}
	
	/**
	 * 
	 * @return Display string for is dev.
	 */
	public SimpleStringProperty isDevText() {
		return isDevText;
	}
	
	/**
	 * 
	 * @return Display string for transition type.
	 */
	public SimpleStringProperty transitionTypeText() {
		return transitionTypeText;
	}
	
	/**
	 * 
	 * @return Display string for media.
	 */
	public SimpleStringProperty mediaText() {
		return mediaText;
	}
	
	/**
	 * 
	 * @return Display string for project location.
	 */
	public SimpleStringProperty projectLocationText() {
		return projectLocationText;
	}
	
	/**
	 * 
	 * @return Display string for background.
	 */
	public SimpleStringProperty backgroundText() {
		return backgroundText;
	}
	
	/**
	 * 
	 * @return Display string for language.
	 */
	public SimpleStringProperty languageText() {
		return languageText;
	}
}
