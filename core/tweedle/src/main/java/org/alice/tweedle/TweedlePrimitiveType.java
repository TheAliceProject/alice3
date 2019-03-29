package org.alice.tweedle;

public class TweedlePrimitiveType<T> extends TweedleType {

	TweedlePrimitiveType( String name, TweedleType impliedType ) {
		super( name, impliedType );
	}

	@Override public String toString() {
		return getName();
	}

	public TweedlePrimitiveValue<T> createValue( T value ) {
		return new TweedlePrimitiveValue<T>( value, this );
	}

	@Override public String valueToString( TweedleValue tweedleValue ) {
		return ((TweedlePrimitiveValue<?>) tweedleValue).getPrimitiveValue().toString();
	}
}
