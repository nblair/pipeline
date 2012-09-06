/**
 * 
 */

package nblair.pipeline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nblair.pipeline.OfferExpiredException;
import nblair.pipeline.OfferRejectedExecutionHandler;

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
