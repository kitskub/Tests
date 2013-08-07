package tests.parallel;

public interface Operation<T> {
	public void invoke(T elem);
}
