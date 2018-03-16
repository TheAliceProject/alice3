package org.alice.tweedle;

public class TweedleArray<T extends TweedleType> extends TweedleValue<TweedleArrayType<T>>{
	private final T[] values;

	public TweedleArray( T[] initialValues ) {
		//TODO capture values type and verify values they match
		values = initialValues ;
	}

	public T getAt(int index) {
		return values[index];
	}

	public int length() {
		return values.length;
	}
}
