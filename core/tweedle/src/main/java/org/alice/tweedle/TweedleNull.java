package org.alice.tweedle;

public class TweedleNull extends TweedleValue {

	public static TweedleNull NULL = new TweedleNull();

	private TweedleNull() {
		super( TweedleVoidType.VOID );
	}
}
