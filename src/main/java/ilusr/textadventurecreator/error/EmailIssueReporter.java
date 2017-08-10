package ilusr.textadventurecreator.error;

import java.util.List;

import javax.mail.Message.RecipientType;

import ilusr.logrunner.LogRunner;

public class EmailIssueReporter implements IReportIssueService {

	private final IEmailService emailService;
	
	public EmailIssueReporter(IEmailService emailService) {
		this.emailService = emailService;
	}
	
	@Override
	public void reportIssue(String type, List<String> attachments, String reporter, String description) {
		EmailParameters parameters = new EmailParameters();
		parameters.setFromAddress("illusionedrealmsbugs@gmail.com");
		parameters.recipients().add(new EmailRecipient("illusionedrealmsbugs@gmail.com", RecipientType.TO));
		parameters.setSubject(buildSubject(type));
		
		parameters.attachments().add(new EmailAttachment(LogRunner.getCurrentLogFile(), "LogFile.tsv"));
		for (String attachment : attachments) {
			int index = attachment.lastIndexOf('/') + 1;
			index = index > 0 ? index : attachment.lastIndexOf('\\') + 1;
			
			parameters.attachments().add(new EmailAttachment(attachment, attachment.substring(index)));
		}
		
		boolean followup = !reporter.equals("Anonymous");
		parameters.setContent(buildHtml(description, followup));
		
		if (followup) {
			parameters.setReplyToAddress(reporter);
		}
		
		emailService.sendEmail(parameters);
	}

	private String buildSubject(String type) {
		switch (type) {
			case IssueTypes.ERROR:
				return "Error Reported";
			case IssueTypes.STRANGE_BEHAVIOR:
				return "Issue Reported";
			case IssueTypes.REQUEST:
				return "New Feature requested";
			default:
				return "Unknown Issue type Reported";
		}
	}
	
	private String buildHtml(String description, boolean followup) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body>");
		
		if (followup) {
			builder.append("<p>Follow up requested.</p>");
		}
		
		builder.append("<pre>");
		builder.append(description);
		builder.append("</pre></body></html>");
		return builder.toString();
	}
}
