package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

public class AssignmentExpression extends TweedleExpression {

	private final TweedleExpression assigneeExp;
	private TweedleExpression valueExp;

	public AssignmentExpression( TweedleExpression assigneeExp, TweedleExpression valueExp ) {
		super( valueExp.getType() );
		this.assigneeExp = assigneeExp;
		this.valueExp = valueExp;
	}


	@Override public TweedleValue evaluate( Frame frame ) {
		TweedleValue newValue = valueExp.evaluate( frame );
		// TODO Evaluate the assignee as a target and set to the newValue
		return newValue;
	}
}