package tests.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Parallel2 {
	private ForkJoinPool executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

	public void shutdown() {
		executor.shutdownNow();
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
	public<T> void For(final Iterable<T> elements, final Operation<T> operation) {
		For(elements, operation, true, -1);
	}

	/**
	 * Invokes the Operation on the Iterable's elements in parallel.
	 * 
	 * @param elements
	 *            The Iterable to process.
	 * @param operation
	 *            The Operation to invoke in parallel.
	 * @param blocking
	 *            Wether this method should block the current thread of
	 *            execution.
	 * @param timeout
	 *            Sets the amount of time all Operation can maximal use to
	 *            complete.
	 */
	public <T> void For(final Iterable<T> elements, final Operation<T> operation, boolean blocking, long timeout) {
		if (timeout == -1) {
			executor.invokeAll(createCallables(elements, operation));
		} else {
			try {
				executor.invokeAll(createCallables(elements, operation), timeout, TimeUnit.MILLISECONDS);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Wraps an Operation's invoke method with the generic Iterable into a
	 * Callable.
	 * 
	 * @param elements
	 *            The Iterable.
	 * @param operation
	 *            The Operation to invoke in the Callable.
	 * @return
	 */
	private static <T> List<Callable<Void>> createCallables(final Iterable<T> elements, final Operation<T> operation) {
		List<Callable<Void>> callables = new ArrayList<>();
		for (final T elem : elements) {
			callables.add(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					operation.invoke(elem);
					return null;
				}
			});
		}
		return callables;
	}

}