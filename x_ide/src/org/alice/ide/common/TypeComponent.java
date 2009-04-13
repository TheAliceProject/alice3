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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class TypeComponent extends NodeNameLabel {
	private boolean isRollover = false;
	public TypeComponent( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type );
		String typeName;
		if( type != null ) {
			//typeName = type.getName() + " " + type.hashCode();
			typeName = type.getName();
		} else {
			typeName = "<unset>";
		}
		this.setToolTipText( "class: " + typeName );
		this.setBorder( TypeBorder.getSingletonFor( type ) );
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
			final zoot.ActionOperation operation = new zoot.DefaultPopupActionOperation( 
					new org.alice.ide.operations.ast.RenameTypeOperation( typeInAlice ),
					new org.alice.ide.operations.file.SaveAsTypeOperation( typeInAlice ) 
			);
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseEntered( java.awt.event.MouseEvent e ) {
					setRollover( true );
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
					setRollover( false );
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					zoot.ZManager.performIfAppropriate( operation, e, zoot.ZManager.CANCEL_IS_WORTHWHILE );
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
			} );
		}
	}
	public void setRollover( boolean isRollover ) {
		this.isRollover = isRollover;
		this.repaint();
	}
	@Override
	public java.awt.Color getForeground() {
		if( this.isRollover ) {
			return java.awt.Color.BLUE.darker();
		} else {
			return super.getForeground();
		}
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		this.paintBorder( g );
		this.paintComponent( g );
	}
}
