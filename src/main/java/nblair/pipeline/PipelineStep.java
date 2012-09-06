/**
 * 
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
