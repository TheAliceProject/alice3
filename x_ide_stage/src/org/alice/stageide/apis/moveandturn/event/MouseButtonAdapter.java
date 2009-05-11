package org.alice.stageide.apis.moveandturn.event;

public class MouseButtonAdapter implements org.alice.apis.moveandturn.event.MouseButtonListener {
	public MouseButtonAdapter( edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type, Object[] arguments ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "MouseButtonAdapter", type );
	}
	public void mouseButtonClicked( org.alice.apis.moveandturn.event.MouseButtonEvent e ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "mouseButtonClicked", e );
	}
}
