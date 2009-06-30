package org.alice.stageide.apis.moveandturn.event;

public class KeyAdapter implements org.alice.apis.moveandturn.event.KeyListener {
	private edu.cmu.cs.dennisc.alice.virtualmachine.Context context;
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;
	public KeyAdapter( edu.cmu.cs.dennisc.alice.virtualmachine.Context context, edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type, Object[] arguments ) {
		this.context = context;
		this.method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)type.getDeclaredMethod( "keyTyped", org.alice.apis.moveandturn.event.MouseButtonEvent.class );
	}
	public void keyTyped( org.alice.apis.moveandturn.event.KeyEvent e ) {
		this.context.invokeEntryPoint( this.method, e );
	}
}
