/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.stageide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class KeyChooser extends org.alice.ide.choosers.AbstractRowsPaneChooser< edu.cmu.cs.dennisc.alice.ast.FieldAccess > {
	//	private javax.swing.JLabel keyReceiver = zoot.ZLabel.acquire( "<press any key>", zoot.font.ZTextWeight.LIGHT );
	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
		public void keyPressed( java.awt.event.KeyEvent e ) {
			KeyChooser.this.updateKey( org.alice.apis.moveandturn.Key.get( e ) );
		}
		public void keyReleased( java.awt.event.KeyEvent e ) {
		}
		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};
	private static final String NULL_TEXT = "<press any key>";
	private javax.swing.JLabel keyReceiver = new javax.swing.JLabel( NULL_TEXT ) {
		@Override
		public void addNotify() {
			super.addNotify();
			this.getRootPane().setDefaultButton( null );
			this.requestFocusInWindow();
			this.addKeyListener( keyAdapter );
		}
		@Override
		public void removeNotify() {
			//todo: this never gets invoked
			this.removeKeyListener( keyAdapter );
			super.removeNotify();
		}
	};
	private org.lgna.croquet.components.Component< ? >[] components = { new org.lgna.croquet.components.SwingAdapter( this.keyReceiver ) };
	private org.alice.apis.moveandturn.Key key = null;

	public KeyChooser() {
		this.keyReceiver.setFocusable( true );
		this.keyReceiver.setFocusTraversalKeysEnabled( false );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)previousExpression;
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = fieldAccess.getType();
			if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Key.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				if( field != null ) {
					this.updateKey( Enum.valueOf( org.alice.apis.moveandturn.Key.class, field.getName() ) );
				}
			}
		}
	}

	private void updateKey( org.alice.apis.moveandturn.Key key ) {
		this.key = key;
		if( this.key != null ) {
			this.keyReceiver.setText( this.key.name() );
		} else {
			this.keyReceiver.setText( NULL_TEXT );
		}
		throw new RuntimeException( "todo" );
//		edu.cmu.cs.dennisc.croquet.InputPanel< ? > inputPanel = this.getInputPanel();
//		if( inputPanel != null ) {
//			inputPanel.updateOKButton();
//		}
	}
	@Override
	public org.lgna.croquet.components.Component< ? >[] getComponents() {
		return this.components;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.FieldAccess getValue() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Key.class );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( type, this.key.name() );
		assert field.isPublicAccess() && field.isStatic() && field.isFinal();
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
	}
	
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		if( this.getValue() != null ) {
			return null;
		} else {
			return "value not set";
		}
	}
	@Override
	public String getTitleDefault() {
		return "Press Key on Keyboard To Enter Custom Key";
	}
}
