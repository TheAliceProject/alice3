package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class FieldAccess extends MemberAccessExpression {

	private final String fieldName;

	public FieldAccess( TweedleExpression target, String fieldName ) {
		super( target );
		this.fieldName = fieldName;
	}

	@Override public TweedleValue evaluate( Frame frame ) {
		evaluateTarget( frame );
		// TODO access the fieldName on the target.
		return null;
	}

	public String getFieldName() {
		return fieldName;
	}

}
