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
/**
 * 
 */

package nblair.pipeline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for {@link OfferRejectedExecutionHandler}.
 * 
 * @author Nicholas Blair
 */
public class OfferRejectedExecutionHandlerTest {

	/**
	 * 
	 */
	@Test
	public void testSuccess() {
		Runnable r = Mockito.mock(Runnable.class);
		ThreadPoolExecutor executor = Mockito.mock(ThreadPoolExecutor.class);
		
		// implementation of offer returns true right away
		@SuppressWarnings("serial")
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>() {
			/* (non-Javadoc)
			 * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
			 */
			@Override
			public boolean offer(Runnable e, long timeout, TimeUnit unit)
					throws InterruptedException {
				return true;
			}
		};
		
		Mockito.when(executor.getQueue()).thenReturn(queue);
		
		OfferRejectedExecutionHandler handler = new OfferRejectedExecutionHandler();
		handler.rejectedExecution(r, executor);
	}
	
	/**
	 * 
	 */
	@Test
	public void testFailure() {
		Runnable r = Mockito.mock(Runnable.class);
		ThreadPoolExecutor executor = Mockito.mock(ThreadPoolExecutor.class);
		
		// simulate offer expired
		@SuppressWarnings("serial")
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>() {
			/* (non-Javadoc)
			 * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
			 */
			@Override
			public boolean offer(Runnable e, long timeout, TimeUnit unit)
					throws InterruptedException {
				return false;
			}
		};
		
		Mockito.when(executor.getQueue()).thenReturn(queue);
		
		OfferRejectedExecutionHandler handler = new OfferRejectedExecutionHandler();
		try {
			handler.rejectedExecution(r, executor);
			Assert.fail("expected OfferExpiredException to be thrown");
		} catch (OfferExpiredException e) {
			// success
		}
	}
}
