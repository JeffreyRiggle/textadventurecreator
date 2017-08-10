package ilusr.textadventurecreator.views.trigger;

import java.util.List;
import java.util.logging.Level;

import ilusr.core.interfaces.Callback;
import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MultiTriggerModel {

	private final IDialogService dialogService;
	private final IDialogProvider dialogProvider;
	private final TriggerViewFactory factory;
	private final ILanguageService languageService;
	private MultiPartTriggerPersistenceObject trigger;
	private ObservableList<TriggerPersistenceObject> triggers;
	private TriggerPersistenceObject addTriggerKey;
	private LanguageAwareString triggersText;
	
	/**
	 * 
	 * @param trigger The trigger to bind to.
	 * @param service A @see IDialogService to display dialogs.
	 * @param factory A @see TriggerViewFactory to create trigger views.
	 * @param languageService A @see LanguageService to provide display strings.
	 */
	public MultiTriggerModel(MultiPartTriggerPersistenceObject trigger, 
							 IDialogService service,
							 IDialogProvider dialogProvider,
							 TriggerViewFactory factory,
							 ILanguageService languageService) {
		this.trigger = trigger;
		this.dialogService = service;
		this.dialogProvider = dialogProvider;
		this.factory = factory;
		this.languageService = languageService;
		triggers = FXCollections.observableArrayList();
		triggersText = new LanguageAwareString(languageService, DisplayStrings.TRIGGERS);
		
		try {
			addTriggerKey = new TextTriggerPersistenceObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initialize();
		bind();
	}
	
	private void initialize() {
		for (TriggerPersistenceObject trig : trigger.triggers()) {
			triggers.add(trig);
		}
	}
	
	private void bind() {
		triggers.addListener((Change<? extends TriggerPersistenceObject> c) -> {
			if (!c.next()) {
				return;
			}
			
			List<? extends TriggerPersistenceObject> rTrigger = c.getRemoved();
			List<? extends TriggerPersistenceObject> aTrigger = c.getList();
			
			for (TriggerPersistenceObject model : rTrigger) {
				if (aTrigger.contains(model)) {
					continue;
				}
				
				trigger.removeTrigger(model);
			}
		});
	}
	
	/**
	 * 
	 * @return The Triggers associated with this multi part trigger.
	 */
	public ObservableList<TriggerPersistenceObject> triggers() {
		return triggers;
	}
	
	/**
	 * 
	 * @return A trigger to be used as an add action.
	 */
	public TriggerPersistenceObject addTriggerKey() {
		return addTriggerKey;
	}
	
	/**
	 * 
	 * @return A Callback to run to edit a trigger.
	 */
	public Callback<TriggerPersistenceObject> getEditTriggerAction() {
		return (trig) -> {
			Dialog dialog = dialogProvider.create(factory.create(new TriggerModel(trig, languageService)));
			
			LogRunner.logger().log(Level.INFO, "Editing trigger.");
			dialogService.displayModal(dialog);
		};
	}
	
	/**
	 * 
	 * @return An EventHandler to run to add a trigger.
	 */
	public EventHandler<ActionEvent> getAddTriggerAction() {
		return (e) -> {
			TriggerModel model = new TriggerModel(languageService);
			Dialog dialog = dialogProvider.create(factory.create(model));
			
			dialog.setOnComplete(() -> {
				TriggerPersistenceObject trig = model.persistenceObject().get();
				LogRunner.logger().log(Level.INFO, "Adding trigger.");
				triggers.add(trig);
				trigger.addTrigger(trig);
			});
			
			LogRunner.logger().log(Level.INFO, "Attempting to add a trigger.");
			dialogService.displayModal(dialog);
		};
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public MultiPartTriggerPersistenceObject getPersistenceObject() {
		return trigger;
	}
	
	/**
	 * 
	 * @return Display string for triggers.
	 */
	public SimpleStringProperty triggersText() {
		return triggersText;
	}
}
