/**
 * 
 */

package nblair.pipeline;

/**
 * Thrown by {@link OfferRejectedExecutionHandler} in the event the wait time
 * elapsed and the task was not accepted.
 * 
 * @author Nicholas Blair
 */
public class OfferExpiredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6088349866311752904L;

	/**
	 * 
	 */
	public OfferExpiredException() {
	}

	/**
	 * @param message
	 */
	public OfferExpiredException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public OfferExpiredException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OfferExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}
