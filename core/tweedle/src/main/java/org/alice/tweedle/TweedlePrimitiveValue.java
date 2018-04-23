package org.alice.tweedle;

/**
 * Holds strings, numbers, etc.
 */
public class TweedlePrimitiveValue<T> extends TweedleValue {

	private final T value;
	private final TweedlePrimitiveType<T> type;

	TweedlePrimitiveValue( T value, TweedlePrimitiveType<T> type ) {
		super(type);
		this.value = value;
		this.type = type;
	}

	public T getPrimitiveValue() {
		return value;
	}

	@Override public boolean equals( Object obj ) {
		return super.equals( obj ) ||
						obj instanceof TweedlePrimitiveValue &&
										value.equals( ((TweedlePrimitiveValue) obj).value ) &&
										type.equals( ((TweedlePrimitiveValue) obj).type );
	}

	@Override public int hashCode() {
		return 17 * value.hashCode() + type.hashCode();
	}

	@Override public String toString() {
		return "Value(" + value + " : " + type + ")";
	}
}
