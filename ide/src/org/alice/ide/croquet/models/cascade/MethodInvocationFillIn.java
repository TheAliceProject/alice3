package org.alice.ide.croquet.models.cascade;

public class MethodInvocationFillIn extends ExpressionFillInWithExpressionBlanks< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > {
	private final edu.cmu.cs.dennisc.alice.ast.MethodInvocation transientValue;
	public MethodInvocationFillIn( edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "b105e9e9-b8da-4927-b2d1-5bc6ebd17c1a" ) );
		this.transientValue = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( expression, method );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : method.getParameters() ) {
			ParameterBlank parameterBlank = ParameterBlank.getInstance( parameter );
			this.addBlank( parameterBlank );
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.MethodInvocation createValue( edu.cmu.cs.dennisc.alice.ast.Expression[] expressions ) {
		return org.alice.ide.ast.NodeUtilities.createMethodInvocation( this.transientValue.expression.getValue(), this.transientValue.method.getValue(), expressions );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.MethodInvocation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Expression > step ) {
		return this.transientValue;
	}
}
