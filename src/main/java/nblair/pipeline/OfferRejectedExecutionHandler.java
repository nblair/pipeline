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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * {@link RejectedExecutionHandler} that will re-offer the runnable, waiting 
 * up to {@link #getDuration()} {@link #getTimeUnit()}s.
 * 
 * @see BlockingQueue#offer(Object, long, TimeUnit)
 * @author Nicholas Blair
 */
public final class OfferRejectedExecutionHandler implements RejectedExecutionHandler {

	private TimeUnit timeUnit = TimeUnit.DAYS;
	private long duration = 1;
	
	/**
	 * 
	 */
	public OfferRejectedExecutionHandler() { }
	/**
	 * @param timeUnit
	 * @param duration
	 */
	public OfferRejectedExecutionHandler(TimeUnit timeUnit, long duration) {
		this.timeUnit = timeUnit;
		this.duration = duration;
	}
	/**
	 * @return the timeUnit
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			boolean successful = executor.getQueue().offer(r, duration, timeUnit);
			if(!successful) {
				throw new OfferExpiredException("offer returned false after " + getDuration() + " " + getTimeUnit() + " for " + r);
			}
		} catch (InterruptedException e) {
			throw new IllegalStateException("Interrupted in RejectedExecutionHandler#rejectedExecution", e);
		}
	}
}