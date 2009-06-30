package org.alice.stageide.cascade.fillerinners;

public class KeyListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public KeyListenerFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.KeyListener.class ), edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class );
	}
	@Override
	public void addFillIns( cascade.Blank blank ) {
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] parameters = new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] {
				new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( "e", org.alice.apis.moveandturn.event.KeyEvent.class )
		};
		edu.cmu.cs.dennisc.alice.ast.BlockStatement body = new edu.cmu.cs.dennisc.alice.ast.BlockStatement();
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = new edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice( "keyTyped", Void.TYPE, parameters, body );
		method.isSignatureLocked.setValue( true );
		edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type = new edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice();
		type.superType.setValue( this.getType() );
		type.methods.add( method );
		edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor constructor = edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor.get( type );
		blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor ) ) );
	}
}
