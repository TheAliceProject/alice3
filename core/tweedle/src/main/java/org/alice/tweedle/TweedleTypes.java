package org.alice.tweedle;

public class TweedleTypes {
	public static final TweedlePrimitiveType<Number> NUMBER = new TweedlePrimitiveType<>( "Number", null );
	public static final TweedlePrimitiveType<Double> DECIMAL_NUMBER = new TweedlePrimitiveType<>( "DecimalNumber", NUMBER);
	public static final TweedlePrimitiveType<Integer> WHOLE_NUMBER = new TweedlePrimitiveType<>( "WholeNumber", NUMBER);
	public static final TweedlePrimitiveType<Boolean> BOOLEAN = new TweedlePrimitiveType<>( "Boolean", null);
	public static final TweedlePrimitiveType<String> STRING = new TweedlePrimitiveType<>("String", null);

	public static final TweedlePrimitiveType[] PRIMITIVE_TYPES = {NUMBER, DECIMAL_NUMBER, WHOLE_NUMBER, BOOLEAN, STRING};
}
