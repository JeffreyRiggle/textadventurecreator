package ilusr.textadventurecreator.error;

import java.util.logging.Level;

import javax.mail.Message.RecipientType;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.beans.property.SimpleStringProperty;

public class ErrorModel {

	private final IEmailService emailService;
	private SimpleStringProperty exceptionString;
	private SimpleStringProperty errorText;
	private SimpleStringProperty continueText;
	private SimpleStringProperty reportText;
	private SimpleStringProperty exitText;
	
	/**
	 * Base ctor.
	 */
	public ErrorModel(ILanguageService languageService, IEmailService emailService) {
		this(new String(), languageService, emailService);
	}
	
	/**
	 * 
	 * @param exception The exception to be shown.
	 */
	public ErrorModel(String exception, ILanguageService languageService, IEmailService emailService) {
		this.emailService = emailService;
		exceptionString = new SimpleStringProperty(exception);
		errorText = new SimpleStringProperty(languageService.getValue(DisplayStrings.ERROR_TEXT));
		continueText = new SimpleStringProperty(languageService.getValue(DisplayStrings.CONTINUE));
		reportText = new SimpleStringProperty(languageService.getValue(DisplayStrings.REPORT));
		exitText = new SimpleStringProperty(languageService.getValue(DisplayStrings.EXIT));
	}
	
	/**
	 * 
	 * @return The exception to be shown.
	 */
	public SimpleStringProperty exceptionProperty() {
		return exceptionString;
	}
	
	/**
	 * 
	 * @return The error display string.
	 */
	public SimpleStringProperty errorText() {
		return errorText;
	}
	
	/**
	 * 
	 * @return The continue display string.
	 */
	public SimpleStringProperty continueText() {
		return continueText;
	}
	
	/**
	 * 
	 * @return The report display string.
	 */
	public SimpleStringProperty reportText() {
		return reportText;
	}
	
	/**
	 * 
	 * @return The exit display string.
	 */
	public SimpleStringProperty exitText() {
		return exitText;
	}
	
	/**
	 * Sends a report.
	 * 
	 * @param callback A @see Runnable to execute after the report has been sent.
	 */
	public void sendReport(Runnable callback) {
		new Thread(() -> {
			sendReportImpl(callback);
		}).start();
	}
	
	private void sendReportImpl(Runnable callback) {
		LogRunner.logger().log(Level.INFO, "Creating report for error.");
		EmailParameters parameters = new EmailParameters();
		parameters.setFromAddress("illusionedrealmsbugs@gmail.com");
		parameters.recipients().add(new EmailRecipient("illusionedrealmsbugs@gmail.com", RecipientType.TO));
		parameters.setSubject("Bug Reported!");
		parameters.attachments().add(new EmailAttachment(LogRunner.getCurrentLogFile(), "LogFile.tsv"));
		parameters.setContent(buildHtml());
		emailService.sendEmail(parameters);
		callback.run();
	}
	
	private String buildHtml() {
		StringBuilder builder = new StringBuilder();
		builder.append("<h1>Issue Reported</h1>\r\n");
		builder.append("<h3>Call Stack</h3>\r\n");
		builder.append("<pre>");
		builder.append(exceptionString.get());
		builder.append("</pre>\r\n");
		return builder.toString();
	}
}
