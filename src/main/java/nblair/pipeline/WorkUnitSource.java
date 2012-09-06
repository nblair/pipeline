/**
 * 
 */

package nblair.pipeline;

import java.util.Iterator;

/**
 * A source for work units.
 * 
 * @author Nicholas Blair
 */
public interface WorkUnitSource<W> {

	/**
	 * 
	 * @return a never null, but potentially empty, {@link Iterator} of work units
	 */
	WorkUnitCollection<W> getWorkUnits();
}
