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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Super class for {@link PipelineStep}s that provide a configurable number of workers.
 * {@link #accept(Object)} and {@link #blockUntilComplete()} are implemented to perform
 * concurrent execution of {@link PipelineStepCallable}s returned by subclass' {@link #constructCallableForWorkUnit(Object)} implementations
 * via an {@link ExecutorService}.
 * 
 * @see PipelineThreadPoolExecutor
 * @author Nicholas Blair
 */
public abstract class AbstractParallelPipelineStep<W> implements PipelineStep<W>, InitializingBean, DisposableBean {

	protected final Log log = LogFactory.getLog(this.getClass());
	private ThreadPoolExecutor executorService;
	private PipelineStep<W> nextStep;
	private int queueCapacity;
	/**
	 * @return the nextStep
	 */
	public PipelineStep<W> getNextStep() {
		return nextStep;
	}
	/**
	 * @param nextStep the nextStep to set
	 */
	public void setNextStep(PipelineStep<W> nextStep) {
		this.nextStep = nextStep;
	}
	/**
	 * @return the queueCapacity
	 */
	public int getQueueCapacity() {
		return queueCapacity;
	}
	/**
	 * @param queueCapacity the queueCapacity to set
	 */
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public final void destroy() throws Exception {
		this.executorService.shutdown();
	}
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public final void afterPropertiesSet() throws Exception {
		this.executorService = new PipelineThreadPoolExecutor(getQueueCapacity(), getName());
	}
	/*
	 * (non-Javadoc)
	 * @see edu.wisc.wisccal.classsched.pipeline.SyncStep#accept(java.lang.Object)
	 */
	@Override
	public final void accept(W workUnit) {
		if(shouldProcess(workUnit)) {
			PipelineStepCallable<W> task = constructCallableForWorkUnit(workUnit);
			this.executorService.submit(task);
		} else {
			if(log.isDebugEnabled()) {
				log.debug("shouldProcess on pipelineData for " + workUnit + " returned false for step " + getName());
			}
			// just because there was nothing to do in this step doesn't mean we shouldn't pass it to next step in the pipeline
			PipelineStep<W> nextStep = getNextStep();
			if(nextStep != null) {
				nextStep.accept(workUnit);
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see edu.wisc.wisccal.classsched.pipeline.SyncStep#blockUntilComplete()
	 */
	@Override
	public final void blockUntilComplete() {
		this.executorService.shutdown();
		try {
			// no "await indefinitely", 10 DAYS seemingly long enough
			this.executorService.awaitTermination(10, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new IllegalStateException("Interrupted in executorService#awaitTermination in step " + getName(), e);
		}
		getNextStep().blockUntilComplete();
	}
	/**
	 * Construct a {@link PipelineStepCallable} to perform work on the unit.
	 * 
	 * @param workUnit
	 * @return a {@link PipelineStepCallable} implementation that performs work on the workUnit
	 */
	public abstract PipelineStepCallable<W> constructCallableForWorkUnit(W workUnit);
}
