package ilusr.textadventurecreator.error;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ReportIssueModel {
	
	private final IReportIssueService reportIssueService;
	private final String addId;
	private SelectionAwareObservableList<String> problemType;
	private SimpleStringProperty problemDescription;
	private SimpleStringProperty replyToAddress;
	private SimpleBooleanProperty includeReplyTo;
	private ObservableList<String> attachments;
	private SimpleBooleanProperty valid;
	private LanguageAwareString typeText;
	private LanguageAwareString descriptionText;
	private LanguageAwareString attachmentText;
	private LanguageAwareString replyText;
	private LanguageAwareString replyAddressText;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to use for display strings.
	 * @param reportIssueService a @see IReportIssueService to use to report an issue.
	 */
	public ReportIssueModel(ILanguageService languageService, IReportIssueService reportIssueService) {
		this.reportIssueService = reportIssueService;
		
		addId = UUID.randomUUID().toString();
		problemDescription = new SimpleStringProperty();
		attachments = FXCollections.observableArrayList();
		typeText = new LanguageAwareString(languageService, DisplayStrings.PROBLEM_TYPE);
		descriptionText = new LanguageAwareString(languageService, DisplayStrings.PROBLEM_DESCRIPTION);
		attachmentText = new LanguageAwareString(languageService, DisplayStrings.PROBLEM_ATTACHMENT);
		replyText = new LanguageAwareString(languageService, DisplayStrings.PROBLEM_SHOULD_REPLY);
		replyAddressText = new LanguageAwareString(languageService, DisplayStrings.EMAIL_ADDRESS);
		problemType = new SelectionAwareObservableList<String>();
		valid = new SimpleBooleanProperty(false);
		replyToAddress = new SimpleStringProperty();
		includeReplyTo = new SimpleBooleanProperty(false);
		
		initialize();
	}
	
	private void initialize() {
		problemType.list().add(IssueTypes.ERROR);
		problemType.list().add(IssueTypes.STRANGE_BEHAVIOR);
		problemType.list().add(IssueTypes.REQUEST);
		problemType.selected().set(IssueTypes.ERROR);
		
		problemDescription.addListener((v, o, n) -> {
			valid.set(n != null && !n.isEmpty());
		});
	}
	
	/**
	 * 
	 * @return If the model data is valid.
	 */
	public SimpleBooleanProperty valid() {
		return valid;
	}
	
	/**
	 * 
	 * @return The type of problem.
	 */
	public SelectionAwareObservableList<String> problemType() {
		return problemType;
	}
	
	/**
	 * 
	 * @return A description of the problem.
	 */
	public SimpleStringProperty problemDescription() {
		return problemDescription;
	}
	
	/**
	 * 
	 * @return The Text to display for type.
	 */
	public SimpleStringProperty typeText() {
		return typeText;
	}
	
	/**
	 * 
	 * @return The text to display for description.
	 */
	public SimpleStringProperty descriptionText() {
		return descriptionText;
	}
	
	/**
	 * 
	 * @return The text to display for attachments.
	 */
	public SimpleStringProperty attachmentText() {
		return attachmentText;
	}
	
	/**
	 * 
	 * @return The text to display for reply.
	 */
	public SimpleStringProperty replyText() {
		return replyText;
	}
	
	/**
	 * 
	 * @return The text to display for reply to address.
	 */
	public SimpleStringProperty replyAddressText() {
		return replyAddressText;
	}
	
	/**
	 * 
	 * @return The address to reply to.
	 */
	public SimpleStringProperty replyToAddress() {
		return replyToAddress;
	}

	/**
	 * 
	 * @return The attachments to send.
	 */
	public ObservableList<String> attachments() {
		return attachments;
	}
	
	/**
	 * 
	 * @return If reply to should be used.
	 */
	public SimpleBooleanProperty includeReplyTo() {
		return includeReplyTo;
	}
	
	/**
	 * 
	 * @return a id to use for the add button.
	 */
	public String addId() {
		return addId;
	}
	
	/**
	 * Sends the request.
	 */
	public void sendRequest() {
		new Thread(() -> {
			String reporter = "Anonymous";
			if (includeReplyTo.get()) {
				reporter = replyToAddress.get();
			}
			
			List<String> attach = new ArrayList<>();
			for (String att : attachments) {
				if (att.equals(addId)) {
					continue;
				}
				
				attach.add(att);
			}
			
			attach.add(LogRunner.getCurrentLogFile());
			
			reportIssueService.reportIssue(problemType.selected().get(), attach, reporter, problemDescription.get());
		}).start();
	}
}
