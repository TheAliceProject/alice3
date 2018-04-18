package org.alice.tweedle;

import java.util.List;

public class TweedleArray extends TweedleValue {
	private final List<TweedleValue> values;
	private final TweedleArrayType arrayType;

	public TweedleArray( TweedleArrayType arrayType, List<TweedleValue> initialValues ) {
		//TODO capture values type and verify values they match
		values = initialValues ;
		this.arrayType = arrayType;
	}

	public TweedleValue getAt(int index) {
		return values.get( index );
	}

	public int length() {
		return values.size();
	}

	@Override public TweedleType getType() {
		return arrayType;
	}

	@Override public boolean equals( Object obj ) {
		return super.equals( obj ) ||
						obj instanceof TweedleArray &&
										values.equals( ((TweedleArray) obj).values ) &&
										arrayType.equals( ((TweedleArray) obj).arrayType );
	}

	@Override public int hashCode() {
		return 17 * values.hashCode() + arrayType.hashCode();
	}
}
