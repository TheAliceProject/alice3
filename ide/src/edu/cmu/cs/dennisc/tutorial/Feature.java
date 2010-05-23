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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ abstract class Feature {
	public enum ConnectionPreference {
		NORTH_SOUTH,
		EAST_WEST
	}
	private static final java.awt.Stroke ARROW_STROKE = new java.awt.BasicStroke( 3.0f ); 

	private edu.cmu.cs.dennisc.croquet.Component<?> component;
	private ConnectionPreference connectionPreference;
	public Feature( edu.cmu.cs.dennisc.croquet.Component<?> component, ConnectionPreference connectionPreference ) {
		this.component = component;
		this.connectionPreference = connectionPreference;
	}
	protected abstract java.awt.Rectangle updateBoundsForContains( java.awt.Rectangle rv );
	protected abstract java.awt.Rectangle updateBoundsForPaint( java.awt.Rectangle rv );
	public final java.awt.geom.Area getAreaToSubstractForContains( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy ) {
		if( this.component != null ) {
			java.awt.Rectangle bounds = this.component.getBounds( asSeenBy );
			bounds = this.updateBoundsForContains( bounds );
			if( bounds != null ) {
				return new java.awt.geom.Area( bounds );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	private java.awt.Rectangle getBoundsForPaint( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy ) {
		if( this.component != null ) {
			java.awt.Rectangle bounds = this.component.getBounds( asSeenBy );
			return this.updateBoundsForPaint( bounds );
		} else {
			return null;
		}
	}
	public final java.awt.geom.Area getAreaToSubstractForPaint( edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy ) {
		java.awt.Rectangle bounds = this.getBoundsForPaint( asSeenBy );
		if( bounds != null ) {
			return new java.awt.geom.Area( bounds );
		} else {
			return null;
		}
	}
	protected abstract void paint( java.awt.Graphics2D g2, java.awt.Rectangle componentBounds );
	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo ) {
		float xVector = xTo-xFrom; 
		float yVector = yTo-yFrom;
		
		final float A = 0.15f;
		final float B = 1.0f;
		
		float xA = xFrom + xVector*A;
		float yA = yFrom + yVector*A;

		float xB = xFrom + xVector*B;
		float yB = yFrom + yVector*B;
		
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xFrom, yFrom );
		path.curveTo( xB, yA, xA, yB, xTo, yTo );

		g2.draw( path );
		//g2.drawLine( (int)xB, (int)yA, (int)xA, (int)yB );
	}
	public final void paint( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Component<?> asSeenBy, edu.cmu.cs.dennisc.croquet.Component<?> note ) {
		java.awt.Rectangle componentBounds = getBoundsForPaint( asSeenBy );
		this.paint( g2, componentBounds );

		g2.setPaint( java.awt.Color.BLACK );
		java.awt.Rectangle noteBounds = note.getBounds( asSeenBy );
		
		java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( componentBounds, javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER );
		java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER );
		
		final int ARROW_WIDTH = 19;
		final int ARROW_HALF_HEIGHT = 6;
		
		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setStroke( ARROW_STROKE );
		drawPath( g2, ptNote.x, ptNote.y, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1);
		g2.setStroke( prevStroke );

		int x = ptComponent.x;
		int y = ptComponent.y - ARROW_HALF_HEIGHT;
		g2.translate( x, y );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.WEST, new java.awt.Dimension( ARROW_WIDTH, ARROW_HALF_HEIGHT*2+1 ) );
		g2.translate( -x, -y );
	}
}
