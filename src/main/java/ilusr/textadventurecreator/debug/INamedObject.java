package ilusr.textadventurecreator.debug;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface INamedObject {
	/**
	 * 
	 * @return The name of the object.
	 */
	String name();
	/**
	 * 
	 * @return The value of the object.
	 */
	String value();
	/**
	 * 
	 * @return The description of the object.
	 */
	String description();
	/**
	 * 
	 * @param updated A @see Runnable to execute when the object is updated.
	 */
	void updated(Runnable updated);
}
