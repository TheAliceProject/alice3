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
/*package-private*/ class Frame extends org.lgna.stencil.Feature {
	private enum Paint {
		SINGLETON;
		private java.awt.Paint internal;
		Paint() {
			int width = 1;
			int height = 4;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			java.awt.Color c = org.lgna.stencil.Stencil.STENCIL_BASE_COLOR;
			g2.setColor( new java.awt.Color( c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()/4 ) );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( org.lgna.stencil.Stencil.STENCIL_LINE_COLOR );
			int y = 0;
			g2.drawLine( 0, y, width, y );
			g2.dispose();
			this.internal = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
	};
	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 3.0f ); 

	public Frame( org.lgna.croquet.RuntimeResolver< ? extends org.lgna.croquet.TrackableShape > trackableShapeResolver, ConnectionPreference connectionPreference ) {
		super( trackableShapeResolver, connectionPreference );
	}
	@Override
	protected boolean isPathRenderingDesired() {
		return true;
	}
	@Override
	protected java.awt.Insets getBoundsInsets() {
		return null;
	}
	@Override
	protected java.awt.Insets getContainsInsets() {
		return null;
	}
	@Override
	protected java.awt.Insets getPaintInsets() {
		return null;
	}
	@Override
	public java.awt.geom.Area getAreaToSubstractForContains( org.lgna.croquet.components.Component< ? > asSeenBy ) {
//		edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = this.getTrackableShape();
//		java.awt.Rectangle bounds = this.getScrollBarBoundsIfNecessary( trackableShape, asSeenBy);
//		if( bounds != null ) {
//			return new java.awt.geom.Area( bounds );
//		} else {
			return null;
//		}
	}

	private static void fill( java.awt.Graphics2D g2, java.awt.Shape shape, float x, float y, double theta ) {
		java.awt.geom.AffineTransform m = g2.getTransform();
		try {
			g2.translate( x, y );
			g2.rotate( theta );
			g2.fill( shape );
		} finally {
			g2.setTransform( m );
		}
	}
	@Override
	protected void paint(java.awt.Graphics2D g2, java.awt.Shape shape, Connection actualConnection) {
		java.awt.Rectangle componentBounds = shape.getBounds();
		g2.setPaint( Paint.SINGLETON.internal );
		g2.fillRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );

		g2.setStroke( STROKE );
		g2.setPaint( java.awt.Color.RED );
		g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
		
		g2.setPaint( java.awt.Color.BLACK );

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( -3, -3 );
		path.lineTo( -3, 9 );
		path.lineTo( 3, 6 );
		path.lineTo( 3, 3 );
		path.lineTo( 6, 3 );
		path.lineTo( 9, -3 );
		path.closePath();

		//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
		fill( g2, path, componentBounds.x, componentBounds.y, 0 );
		fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y, Math.PI/2 );
		fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y + componentBounds.height, Math.PI );
		fill( g2, path, componentBounds.x, componentBounds.y + componentBounds.height, 3*Math.PI/2 );
		//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
	}
}
