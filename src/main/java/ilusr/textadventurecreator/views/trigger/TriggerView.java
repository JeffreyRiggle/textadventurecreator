package ilusr.textadventurecreator.views.trigger;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.core.ViewSwitcher;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.style.StyledComponents;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private ComboBox<String> types;
	
	@FXML
	private ViewSwitcher<String> switcher;
	
	@FXML
	private Label typeLabel;
	
	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;
	private final TriggerViewFactory factory;
	private final List<PlayerPersistenceObject> players;
	private final ILanguageService languageService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private TriggerModel model;
	private TextTriggerViewProvider textProvider;
	private PlayerTriggerViewProvider playerProvider;
	private ScriptTriggerViewProvider scriptProvider;
	private MultiTriggerViewProvider multiProvider;
	
	/**
	 * 
	 * @param model The model to bind to.
	 * @param textProvider A @see TextTriggerViewProvider to provide text trigger views.
	 * @param playerProvider A @see PlayerTriggerViewProvider to provide player trigger views.
	 * @param scriptProvider A @see ScriptTriggerViewProvider to provide script trigger views.
	 * @param multiProvider A @see MultiTriggerViewProvider to provide multi trigger views.
	 * @param provider A @see TextAdventureProvider to provide the current text adventure.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public TriggerView(TriggerModel model, 
					   TextTriggerViewProvider textProvider, 
					   PlayerTriggerViewProvider playerProvider,
					   ScriptTriggerViewProvider scriptProvider,
					   MultiTriggerViewProvider multiProvider,
					   TextAdventureProvider provider,
					   IDialogService dialogService,
					   IDialogProvider dialogProvider,
					   TriggerViewFactory factory,
					   ILanguageService languageService,
					   IStyleContainerService styleService,
					   InternalURLProvider urlProvider) {
		this(model, textProvider, 
			 playerProvider, scriptProvider, 
			 multiProvider, provider.getTextAdventureProject().getTextAdventure().players(),
			 dialogService, dialogProvider, factory, languageService, styleService, urlProvider);
	}
	
	/**
	 * 
	 * @param model The model to bind to.
	 * @param textProvider A @see TextTriggerViewProvider to provide text trigger views.
	 * @param playerProvider A @see PlayerTriggerViewProvider to provide player trigger views.
	 * @param scriptProvider A @see ScriptTriggerViewProvider to provide script trigger views.
	 * @param multiProvider A @see MultiTriggerViewProvider to provide multi trigger views.
	 * @param players The players avaliabe to these triggers.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public TriggerView(TriggerModel model, 
			   TextTriggerViewProvider textProvider, 
			   PlayerTriggerViewProvider playerProvider,
			   ScriptTriggerViewProvider scriptProvider,
			   MultiTriggerViewProvider multiProvider,
			   List<PlayerPersistenceObject> players,
			   IDialogService dialogService,
			   IDialogProvider dialogProvider,
			   TriggerViewFactory factory,
			   ILanguageService languageService,
			   IStyleContainerService styleService,
			   InternalURLProvider urlProvider) {
		this.model = model;
		this.textProvider = textProvider;
		this.playerProvider = playerProvider;
		this.scriptProvider = scriptProvider;
		this.multiProvider = multiProvider;
		this.players = players;
		this.dialogService = dialogService;
		this.dialogProvider = dialogProvider;
		this.factory = factory;
		this.languageService = languageService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TriggerView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		typeLabel.textProperty().bind(model.typeText());
		types.itemsProperty().set(model.types().list());
		setupViews();
		types.valueProperty().addListener((v, o, n) -> {
			switcher.switchView(n);
			updateModel(n);
		});
		types.valueProperty().set(model.types().selected().get());
		setupStyles();
	}
	
	private void setupViews() {
		if (model.persistenceObject().get() != null) {
			bindPersistence();
		}
		switcher.addView(model.multiPartId(), multiProvider);
		switcher.addView(model.textId(), textProvider);
		switcher.addView(model.playerId(), playerProvider);
		switcher.addView(model.scriptId(), scriptProvider);
	}
	
	private void bindPersistence() {
		TriggerPersistenceObject trig = model.persistenceObject().get();
		
		if (trig instanceof TextTriggerPersistenceObject) {
			textProvider = new TextTriggerViewProvider(new TextTriggerModel((TextTriggerPersistenceObject)trig, model.getLanguageService()));
		} else if (trig instanceof PlayerTriggerPersistenceObject) {
			playerProvider = new PlayerTriggerViewProvider((PlayerTriggerPersistenceObject)trig, players, model.getLanguageService());
		} else if (trig instanceof ScriptedTriggerPersistenceObject) {
			scriptProvider = new ScriptTriggerViewProvider(new ScriptedTriggerModel((ScriptedTriggerPersistenceObject)trig, model.getLanguageService()));
		} else if (trig instanceof MultiPartTriggerPersistenceObject) {
			//TODO: Added IDialogProvider need to start using that.
			multiProvider = new MultiTriggerViewProvider(new MultiTriggerModel((MultiPartTriggerPersistenceObject)trig, dialogService, dialogProvider, factory, languageService));
		}
	}
	
	private void updateModel(String type) {
		if (type.equals(model.multiPartId())) {
			model.persistenceObject().set(multiProvider.getView().triggerPersistenceObject());
		} else if (type.equals(model.textId())) {
			model.persistenceObject().set(textProvider.getView().triggerPersistenceObject());
		} else if (type.equals(model.playerId())) {
			model.persistenceObject().set(playerProvider.getView().triggerPersistenceObject());
		} else if (type.equals(model.scriptId())) {
			model.persistenceObject().set(scriptProvider.getView().triggerPersistenceObject());
		}
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "triggerstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.TRIGGER), this, false);
		
		String style = styleService.get(StyledComponents.TRIGGER);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}

	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}
}
