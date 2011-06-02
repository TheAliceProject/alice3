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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class DropDownPane extends org.alice.ide.common.AbstractDropDownPane {
	private org.lgna.croquet.components.Component< ? > mainComponent;
	public DropDownPane( org.lgna.croquet.PopupPrepModel< ? > model, org.lgna.croquet.components.Component< ? > prefixPane, org.lgna.croquet.components.Component< ? > mainComponent, org.lgna.croquet.components.Component< ? > postfixPane ) {
		super( model );
		if( prefixPane != null ) {
			this.addComponent( prefixPane );
		}
		this.mainComponent = mainComponent;
		this.addComponent( this.mainComponent );
		if( postfixPane != null ) {
			this.addComponent( postfixPane );
		}
	}
	public org.lgna.croquet.components.Component< ? > getMainComponent() {
		return this.mainComponent;
	}
	

//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		return new edu.cmu.cs.dennisc.awt.BeveledRectangle( new java.awt.geom.Rectangle2D.Float( 0, 0, getWidth()-1.0f, getHeight()-1.0f ) );
//	}
//	@Override
//	protected void paintBorder( java.awt.Graphics g ) {
//		super.paintBorder( g );
//		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//		java.awt.Color prev = g.getColor();
//		try {
//			final int INSET = 4;
//			int size = AFFORDANCE_SIZE;
//
//			int x0 = getWidth() - INSET/2 - AFFORDANCE_SIZE;
//			int x1 = x0 + size;
//			int xC = (x0 + x1) / 2;
//
//			
//			int y0 = INSET + 2;
//			int y1 = y0 + size;
//
//			java.awt.Color triangleFill;
//			java.awt.Color triangleOutline;
//			if( this.isActive() ) {
//				triangleFill = java.awt.Color.YELLOW;
//				triangleOutline = java.awt.Color.BLACK;
//			} else {
//				triangleFill = edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 192 );
//				triangleOutline = null;
//			}
//
//			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//
//			int[] xs = { x0, xC, x1 };
//			int[] ys = { y0, y1, y0 };
//			g.setColor( triangleFill );
//			g.fillPolygon( xs, ys, 3 );
//			if( triangleOutline != null ) {
//				g.setColor( triangleOutline );
//				g.drawPolygon( xs, ys, 3 );
//			}
//		} finally {
//			g.setColor( prev );
//		}
//	}
	
//	@Override
//	protected void paintActiveBorder( java.awt.Graphics2D g2 ) {
//		//super.paintActiveBorder( g2 );
//		g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
//		g2.setColor( java.awt.Color.BLUE );
//		g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, getWidth()-3.0f, getHeight()-3.0f ) );
//	}
}
