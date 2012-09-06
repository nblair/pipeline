/**
 * 
 */

package nblair.pipeline;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Nicholas Blair
 */
@Qualifier("end")
public class TerminationStep<W> implements PipelineStep<W> {
	
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#getName()
	 */
	@Override
	public String getName() {
		return "end";
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#accept(java.lang.Object)
	 */
	@Override
	public void accept(W workUnit) {
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#blockUntilComplete()
	 */
	@Override
	public void blockUntilComplete() {
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#getNextStep()
	 */
	@Override
	public PipelineStep<W> getNextStep() {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.github.nblair.pipeline.PipelineStep#shouldProcess(java.lang.Object)
	 */
	@Override
	public boolean shouldProcess(W workUnit) {
		return true;
	}
}
