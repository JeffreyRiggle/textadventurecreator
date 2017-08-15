package ilusr.textadventurecreator.error;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ilusr.logrunner.LogRunner;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EmailService implements IEmailService {

	private Session session;
	
	/**
	 * Creates an email service
	 */
	public EmailService(String host, int port, String address, String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		Authenticator auth = new Authenticator() {
			private PasswordAuthentication pass = new PasswordAuthentication(address, password);
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return pass;
			}
		};
		
		session = Session.getInstance(props, auth);
		session.setDebug(false);
	}
	
	@Override
	public void sendEmail(EmailParameters parameters) {
		try {
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(parameters.getFromAddress()));
			for (EmailRecipient recipient : parameters.recipients()) {
				message.addRecipient(recipient.getRecipientType(), new InternetAddress(recipient.getAddress()));
			}

			if (parameters.getReplyToAddress() != null && !parameters.getReplyToAddress().isEmpty()) {
				message.setReplyTo(new Address[] { new InternetAddress(parameters.getReplyToAddress())});
			}
			
			message.setSubject(parameters.getSubject());
			Multipart multi = new MimeMultipart();
			
			BodyPart content = new MimeBodyPart();
			content.setContent(parameters.getContent(), "text/html; charset=utf-8");
			multi.addBodyPart(content);
			
			for (EmailAttachment attachment : parameters.attachments()) {
				BodyPart attachmentPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment.getFile());
				attachmentPart.setDataHandler(new DataHandler(source));
				attachmentPart.setFileName(attachment.getFileName());
				
				multi.addBodyPart(attachmentPart);
			}
			
			message.setContent(multi);
			
			LogRunner.logger().info(String.format("Sending email with subject %s", parameters.getSubject()));
			
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
