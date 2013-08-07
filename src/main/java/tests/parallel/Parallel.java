package tests.parallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Parallel {
	private ExecutorService executor = Executors.newFixedThreadPool(20);

	public void shutdown() {
		executor.shutdown();
	}

	/**
	 * Invokes the Operation on the Iterable's elements in parallel. This method
	 * blocks until all Operations are completed.
	 * 
	 * @param elements
	 *            The Iterable to process.
	 * @param operation
	 *            The Operation to invoke in parallel.
	 */
	public <T> void For(final Iterable<T> elements, final Operation<T> operation) {
		For(elements, operation, -1);
	}

	/**
	 * Invokes the Operation on the Iterable's elements in parallel.
	 * 
	 * @param elements
	 *            The Iterable to process.
	 * @param operation
	 *            The Operation to invoke in parallel.
	 * @param timeout
	 *            Sets the amount of time all Operation can maximal use to
	 *            complete.
	 */
	public <T> void For(final Iterable<T> elements, final Operation<T> operation, long timeout) {
		
		for (final T elem : elements) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					operation.invoke(elem);
				}
			});
		}

		if (timeout != -1) {
			  try {
				executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}