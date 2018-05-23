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

	//TODO Move these off the types class?
	public static final TweedlePrimitiveValue<Boolean> TRUE = new TweedlePrimitiveValue<>( true, BOOLEAN );
	public static final TweedlePrimitiveValue<Boolean> FALSE = new TweedlePrimitiveValue<>( false, BOOLEAN );

	public static TweedlePrimitiveType commonNumberType( TweedleExpression lhs, TweedleExpression rhs ) {
		if (lhs == null || rhs == null || lhs.getType() == null || rhs.getType() == null ||
						TweedleTypes.NUMBER.equals(lhs.getType()) || TweedleTypes.NUMBER.equals(rhs.getType())) {
			return TweedleTypes.NUMBER;
		}
		return TweedleTypes.WHOLE_NUMBER.equals(lhs.getType()) && TweedleTypes.WHOLE_NUMBER.equals(rhs.getType())
						? TweedleTypes.WHOLE_NUMBER
						: TweedleTypes.DECIMAL_NUMBER;
	}
}
