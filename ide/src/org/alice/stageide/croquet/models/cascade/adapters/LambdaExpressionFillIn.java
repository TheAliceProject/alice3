package org.alice.stageide.croquet.models.cascade.adapters;

import java.util.UUID;

public abstract class LambdaExpressionFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< org.lgna.project.ast.LambdaExpression > {
	private final Class<?> listenerCls;
	private final org.lgna.project.ast.LambdaExpression transientValue;
	public LambdaExpressionFillIn(UUID id, Class<?> listenerCls) {
		super(id);
		this.listenerCls = listenerCls;
		this.transientValue = this.createValue();
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
	private org.lgna.project.ast.LambdaExpression createValue() { 
		return org.lgna.project.ast.AstUtilities.createLambdaExpression( this.listenerCls );
	}
}
