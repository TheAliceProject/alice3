package org.alice.tweedle;

public class TweedlePrimitiveType<T> extends TweedleType {

	TweedlePrimitiveType( String name, TweedleType impliedType ) {
		super( name, impliedType );
	}

	public TweedlePrimitiveValue<T> createValue( T value ) {
		return new TweedlePrimitiveValue<T>( value, this );
	}
}