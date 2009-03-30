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

import java.awt.event.MouseEvent;

import zoot.ZManager;

/**
 * @author Dennis Cosgrove
 */
public abstract class TypedDeclarationPane extends swing.LineAxisPane  {
	class PopupOperation extends zoot.AbstractPopupActionOperation {
		@Override
		protected java.util.List< zoot.Operation > getOperations() {
			return TypedDeclarationPane.this.getPopupOperations();
		}
	}
	private PopupOperation popupOperation = new PopupOperation();
	public TypedDeclarationPane( java.awt.Component... components ) {
		super( components );
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setForeground( java.awt.Color.GRAY );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mousePressed( MouseEvent e ) {
				if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {
					if( TypedDeclarationPane.this.popupOperation != null ) {
						ZManager.performIfAppropriate( TypedDeclarationPane.this.popupOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
					}
				}
			}
			public void mouseReleased( MouseEvent e ) {
			}
			public void mouseClicked( MouseEvent e ) {
			}
			public void mouseEntered( MouseEvent e ) {
			}
			public void mouseExited( MouseEvent e ) {
			}
		} );
	}
	protected abstract java.util.List< zoot.Operation > getPopupOperations();
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		g.drawRect( 0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}
