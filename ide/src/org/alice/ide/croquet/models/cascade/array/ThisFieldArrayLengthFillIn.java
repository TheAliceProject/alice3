package org.alice.ide.croquet.models.cascade.array;

public class ThisFieldArrayLengthFillIn extends ArrayLengthFillIn {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, ThisFieldArrayLengthFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized ThisFieldArrayLengthFillIn getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		assert field != null;
		ThisFieldArrayLengthFillIn rv = map.get( field );
		if( rv != null ) {
			//pass
		} else {
			rv = new ThisFieldArrayLengthFillIn( field );
			map.put( field, rv );
		}
		return rv;
	}
	private final edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	private ThisFieldArrayLengthFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( java.util.UUID.fromString( "1aa9aa94-fd7f-47e9-99a6-2556d7871f28" ) );
		this.field = field;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createAccessExpression() {
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), this.field );
	}
}
