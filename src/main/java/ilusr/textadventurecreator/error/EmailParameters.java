package ilusr.textadventurecreator.error;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EmailParameters {

	private String from;
	private String subject;
	private String content;
	private String replyToAddress;
	
	private List<EmailRecipient> recipients;
	private List<EmailAttachment> attachments;
	
	/**
	 * 
	 */
	public EmailParameters() {
		this(new String());
	}
	
	/**
	 * 
	 * @param from The from address.
	 */
	public EmailParameters(String from) {
		this(from, new String());
	}
	
	/**
	 * 
	 * @param from The from address.
	 * @param subject The subject for the email.
	 */
	public EmailParameters(String from, String subject) {
		this(from, subject, new String());
	}
	
	/**
	 * 
	 * @param from The from address.
	 * @param subject The subject for the email.
	 * @param content The body of the email.
	 */
	public EmailParameters(String from, String subject, String content) {
		this(from, subject, content, new ArrayList<EmailRecipient>());
	}
	
	/**
	 * 
	 * @param from The from address.
	 * @param subject The subject for the email.
	 * @param content The body of the email.
	 * @param recipients A list of @see EmailRecipient to send this email to.
	 */
	public EmailParameters(String from, String subject, String content, List<EmailRecipient> recipients) {
		this(from, subject, content, recipients, new ArrayList<EmailAttachment>());
	}
	
	/**
	 * 
	 * @param from The from address.
	 * @param subject The subject for the email.
	 * @param content The body of the email.
	 * @param recipients A list of @see EmailRecipient to send this email to.
	 * @param attachments A list of @see EmailAttachment to attach to this email.
	 */
	public EmailParameters(String from, String subject, String content, List<EmailRecipient> recipients, List<EmailAttachment> attachments) {
		this.from = from;
		this.subject = subject;
		this.content = content;
		this.recipients = recipients;
		this.attachments = attachments;
		this.replyToAddress = new String();
	}
	
	/**
	 * 
	 * @return The email address this is from.
	 */
	public String getFromAddress() {
		return from;
	}
	
	/**
	 * 
	 * @param address The new email address this is from.
	 */
	public void setFromAddress(String address) {
		from = address;
	}
	
	/**
	 * 
	 * @return A List of @see EmailRecipient to send this email to.
	 */
	public List<EmailRecipient> recipients() {
		return recipients;
	}
	
	/**
	 * 
	 * @return The subject for this email.
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 
	 * @param subject The new subject for this email.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 
	 * @return The body of this email.
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 
	 * @param content The new body of this email.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 
	 * @return A list of @see EmailAttachment to attach to this email.
	 */
	public List<EmailAttachment> attachments() {
		return attachments;
	}
	
	/**
	 * 
	 * @return A reply to address to reply to.
	 */
	public String getReplyToAddress() {
		return replyToAddress;
	}
	
	/**
	 * 
	 * @param address The new reply to address to reply to.
	 */
	public void setReplyToAddress(String address) {
		replyToAddress = address;
	}
}
