package org.alice.tweedle;

public class TweedleTypes {
	public static final TweedlePrimitiveType<Number> NUMBER = new TweedlePrimitiveType<>( "Number", null );
	public static final TweedlePrimitiveType<Double> DECIMAL_NUMBER = new TweedlePrimitiveType<>( "DecimalNumber", NUMBER);
	public static final TweedlePrimitiveType<Integer> WHOLE_NUMBER = new TweedlePrimitiveType<>( "WholeNumber", NUMBER);
	public static final TweedlePrimitiveType<Boolean> BOOLEAN = new TweedlePrimitiveType<>( "Boolean", null);
}
