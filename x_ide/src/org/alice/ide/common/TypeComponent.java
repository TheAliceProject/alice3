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
public class TypeComponent extends DeclarationNameLabel {
	private edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation popupOperation;
	private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			setRollover( true );
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			setRollover( false );
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			if( TypeComponent.this.popupOperation != null ) {
				edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( TypeComponent.this.popupOperation, e, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
			}
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};
	private boolean isRollover = false;
	public TypeComponent( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type );
		this.setCursor( java.awt.Cursor.getDefaultCursor() );
		this.setBorder( TypeBorder.getSingletonFor( type ) );
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
			
			java.util.List< edu.cmu.cs.dennisc.zoot.Operation > operations = new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >();
			operations.add( new org.alice.ide.operations.ast.RenameTypeOperation( typeInAlice ) );
			
			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
			if( ide.isInstanceCreationAllowableFor( typeInAlice ) ) {
				operations.add( new org.alice.ide.operations.ast.DeclareFieldOfPredeterminedTypeOperation( ide.getSceneType(), typeInAlice ) );
			}
			operations.add( new org.alice.ide.operations.file.SaveAsTypeOperation( typeInAlice ) );
			this.popupOperation = new edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation( operations );
					
//			this.popupOperation = new zoot.DefaultPopupActionOperation( 
//					new org.alice.ide.operations.ast.RenameTypeOperation( typeInAlice ),
//					new org.alice.ide.operations.ast.DeclareFieldOfPredeterminedTypeOperation( org.alice.ide.IDE.getSingleton().getSceneType(), typeInAlice ), 
//					new org.alice.ide.operations.file.SaveAsTypeOperation( typeInAlice ) 
//			);
		}
	}
	public TypeComponent( edu.cmu.cs.dennisc.alice.ast.AbstractType type, boolean isToolTipDesired ) {
		this( type );
		if( isToolTipDesired ) {
			String typeName;
			if( type != null ) {
				//typeName = type.getName() + " " + type.hashCode();
				typeName = type.getName();
			} else {
				typeName = "<unset>";
			}
			this.setToolTipText( "class: " + typeName );
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if( this.popupOperation != null ) {
			this.addMouseListener( this.mouseAdapter );
		}
	}
	@Override
	public void removeNotify() {
		if( this.popupOperation != null ) {
			this.removeMouseListener( this.mouseAdapter );
		}
		super.removeNotify();
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
