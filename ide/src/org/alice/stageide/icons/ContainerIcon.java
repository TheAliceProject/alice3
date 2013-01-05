/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public abstract class ContainerIcon extends org.lgna.croquet.icon.AbstractIcon {
	private final int childCount;

	public ContainerIcon( java.awt.Dimension size, int childCount ) {
		super( size );
		this.childCount = childCount;
	}

	protected abstract void paintIconMain( java.awt.Component c, java.awt.Graphics2D g2 );

	private static java.awt.Shape createShapeAround( java.awt.geom.Rectangle2D bounds ) {
		double x0 = bounds.getX() - 2;
		double y0 = bounds.getY() - 4;
		double x1 = x0 + bounds.getWidth() + 4;
		double y1 = y0 + bounds.getHeight() + 5;
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		rv.moveTo( x0, y1 );
		rv.lineTo( x0, y0 );
		rv.lineTo( x0 + 7, y0 );
		rv.lineTo( x0 + 10, y0 + 3 );
		rv.lineTo( x1, y0 + 3 );
		rv.lineTo( x1, y1 );
		rv.closePath();
		return rv;
	}

	@Override
	protected final void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
		this.paintIconMain( c, g2 );

		if( this.getIconHeight() > 32 ) {
			String s = Integer.toString( this.childCount );
			java.awt.FontMetrics fm = g2.getFontMetrics();
			java.awt.geom.Rectangle2D bounds = fm.getStringBounds( "00", g2 );

			java.awt.Shape shape = createShapeAround( bounds );
			java.awt.geom.Rectangle2D shapeBounds = shape.getBounds();

			final double INSET = 0;

			double x = this.getIconWidth() - shapeBounds.getWidth() - INSET;
			double y = shapeBounds.getHeight() + INSET;

			g2.translate( x, y );
			try {
				g2.setPaint( new java.awt.Color( 255, 255, 191 ) );
				g2.fill( shape );
				g2.setPaint( java.awt.Color.GRAY );
				g2.draw( shape );

				g2.setPaint( java.awt.Color.BLACK );
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, s, bounds );
			} finally {
				g2.translate( -x, -y );
			}
		}
	}
}
