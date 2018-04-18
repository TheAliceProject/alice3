package org.alice.tweedle;

public class TweedleArrayType extends TweedleType{
	private TweedleType memberType;

	public TweedleArrayType( TweedleType type) {
		super( type.getName() + "[]");
	}

	@Override public boolean willAcceptValueOfType( TweedleType type) {
		return (type instanceof TweedleArrayType) && memberType.willAcceptValueOfType( ((TweedleArrayType) type).memberType );
	}
}
