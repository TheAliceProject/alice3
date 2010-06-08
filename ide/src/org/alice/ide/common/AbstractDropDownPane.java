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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDropDownPane extends edu.cmu.cs.dennisc.croquet.Control {
//public abstract class AbstractDropDownPane extends edu.cmu.cs.dennisc.croquet.AbstractButton< javax.swing.AbstractButton, edu.cmu.cs.dennisc.croquet.Operation< ? > > {

//	private zoot.event.ControlAdapter controlAdapter = new zoot.event.ControlAdapter( this );
	public AbstractDropDownPane() {
		this.setBackgroundColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 230 ) );
		this.setForegroundColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 ) );
		this.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.DEFAULT_CURSOR ) );
	}
//	@Override
//	public void addNotify() {
//		super.addNotify();
//		this.addMouseListener( controlAdapter );
//		this.addMouseMotionListener( controlAdapter );
//	}
//	@Override
//	public void removeNotify() {
//		this.removeMouseListener( controlAdapter );
//		this.removeMouseMotionListener( controlAdapter );
//		super.removeNotify();
//	}
	
	private static final int AFFORDANCE_WIDTH = 6;
	private static final int AFFORDANCE_HALF_HEIGHT = 5;
	@Override
	protected int getInsetTop() {
//		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
			return 1;
//		} else {
//			return 0;
//		}
	}
	@Override
	protected int getInsetLeft() {
		return 1;
	}
	@Override
	protected int getInsetBottom() {
//		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
			return 1;
//		} else {
//			return 0;
//		}
	}
	@Override
	protected int getInsetRight() {
		return 5 + AbstractDropDownPane.AFFORDANCE_WIDTH;
	}
	
	protected boolean isInactiveFeedbackDesired() {
		return true;
	}
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x+1, y+1, width - 3, height - 3 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isActive() || this.isInactiveFeedbackDesired() ) {
			java.awt.Paint prevPaint = g2.getPaint();
			if( this.isActive() ) {
				g2.setColor( java.awt.Color.WHITE );
			}
			try {
				this.fillBounds( g2, x, y, width, height );
			} finally {
				g2.setPaint( prevPaint );
			}
		}
		
		float x0 = x + width - 4 - AbstractDropDownPane.AFFORDANCE_WIDTH;
		float x1 = x0 + AbstractDropDownPane.AFFORDANCE_WIDTH;
		float xC = (x0 + x1) / 2;

		float yC = (y + height)/2;
		float y0 = yC-AbstractDropDownPane.AFFORDANCE_HALF_HEIGHT;
		float y1 = yC+AbstractDropDownPane.AFFORDANCE_HALF_HEIGHT;

		java.awt.Color triangleFill;
		java.awt.Color triangleOutline;
		if( this.isActive() ) {
			triangleFill = java.awt.Color.YELLOW;
			triangleOutline = java.awt.Color.BLACK;
		} else {
			triangleFill = this.getForegroundColor();
			triangleOutline = null;
		}

		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

//		float[] xs = { x0, xC, x1 };
//		float[] ys = { y0, y1, y0 };
		
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( x0, y0 );
		path.lineTo( xC, y1 );
		path.lineTo( x1, y0 );
		path.closePath();
		
		
		g2.setColor( triangleFill );
		g2.fill( path );
//		g2.fillPolygon( xs, ys, 3 );
		if( triangleOutline != null ) {
			g2.setColor( triangleOutline );
			g2.draw( path );
//			g2.drawPolygon( xs, ys, 3 );
		}
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isActive() ) {
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
			g2.setColor( java.awt.Color.BLUE );
			g2.draw( new java.awt.geom.Rectangle2D.Float( 1.5f, 1.5f, width-3.0f, height-3.0f ) );
		} else {
			if( this.isInactiveFeedbackDesired() ) {
				g2.setColor( java.awt.Color.WHITE );
				//g2.drawRect( x, y, width-1, height-1 );
				g2.drawLine( x+1, y+1, x+width-4, y+1 );
				g2.drawLine( x+1, y+1, x+1, y+height-4 );
			}
		}
	}
}
