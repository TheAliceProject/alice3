package org.alice.stageide.croquet.models.cascade.adapters;

import java.util.UUID;

import org.lgna.project.ast.LambdaExpression;


public abstract class LambdaExpressionFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< org.lgna.project.ast.LambdaExpression > {


	protected org.lgna.project.ast.LambdaExpression transientValue;
	
	public LambdaExpressionFillIn(UUID id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public org.lgna.project.ast.LambdaExpression getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super  org.lgna.project.ast.LambdaExpression,Void > step ) {
		return this.transientValue;
	}
	
	@Override
	public org.lgna.project.ast.LambdaExpression createValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.LambdaExpression,Void > step ) {
		return this.createValue();
	}
	@Override
	public boolean isAutomaticallySelectedWhenSoleOption() {
		return true;
	}

	protected abstract LambdaExpression createValue();
	
}
