package org.alice.ide.declarationseditor.events;

import org.alice.ide.croquet.models.cascade.MethodInvocationFillIn;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ThisExpression;


//todo: consolidate w/ similar class
public class AddEventListenerMethodInvocationFillIn extends MethodInvocationFillIn {
	private static java.util.Map< org.lgna.project.ast.AbstractMethod, AddEventListenerMethodInvocationFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized AddEventListenerMethodInvocationFillIn getInstance( org.lgna.project.ast.AbstractMethod method ) {
		AddEventListenerMethodInvocationFillIn rv = map.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = new AddEventListenerMethodInvocationFillIn( method );
			map.put( method, rv );
		}
		return rv;
	}
	private AddEventListenerMethodInvocationFillIn( org.lgna.project.ast.AbstractMethod method ) {
		super( java.util.UUID.fromString( "bcc0b92b-2154-4b59-aafc-5243d2d3422e" ), new ThisExpression(), method );
	}
	@Override
	protected Expression createExpression(Expression transientValueExpression) {
		return new ThisExpression();
	}
}
