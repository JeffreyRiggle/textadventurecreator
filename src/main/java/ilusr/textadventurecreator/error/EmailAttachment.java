package ilusr.textadventurecreator.error;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EmailAttachment {

	private String file;
	private String fileName;
	
	/**
	 * 
	 * @param file The path to a file.
	 */
	public EmailAttachment(String file) {
		this(file, file);
	}
	
	/**
	 * 
	 * @param file The path to a file.
	 * @param fileName The name of the file.
	 */
	public EmailAttachment(String file, String fileName) {
		this.file = file;
		this.fileName = fileName;
	}
	
	/**
	 * 
	 * @return The path to the file.
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * 
	 * @param file The new path for the file.
	 */
	public void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * 
	 * @return The name of the file.
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * 
	 * @param fileName The new name for the file.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
