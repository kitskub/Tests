package tests;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import tests.parallel.Operation;
import tests.parallel.Parallel;
import tests.parallel.Parallel2;
import tests.parallel.Parallel3;

/**
 * Output:
First operation (long)

Parallel took: 92
Parallel(secondrun) took: 21

Parallel2 took: 26963
Parallel2(second run) took: 5880

Parallel3 took: 7735
Parallel3(second run) took: 10790

Sequential took: 10328

Second operation (short)

Parallel took: 17
Parallel(secondrun) took: 7

Parallel2 took: 29
Parallel2(second run) took: 5

Parallel3 took: 19
Parallel3(second run) took: 17

Sequential took: 12
 *
 */
public class ParallelTest implements Test {
	StringWriter s = new StringWriter();

	@Override
	public void test() {
		List<Integer> ints = new ArrayList<Integer>();
		for (int c = 0; c < 50000; c++) {
			ints.add(c);
		}
		Operation<Integer> op = new Operation<Integer>() {
			@Override
			public void invoke(Integer elem) {
				int e = 0;
				e++;
				File f = new File("test");
				f.mkdirs();
				s.write(e);
				s.toString();
			}
		};
		Operation<Integer> op2 = new Operation<Integer>() {
			@Override
			public void invoke(Integer elem) {
				Math.pow(elem, 2);
			}
		};
		
		System.out.println("First operation (long)");
		System.out.println();

		testParrallel(ints, op);
		System.out.println();
		testParallel2(ints, op);
		System.out.println();
		testParallel3(ints, op);
		System.out.println();
		testSequential(ints, op);
		
		System.out.println();
		System.out.println("Second operation (short)");
		System.out.println();

		testParrallel(ints, op2);
		System.out.println();
		testParallel2(ints, op2);
		System.out.println();
		testParallel3(ints, op2);
		System.out.println();
		testSequential(ints, op2);
	}
	
	public void testParrallel(List<Integer> ints, Operation<Integer> op) {
		Parallel p = new Parallel();

		long takes = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel took: " + (System.currentTimeMillis() - takes));

		long takes2 = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel(secondrun) took: " + (System.currentTimeMillis() - takes2));
		
		p.shutdown();

	}
	
	public void testParallel2(List<Integer> ints, Operation<Integer> op) {
		Parallel2 p = new Parallel2();

		long takes = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel2 took: " + (System.currentTimeMillis() - takes));


		long takes2 = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel2(second run) took: " + (System.currentTimeMillis() - takes2));
		
		p.shutdown();
	}
	
	public void testParallel3(List<Integer> ints, Operation<Integer> op) {
		Parallel3 p = new Parallel3();

		long takes = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel3 took: " + (System.currentTimeMillis() - takes));


		long takes2 = System.currentTimeMillis();
		p.For(ints, op);
		System.out.println("Parallel3(second run) took: " + (System.currentTimeMillis() - takes2));
		
		p.shutdown();
	}
	
	public void testSequential(List<Integer> ints, Operation<Integer> op) {
		long seq = System.currentTimeMillis();
		for (Integer num : ints) {
			op.invoke(num);
		}
		System.out.println("Sequential took: " + (System.currentTimeMillis() - seq));
	}
}
