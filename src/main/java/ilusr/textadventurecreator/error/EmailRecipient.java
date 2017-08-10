package ilusr.textadventurecreator.error;

import javax.mail.internet.MimeMessage.RecipientType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EmailRecipient {

	private javax.mail.Message.RecipientType type;
	private String address;
	
	/**
	 * 
	 * @param address The recipients address.
	 */
	public EmailRecipient(String address) {
		this(address, RecipientType.TO);
	}
	
	/**
	 * 
	 * @param address The recipients address.
	 * @param type The recipient type.
	 */
	public EmailRecipient(String address, javax.mail.Message.RecipientType type) {
		this.address = address;
		this.type = type;
	}
	
	/**
	 * 
	 * @return The recipients address.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 
	 * @param address The new recipient address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * 
	 * @return The recipient type.
	 */
	public javax.mail.Message.RecipientType getRecipientType() {
		return type;
	}
	
	/**
	 * 
	 * @param type The new recipient type.
	 */
	public void setRecipientType(javax.mail.Message.RecipientType type) {
		this.type = type;
	}
}
