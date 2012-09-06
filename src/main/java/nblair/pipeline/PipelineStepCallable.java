/**
 * 
 */

package nblair.pipeline;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract supertype for {@link Callable}s used in the {@link AbstractParallelPipelineStep}s.
 * 
 * @author Nicholas Blair
 */
public abstract class PipelineStepCallable<W> implements Callable<W> {

	protected final Log log = LogFactory.getLog(this.getClass());
	
	private final String stepName;
	private final PipelineStep<W> nextStep;
	private final W workUnit;
	
	/**
	 * @param nextStep
	 */
	public PipelineStepCallable(String stepName, PipelineStep<W> nextStep, W workUnit) {
		this.stepName = stepName;
		this.nextStep = nextStep;
		this.workUnit = workUnit;
	}

	/**
	 * Updates the thread name, invokes {@link #doWork(Object)}, then calls {@link PipelineStep#accept(Object)} on
	 * the next step.
	 * Any {@link Exception} thrown by {@link #doWork(Object)} will be caught, logged, and rethrown.
	 * 
	 *  (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 * @return the workunit
	 */
	@Override
	public final W call() throws Exception {
		final String originalThreadName = Thread.currentThread().getName();
		try {
			Thread.currentThread().setName(originalThreadName + "-" + stepName + "-" + idWorkUnit(workUnit));
			W result = doWork(workUnit);
			nextStep.accept(workUnit);
			return result;
		} catch (Exception e) {
			log.error("caught Exception in SyncStepCallable '" + stepName + "' for workUnit " + idWorkUnit(workUnit), e);
			throw(e);
		} finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}

	/**
	 * 
	 * @param workUnit
	 * @return a {@link String} to 'uniquely' identify the workunit
	 */
	public abstract String idWorkUnit(W workUnit);
	/**
	 * Perform the actual work on the workunit.
	 * 
	 * @param workUnit
	 * @return the workUnit
	 * @throws Exception
	 */
	public abstract W doWork(W workUnit) throws Exception;
}
