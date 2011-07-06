package org.alice.ide.croquet.models.cascade.array;

public abstract class ArrayLengthFillIn extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< edu.cmu.cs.dennisc.alice.ast.ArrayLength  > {
	private edu.cmu.cs.dennisc.alice.ast.ArrayLength transientValue;
	public ArrayLengthFillIn( java.util.UUID id ) {
		super( id );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createAccessExpression();
	private edu.cmu.cs.dennisc.alice.ast.ArrayLength createValue() {
		return new edu.cmu.cs.dennisc.alice.ast.ArrayLength( this.createAccessExpression() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ArrayLength createValue( org.lgna.croquet.cascade.ItemNode< ? super edu.cmu.cs.dennisc.alice.ast.ArrayLength, java.lang.Void > step ) {
		return this.createValue();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ArrayLength getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super edu.cmu.cs.dennisc.alice.ast.ArrayLength, java.lang.Void > step ) {
		if( this.transientValue != null ) {
			//pass
		} else {
			this.transientValue = this.createValue();
		}
		return this.transientValue;
	}
}
