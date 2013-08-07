package tests.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Parallel3 {
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
		try {
			if (timeout == -1) {
				executor.invokeAll(createCallables(elements, operation));
			} else {
				executor.invokeAll(createCallables(elements, operation), timeout, TimeUnit.MILLISECONDS);


			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
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
	private <T> List<Callable<Void>> createCallables(final Iterable<T> elements, final Operation<T> operation) {
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