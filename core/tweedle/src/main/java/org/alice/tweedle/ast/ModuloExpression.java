package org.alice.tweedle.ast;

import org.alice.tweedle.TweedlePrimitiveValue;
import org.alice.tweedle.TweedleTypes;
import org.alice.tweedle.TweedleValue;

public class ModuloExpression extends BinaryExpression{

	public ModuloExpression( TweedleExpression lhs, TweedleExpression rhs) {
		super( lhs, rhs, TweedleTypes.WHOLE_NUMBER );
	}

	@Override TweedleValue evaluate( TweedleValue left, TweedleValue right) {
		return eval( (TweedlePrimitiveValue<Integer>) left, ((TweedlePrimitiveValue<Integer>) right) );
	}

	private TweedlePrimitiveValue<Integer> eval( TweedlePrimitiveValue<Integer> left,
																							 TweedlePrimitiveValue<Integer> right ) {
		return TweedleTypes.WHOLE_NUMBER.createValue( left.getPrimitiveValue() % right.getPrimitiveValue() );
	}
}
