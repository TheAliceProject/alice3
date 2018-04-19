package org.alice.tweedle;

public class TweedleArrayType extends TweedleType{
	private TweedleType valueType;

	public TweedleArrayType( TweedleType valueType) {
		super( valueType.getName() + "[]");
		this.valueType = valueType;
	}

	@Override public boolean willAcceptValueOfType( TweedleType type) {
		return this == type ||
						( (type instanceof TweedleArrayType) &&
							valueType.willAcceptValueOfType( ((TweedleArrayType) type).valueType ) );
	}
}
