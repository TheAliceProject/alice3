package org.alice.tweedle;

import org.alice.tweedle.ast.TweedleExpression;

public class TweedleTypes {
	public static final TweedlePrimitiveType<Number> NUMBER = new TweedlePrimitiveType<>( "Number", null );
	public static final TweedlePrimitiveType<Double> DECIMAL_NUMBER = new TweedlePrimitiveType<>( "DecimalNumber", NUMBER);
	public static final TweedlePrimitiveType<Integer> WHOLE_NUMBER = new TweedlePrimitiveType<>( "WholeNumber", NUMBER);
	public static final TweedlePrimitiveType<Boolean> BOOLEAN = new TweedlePrimitiveType<>( "Boolean", null);
	public static final TweedlePrimitiveType<String> TEXT_STRING = new TweedlePrimitiveType<>( "TextString", null);

	public static final TweedlePrimitiveType[] PRIMITIVE_TYPES =
					{NUMBER, DECIMAL_NUMBER, WHOLE_NUMBER, BOOLEAN, TEXT_STRING };

	public static TweedlePrimitiveType commonNumberType( TweedleExpression lhs, TweedleExpression rhs ) {
		return TweedleTypes.WHOLE_NUMBER.equals(lhs.getType()) && TweedleTypes.WHOLE_NUMBER.equals(rhs.getType()) ? TweedleTypes.WHOLE_NUMBER : TweedleTypes.DECIMAL_NUMBER;
	}
}
