package org.alice.tweedle;

public class TweedleArrayType<T extends TweedleType> extends TweedleType{
	private TweedleType memberType;

	public TweedleArrayType( TweedleType type) {
		super( type.getName() + "[]" );
	}
}
