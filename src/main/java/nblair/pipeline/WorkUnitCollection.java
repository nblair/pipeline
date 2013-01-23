/**
 *    Copyright 2012, Board of Regents of the University of Wisconsin System
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package nblair.pipeline;

import java.util.Collection;

/**
 * Extension of {@link Collection} that adds method to designate that even
 * if the {@link Collection#isEmpty()}, it may be "expecting more" to eventually show up.
 * {@link #isComplete()} will return false until no more items are expected.
 * 
 * Callers that populate this {@link WorkUnitCollection} must invoke {@link #signalCompletion()}
 * to indicate no more items are expected. 
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
