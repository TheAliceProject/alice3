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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class MethodHeaderPane extends AbstractCodeHeaderPane {
	private javax.swing.JLabel nameLabel;
	private edu.cmu.cs.dennisc.zoot.ActionOperation popupOperation;
	private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {
				edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( MethodHeaderPane.this.popupOperation, e, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
			}
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	public MethodHeaderPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice, javax.swing.JComponent parametersPane ) {
		super( methodDeclaredInAlice );
		if( org.alice.ide.IDE.getSingleton().isJava() ) {
			this.add( new org.alice.ide.common.TypeComponent( methodDeclaredInAlice.getReturnType() ) );
			this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
			//this.add( zoot.ZLabel.acquire( " {" ) );
		} else {
			this.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "declare ", edu.cmu.cs.dennisc.zoot.font.ZTextPosture.OBLIQUE ) );
			StringBuffer sb = new StringBuffer();
			if( methodDeclaredInAlice.isProcedure() ) {
				sb.append( " procedure " );
			} else {
				this.add( new org.alice.ide.common.TypeComponent( methodDeclaredInAlice.getReturnType() ) );
				sb.append( " function " );
			}
			this.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( sb.toString(), edu.cmu.cs.dennisc.zoot.font.ZTextPosture.OBLIQUE ) );
		}
		
		
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		this.nameLabel = new org.alice.ide.common.DeclarationNameLabel( methodDeclaredInAlice );
		edu.cmu.cs.dennisc.awt.FontUtilities.setFontToScaledFont( this.nameLabel, 2.0f );

		if( methodDeclaredInAlice.isSignatureLocked.getValue() ) {
			//pass
		} else {
			this.popupOperation = new edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation(
				new org.alice.ide.operations.ast.RenameMethodOperation( methodDeclaredInAlice ) 
			);
		}

		
		this.add( this.nameLabel );
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		if( parametersPane != null ) {
			this.add( parametersPane );
		}
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.popupOperation != null ) {
			this.nameLabel.addMouseListener( this.mouseAdapter );
		}
	}
	@Override
	public void removeNotify() {
		if( this.popupOperation != null ) {
			this.nameLabel.removeMouseListener( this.mouseAdapter );
		}
		super.removeNotify();
	}
}
