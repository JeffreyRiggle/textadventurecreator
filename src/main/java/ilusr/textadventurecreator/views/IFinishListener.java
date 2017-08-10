package ilusr.textadventurecreator.views;

/**
 * 
 * @author Jeff Riggle
 *
 * @param <T> The resulting object type.
 */
public interface IFinishListener<T> {
	/**
	 * Gets called when a finish occurs
	 * 
	 * @param result The finished object. 
	 */
	void finish(T result);
}
