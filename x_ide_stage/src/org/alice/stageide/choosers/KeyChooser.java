/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class KeyChooser extends org.alice.ide.choosers.AbstractChooser< org.alice.apis.moveandturn.Key > {
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
	private java.awt.Component[] components = { this.keyReceiver };
	private org.alice.apis.moveandturn.Key key = null;

	public KeyChooser() {
		this.keyReceiver.setFocusable( true );
		this.keyReceiver.setFocusTraversalKeysEnabled( false );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)previousExpression;
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = fieldAccess.getType();
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
		edu.cmu.cs.dennisc.croquet.KInputPane< ? > inputPane = this.getInputPane();
		if( inputPane != null ) {
			inputPane.updateOKButton();
		}
	}
	@Override
	public java.awt.Component[] getComponents() {
		return this.components;
	}
	public org.alice.apis.moveandturn.Key getValue() {
		return this.key;
	}
	public boolean isInputValid() {
		return getValue() != null;
	}
	public String getTitleDefault() {
		return "Press Key on Keyboard To Enter Custom Key";
	}
}
