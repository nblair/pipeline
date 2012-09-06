/*******************************************************************************
 *  Copyright 2012 The Board of Regents of the University of Wisconsin System.
 *******************************************************************************/
package nblair.pipeline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

/**
 * Sub class of {@link ThreadPoolExecutor} tailored for {@link AbstractMigrationStep}.
 *  
 * @author Nicholas Blair
 * @version $Id: PipelineThreadPoolExecutor.java $
 */
public class PipelineThreadPoolExecutor extends ThreadPoolExecutor {

	protected final Log log = LogFactory.getLog(this.getClass());
	/**
	 * Calls the superclass' constructor passing in the queueCapacity,
	 * 1 Minute keep alive time, a bounded {@link LinkedBlockingQueue},
	 * a {@link CustomizableThreadFactory}, and an {@link OfferRejectedExecutionHandler}.
	 * 
	 * @see LinkedBlockingQueue
	 * @see ThreadPoolExecutor
	 * @see CustomizableThreadFactory
	 * @param queueCapacity the capacity of the work queue
	 * @param namePrefix the thread name prefix
	 */
	public PipelineThreadPoolExecutor(int queueCapacity, String namePrefix) {
		super(queueCapacity, queueCapacity, 1, TimeUnit.MINUTES, 
				new LinkedBlockingQueue<Runnable>(queueCapacity), 
				new CustomizableThreadFactory(namePrefix),
				new OfferRejectedExecutionHandler());
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(t != null) {
			log.fatal("pipeline step failed", t);
		}
		super.afterExecute(r, t);
	}
}
