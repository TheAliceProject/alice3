/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class SunIcon implements javax.swing.Icon {
	//todo
	private static final int SMALL_ICON_SIZE = 24;

	@Override
	public int getIconWidth() {
		return SMALL_ICON_SIZE;
	}

	@Override
	public int getIconHeight() {
		return SMALL_ICON_SIZE;
	}

	private java.awt.Shape createArc( float size ) {
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		rv.moveTo( 0.0f, 0.0f );
		rv.lineTo( size, 0.0f );
		rv.quadTo( size, size, 0.0f, size );
		rv.closePath();
		return rv;
	}

	@Override
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.geom.AffineTransform m = g2.getTransform();
		Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			java.awt.Shape innerArc = this.createArc( 16.0f );
			java.awt.Shape outerArc = this.createArc( 18.0f );

			g2.translate( 4.0f, 4.0f );
			java.awt.geom.GeneralPath pathRays = new java.awt.geom.GeneralPath();
			double thetaN = Math.PI / 2.0;
			double thetaDelta = thetaN / 8.0;
			g2.setColor( new java.awt.Color( 255, 210, 0 ) );
			for( double theta = 0.0; theta <= thetaN; theta += thetaDelta ) {
				pathRays.moveTo( 0.0f, 0.0f );
				pathRays.lineTo( (float)( Math.cos( theta ) * 20.0 ), (float)( Math.sin( theta ) * 20.0 ) );
			}
			g2.draw( pathRays );
			g2.fill( outerArc );

			g2.setColor( new java.awt.Color( 230, 230, 0 ) );
			g2.fill( innerArc );
		} finally {
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
			g2.setTransform( m );
		}
	}
}
