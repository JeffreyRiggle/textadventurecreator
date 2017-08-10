package ilusr.textadventurecreator.error;

import java.io.PrintWriter;
import java.io.StringWriter;

import ilusr.core.ioc.ServiceManager;
import ilusr.iroshell.features.ErrorDialogProvider;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.LanguageService;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ErrorProvider implements ErrorDialogProvider {

	@Override
	public Stage create(Throwable e) {
		LanguageService service = ServiceManager.getInstance().<LanguageService>get("LanguageService");
		IEmailService emailService = ServiceManager.getInstance().<IEmailService>get("EmailService");
		Stage s = new Stage();
		ErrorModel model = new ErrorModel(String.format("Message: %s\nStack Trace\n%s", e.getMessage(), getStackString(e)), service, emailService);
		s.setScene(new Scene(new ErrorView(model)));
		s.setTitle(service.getValue(DisplayStrings.ERROR_TITLE));
		return s;
	}
	
	private String getStackString(Throwable e) {
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		e.printStackTrace(printer);
		return writer.toString();
	}

}
