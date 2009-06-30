package org.alice.stageide.apis.moveandturn.event;

public class MouseButtonAdapter implements org.alice.apis.moveandturn.event.MouseButtonListener {
	private edu.cmu.cs.dennisc.alice.virtualmachine.Context context;
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	public MouseButtonAdapter( edu.cmu.cs.dennisc.alice.virtualmachine.Context context, edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type, Object[] arguments ) {
		this.context = context;
		this.method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)type.getDeclaredMethod( "mouseButtonClicked", org.alice.apis.moveandturn.event.MouseButtonEvent.class );
	}
	public void mouseButtonClicked( org.alice.apis.moveandturn.event.MouseButtonEvent e ) {
		this.context.invokeEntryPoint( this.method, e );
	}
}
