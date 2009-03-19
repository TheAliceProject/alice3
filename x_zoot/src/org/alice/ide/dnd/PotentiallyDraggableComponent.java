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
package org.alice.ide.dnd;


/**
 * @author Dennis Cosgrove
 */
public abstract class PotentiallyDraggableComponent<E> extends org.alice.ide.AbstractControl {
	protected boolean isActuallyPotentiallyDraggable() {
		return false;
	}
	private final int DROP_SHADOW_SIZE = 6;
	public int getDragWidth() {
		return getDropWidth() + DROP_SHADOW_SIZE;
	}
	public int getDragHeight() {
		return getDropHeight() + DROP_SHADOW_SIZE;
	}
	public int getDropWidth() {
		return this.getWidth();
	}
	public int getDropHeight() {
		return this.getHeight();
	}
	
	protected void fillBounds( java.awt.Graphics2D g2 ) {
		fillBounds( g2, 0, 0, this.getWidth(), this.getHeight() );
	}

	
	public void paintDrag( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		java.awt.Paint prevPaint = g2.getPaint();
		g2.setPaint( new java.awt.Color( 0,0,0,64 ) );
		g2.translate( DROP_SHADOW_SIZE, DROP_SHADOW_SIZE );
		this.fillBounds( g2 );
		g2.translate( -DROP_SHADOW_SIZE, -DROP_SHADOW_SIZE );
		g2.setPaint( prevPaint );
		print( g2 );
//		if( isOverDragAccepter ) {
//			//pass
//		} else {
//			g2.setPaint( new java.awt.Color( 127, 127, 127, 127 ) );
//			this.createBoundsShape().fill( g2 );
//		}
		if( isCopyDesired ) {
			g2.setPaint( org.alice.ide.lookandfeel.PaintUtilities.getCopyTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	public void paintDrop( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		print( g2 );
		g2.setColor( new java.awt.Color( 0, 0, 0, 127 ) );
		this.fillBounds( g2 );
	}

}
