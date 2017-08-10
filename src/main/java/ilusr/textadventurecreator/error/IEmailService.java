package ilusr.textadventurecreator.error;

public interface IEmailService {
	/**
	 * Sends an email.
	 * 
	 * @param parameters The @see EmailParameters to use to send this email.
	 */
	void sendEmail(EmailParameters parameters);
}
