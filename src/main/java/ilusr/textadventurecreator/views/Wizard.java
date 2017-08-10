package ilusr.textadventurecreator.views;

import java.util.List;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The type of object that is being setup in the wizard.
 */
public class Wizard<T> extends Stage {
	
	private WizardView<T> view;
	private IFinishListener<T> finishListener;
	private ICancelListener cancelListener;
	
	/**
	 * 
	 * @param pages A list of pages to step through.
	 * @param result The object to modify in the wizard.
	 */
	public Wizard(List<WizardPage<T>> pages, T result) {
		this(pages, result, null);
	}
	
	/**
	 * 
	 * @param pages A list of pages to step through.
	 * @param result The object to modify in the wizard.
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public Wizard(List<WizardPage<T>> pages, T result, ILanguageService languageService) {
		this(pages, result, languageService, null, null);
	}
	
	/**
	 * 
	 * @param pages A list of pages to step through.
	 * @param result The object to modify in the wizard.
	 * @param languageService A @see LanguageService to use for display strings.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 */
	public Wizard(List<WizardPage<T>> pages, 
				  T result, 
				  ILanguageService languageService,
				  IStyleContainerService styleService,
				  InternalURLProvider urlProvider) {
		view = new WizardView<T>(pages, result, languageService, styleService, urlProvider);
		view.setOnFinish((v) -> {
			finish(v);
		});
		
		view.setOnClose(() -> {
			cancel();
		});
		
		super.setScene(new Scene(view));
	}
	
	/**
	 * 
	 * @param listener The listener to notify when the wizard has finished successfully.
	 */
	public void setOnFinish(IFinishListener<T> listener) {
		finishListener = listener;
	}
	
	/**
	 * 
	 * @param listener The listener to notify when the wizard has been canceled.
	 */
	public void setOnCancel(ICancelListener listener) {
		cancelListener = listener;
	}
	
	private void finish(T result) {
		if (finishListener != null) {
			finishListener.finish(result);
		}
		
		super.close();
	}
	
	private void cancel() {
		if (cancelListener != null) {
			cancelListener.cancel();
		}
		
		super.close();
	}
}
