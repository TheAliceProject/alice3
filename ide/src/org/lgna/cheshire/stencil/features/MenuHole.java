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
package org.lgna.cheshire.stencil.features;

/**
 * @author Dennis Cosgrove
 */
public class MenuHole extends Hole {
	private boolean isPathRenderingDesired;
	private boolean isHoleRenderingDesired;
	private boolean isCheckMarkRenderingDesired;
	public MenuHole( 
			org.lgna.croquet.RuntimeResolver< ? extends org.lgna.croquet.TrackableShape > trackableShapeResolver, 
			ConnectionPreference connectionPreference, 
			boolean isPathRenderingDesired, 
			boolean isHoleRenderingDesired,
			boolean isCheckMarkRenderingDesired ) {
		super( trackableShapeResolver, connectionPreference );
		this.isPathRenderingDesired = isPathRenderingDesired;
		this.isHoleRenderingDesired = isHoleRenderingDesired;
		this.isCheckMarkRenderingDesired = isCheckMarkRenderingDesired;
	}
	@Override
	protected boolean isPathRenderingDesired() {
		return this.isPathRenderingDesired;
	}
	@Override
	protected boolean isHoleRenderingDesired() {
		return this.isHoleRenderingDesired;
	}
	@Override
	public java.awt.geom.Area getAreaToSubstractForContains( org.lgna.croquet.components.Component< ? > asSeenBy ) {
		if( this.isHoleRenderingDesired() ) {
			return super.getAreaToSubstractForContains( asSeenBy );
		} else {
			return null;
		}
	}
	@Override
	public java.awt.geom.Area getAreaToSubstractForPaint( org.lgna.croquet.components.Component< ? > asSeenBy ) {
		if( this.isHoleRenderingDesired() ) {
			return super.getAreaToSubstractForPaint( asSeenBy );
		} else {
			return null;
		}
	}	
	
	private static java.awt.Shape createCheckMark( int size ) {
		float x0 = 0.0f * size;
		float xA = 0.325f * size;
		float xB = 0.575f * size;

		float y0 = 0.0f * size;
		float yA = 0.75f * size;
		float y1 = 1.0f * size;
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( x0, y1 );
		path.lineTo( xB, y1 );
		path.lineTo( xB, y0 );
		path.lineTo( xA, y0 );
		path.lineTo( xA, yA );
		path.lineTo( x0, yA );
		path.closePath();
		return path;
	}
	@Override
	protected void paint( java.awt.Graphics2D g2, java.awt.Shape shape, Connection actualConnection ) {
		super.paint( g2, shape, actualConnection );
		if( this.isHoleRenderingDesired() ) {
			//pass
		} else {
			if( this.isCheckMarkRenderingDesired ) {
				if( this.isEntered() ) {
					if( shape != null ) {
						java.awt.Rectangle bounds = shape.getBounds();
						if( bounds != null ) {
							int size = bounds.height;
							java.awt.Shape checkMark = createCheckMark( size );
							java.awt.geom.AffineTransform m = g2.getTransform();
							
							int x = bounds.x;
							if( actualConnection == Connection.EAST ) {
								x -= 0;
								x -= size / 2;
							} else {
								x += 8;
								x += size / 4;
								x += bounds.width;
							}
							
							g2.translate( x, bounds.y-size/8 );
							g2.rotate( Math.PI/5 );
							g2.setPaint( java.awt.Color.GREEN );
							g2.fill( checkMark );
							g2.setPaint( java.awt.Color.GREEN.darker() );
							g2.draw( checkMark );
							g2.setTransform( m );
						}
					}
				}
			}
		}
	}
}
