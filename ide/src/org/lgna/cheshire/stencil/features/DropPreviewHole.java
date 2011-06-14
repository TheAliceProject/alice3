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
public class DropPreviewHole extends org.lgna.cheshire.stencil.features.Hole {
	/*package-private*/ static final java.awt.Color STENCIL_BASE_COLOR =  new java.awt.Color( 181, 140, 140, 45 );
	/*package-private*/ static final java.awt.Color STENCIL_LINE_COLOR =  new java.awt.Color( 92, 48, 24, 63 );
	private enum Paint {
		SINGLETON;
		private java.awt.Paint internal;
		Paint() {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
			g2.setColor( STENCIL_BASE_COLOR );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( STENCIL_LINE_COLOR );
			g2.drawLine( 0, height, width, 0 );
			g2.fillRect( 0, 0, 1, 1 );
			g2.dispose();
			this.internal = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
	};
	private static final java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 220 );
	private static final java.awt.Color SHADOW_COLOR = new java.awt.Color( 31, 31, 31 );
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 3.0f ); 

	public DropPreviewHole( org.lgna.croquet.resolvers.RuntimeResolver< ? extends org.lgna.croquet.components.TrackableShape > trackableShapeResolver, ConnectionPreference connectionPreference ) {
		super( trackableShapeResolver, connectionPreference );
		this.setHeightConstraint( 64 );
	}
	@Override
	protected boolean isPathRenderingDesired() {
		return false;
	}
	@Override
	public java.awt.geom.Area getAreaToSubstractForContains( org.lgna.croquet.components.Component< ? > asSeenBy ) {
		return null;
	}
	
	@Override
	protected void paint( java.awt.Graphics2D g2, java.awt.Shape shape, org.lgna.stencil.Feature.Connection actualConnection ) {
		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Stroke prevStroke = g2.getStroke();
		try {
			g2.setPaint( Paint.SINGLETON.internal );
			g2.fill( shape );
			g2.setStroke( STROKE );
			java.awt.Rectangle bounds = shape.getBounds();
			java.awt.geom.GeneralPath highlightPath = new java.awt.geom.GeneralPath();
			highlightPath.moveTo( bounds.x, bounds.y+bounds.height );
			highlightPath.lineTo( bounds.x+bounds.width, bounds.y+bounds.height );
			highlightPath.lineTo( bounds.x+bounds.width, bounds.y );
			java.awt.geom.GeneralPath shadowPath = new java.awt.geom.GeneralPath();
			shadowPath.moveTo( bounds.x, bounds.y+bounds.height );
			shadowPath.lineTo( bounds.x, bounds.y );
			shadowPath.lineTo( bounds.x+bounds.width, bounds.y );
			g2.setPaint( HIGHLIGHT_COLOR );
			g2.draw( highlightPath );
			g2.setPaint( SHADOW_COLOR );
			g2.draw( shadowPath );
		} finally {
			g2.setStroke( prevStroke );
			g2.setPaint( prevPaint );
		}
	}
}
