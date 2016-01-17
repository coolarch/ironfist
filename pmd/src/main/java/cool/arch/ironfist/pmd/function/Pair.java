package cool.arch.ironfist.pmd.function;

/**
 * Arity 2 tuple.
 * @param <T> Type of value 0
 * @param <U> Type of value 1
 */
public class Pair<T, U> {

	private final T value0;

	private final U value1;

	private Pair(final T value0, final U value1) {
		this.value0 = value0;
		this.value1 = value1;
	}

	/**
	 * @return the value0
	 */
	public final T getValue0() {
		return value0;
	}

	/**
	 * @return the value1
	 */
	public final U getValue1() {
		return value1;
	}

	/**
	 * Creates a new Pair instance for values value0 and value1.
	 * @param value0 First value
	 * @param value1 Second value
	 * @return Pair instance
	 */
	public static <T, U> Pair<T, U> of(final T value0, final U value1) {
		return new Pair<>(value0, value1);
	}
}
