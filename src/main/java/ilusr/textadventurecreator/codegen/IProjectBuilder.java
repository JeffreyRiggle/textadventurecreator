package ilusr.textadventurecreator.codegen;

import ilusr.textadventurecreator.statusbars.StatusItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IProjectBuilder {
	/**
	 * Builds a project and updates the status bar with details about the build.
	 * 
	 * @param item The @see StatusItem to update as the build process occurs.
	 */
	void build(StatusItem item);
}
