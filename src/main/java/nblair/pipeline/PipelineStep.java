/**
 *    Copyright 2012 Nicholas Blair
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

/**
 * A step in the pipeline.
 * 
 * @author Nicholas Blair
 */
public interface PipelineStep<W> {

	/**
	 * 
	 * @return a descriptive name for this step
	 */
	String getName();
	
	/**
	 * Accept a work unit for future processing in this step.
	 * This method may block if the work queue for the step is full.
	 * 
	 * @param workUnit
	 */
	void accept(W workUnit);
	
	/**
	 * This method blocks by design until all work units are complete.
	 */
	void blockUntilComplete();
	
	/**
	 * 
	 * @return a reference to the "next" step in the pipeline after this instance.
	 */
	PipelineStep<W> getNextStep();
	
	/**
	 * Vote on whether or not work unit should be invoked.
	 * 
	 * @param workUnit
	 * @return true if this instance will perform work on the workUnit
	 */
	boolean shouldProcess(W workUnit);
}
