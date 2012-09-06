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
