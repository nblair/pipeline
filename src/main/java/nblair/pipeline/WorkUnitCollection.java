/**
 * 
 */

package nblair.pipeline;

import java.util.Collection;

/**
 * @author Nicholas Blair
 */
public interface WorkUnitCollection<E> extends Collection<E> {

	/**
	 * 
	 * @return false if {@link #signalCompletion()} has not been called
	 */
	boolean isComplete();
	
	/**
	 * Signal that this collection is complete.
	 * Will result in subsequent calls to {@link #isComplete()} returning true
	 */
	void signalCompletion();
}
