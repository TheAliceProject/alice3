package org.alice.ide.croquet.models.cascade;

public class MethodInvocationFillIn extends ExpressionFillInWithExpressionBlanks< org.lgna.project.ast.MethodInvocation > {
	private final org.lgna.project.ast.MethodInvocation transientValue;
	public MethodInvocationFillIn( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "b105e9e9-b8da-4927-b2d1-5bc6ebd17c1a" ) );
		this.transientValue = org.alice.ide.ast.AstUtilities.createIncompleteMethodInvocation( expression, method );
		for( org.lgna.project.ast.AbstractParameter parameter : method.getRequiredParameters() ) {
			ParameterBlank parameterBlank = ParameterBlank.getInstance( parameter );
			this.addBlank( parameterBlank );
		}
	}
	
	@Override
	protected org.lgna.project.ast.MethodInvocation createValue( org.lgna.project.ast.Expression[] expressions ) {
		return org.lgna.project.ast.AstUtilities.createMethodInvocation( this.transientValue.expression.getValue(), this.transientValue.method.getValue(), expressions );
	}
	@Override
	public org.lgna.project.ast.MethodInvocation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.MethodInvocation, org.lgna.project.ast.Expression > step ) {
		return this.transientValue;
	}
}
